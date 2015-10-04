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
