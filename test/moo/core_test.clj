(ns moo.core-test
  (:use
    moo.core
    midje.sweet))

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
             (handle-command ..EXIT..) => nil
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
