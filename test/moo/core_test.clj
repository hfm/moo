(ns moo.core-test
  (:use
    moo.core
    midje.sweet))

(fact "about calc-state"
      (reset! moo nil)
      (calc-state) => :pre-game
      (reset! moo 123)
      (calc-state) => :in-game)

(fact "about create-game"
      (reset! moo nil)
      (create-game) => [:in-game ..CODE..]
      (provided
        (gen-code) => ..CODE..
        (moo-fn ..CODE..) => ..MOO..)
      @moo => ..MOO..)

(tabular "about result-text"
         (fact (result-text ?op ?params) => ?text)
         ?op           ?params         ?text
         :in-game      [[7 2 6]]        "Good luck."
         :keep-in-game [[1 2 3] "1H0E"] "123 ... 1H0E"
         :pre-game     [:win  [7 2 6]]  "That's right, conguratulations!"
         :pre-game     [:lose [7 2 6]]  "Boo! It was 726."
         :keep         [:help]
         (str
           "Acceptable commands are:" \newline
           "  help ... Show help message." \newline
           "  new ... Start a new game." \newline
           "  CODE ... Your guess like 123." \newline
           "  quit ... Quit the game." \newline
           "  exit ... Exit the program.")
         :keep [:bad-state]   "You can't use the command now."
         :keep [:bad-command] "No such command. Type 'help' for usage.")

(fact "about print-result"
      (print-result [:a]) => [:a]
      (provided
        (println (result-text :a nil)) => nil)

      (print-result [:a :b]) => [:a :b]
      (provided
        (println (result-text :a '(:b))) => nil)

      (print-result [:a :b :c]) => [:a :b :c]
      (provided
        (println (result-text :a '(:b :c))) => nil)

      (print-result nil) => nil
      (provided
        (result-text ..OP.. ..PARAMS..) => anything :times 0
        (println ..TEXT..) => anything :times 0)
      )

(fact "about handle-command"
      (handle-command [:new]) => [:in-game ..CODE..]
      (provided
        (command-fits-state? :new (calc-state)) => true
        (create-game) => [:in-game ..CODE..])

      (handle-command [:help]) => [:keep :help]
      (provided
        (command-fits-state? :help (calc-state)) => true)

      (handle-command [:exit]) => nil
      (provided
        (command-fits-state? :exit (calc-state)) => true)

      (handle-command [:unknown]) => [:keep :bad-command]
      (provided
        (command-fits-state? :unknown (calc-state)) => true)

      (handle-command [:new]) => [:keep :bad-state]
      (provided
        (command-fits-state? :new (calc-state)) => false)

      (handle-command [:new]) => [:pre-game]
      (provided
        (command-fits-state? :new (calc-state)) => true
        (create-game) => [:pre-game]
        (init-model) => ..ANY..)

      (defn dummy-moo [_])
      (reset! moo #'dummy-moo)

      (handle-command [:guess [1 2 3]]) => [:op ..PARA1.. ..PARA2..]
      (provided
        (command-fits-state? :guess (calc-state)) => true
        (dummy-moo [1 2 3]) => [:op ..PARA1.. ..PARA2..])

      (handle-command [:quit]) => [:op ..PARA1.. ..PARA2..]
      (provided
        (command-fits-state? :quit (calc-state)) => true
        (dummy-moo :quit) => [:op ..PARA1.. ..PARA2..])
      )

(tabular "abount command-from-line"
         (fact (command-from-line ?l) => ?c)
         ?l          ?c
         "help"      [:help]
         "exit"      [:exit]
         "quit"      [:quit]
         "guess 123" [:guess [1 2 3]]
         "123"       [:guess [1 2 3]]
         "gue 123"   [:unknown]
         "1234"      [:unknown]
         "foo"       [:unknown]
         nil         [:exit])

(fact "abound read-command"
      (read-command) => ..CMD..
      (provided
        (command-from-line (read-line)) => ..CMD..))

(fact "about run-shell"
      (run-shell) => anything
      (provided
        (read-command) => ..CMD.. :times 1
        (handle-command ..CMD..) => nil :times 1
        (print-result nil) => nil :times 1) ;

      (run-shell) => anything
      (provided
        (read-command) =streams=>
        [..CMD1.. ..CMD2.. ..CMD3..] :times 3
        (handle-command ..CMD1..) => ..RES1.. :times 1
        (handle-command ..CMD2..) => ..RES2.. :times 1
        (handle-command ..CMD3..) => nil :times 1
        (print-result ..RES1..) => ..RES1.. :times 1 ;
        (print-result ..RES2..) => ..RES2.. :times 1 ;
        (print-result nil) => nil :times 1)) ;

(future-fact "about sub functions"
             (handle-command ..EXIT..) => nil ;
             (print-result ..ANY..) => ..ANY..)
(fact "about init-view"
      (with-out-str
        (init-view)) => #"^Welcome to Moo!(\n|\r|\r\n)Type 'help' to see how to play.(\n|\r|\r\n)$")

(fact "about init-model"
      (reset! moo 123)
      (init-model) => anything
      @moo => nil)

(fact "about -main"
      (-main) => ..C..
      (provided
        (init-model) => ..A.. :times 1
        (init-view) => ..B.. :times 1
        (run-shell) => ..C.. :times 1))
