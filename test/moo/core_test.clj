(ns moo.core-test
  (:use
    moo.core
    midje.sweet))

(fact "about -main"
      (-main) => ..C..
      (provided
        (init-model) => ..A.. :times 1
        (init-view) => ..B.. :times 1
        (run-shell) => ..C.. :times 1))

(fact "about init-view"
      (with-out-str
        (init-view)) => #"^Welcome to Moo!(\n|\r|\r\n)Type 'help' to see how to play.(\n|\r|\r\n)$")

(fact "about init-model"
      (reset! moo 123)
      (init-model) => anything
      @moo => nil)

(fact "about run-shell"
      (run-shell) => anything
      (provided
        (read-command) => ..CMD.. :times 1
        (handle-command ..CMD..) => nil :times 1
        (print-result nil) => ..ANY.. :times 1)

      (run-shell) => anything
      (provided
        (read-command) =streams=>
        [..CMD1.. ..CMD2.. ..CMD3..] :times 3
        (handle-command ..CMD1..) => ..RES1.. :times 1
        (handle-command ..CMD2..) => ..RES2.. :times 1
        (handle-command ..CMD3..) => nil :times 1
        (print-result ..RES1..) => ..ANY1.. :times 1
        (print-result ..RES2..) => ..ANY2.. :times 1
        (print-result nil) => ..ANY3.. :times 1))
