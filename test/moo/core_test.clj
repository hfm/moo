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

(fact "about ini-model"
      (reset! moo 123)
      (init-model) => anything
      @moo => nil)
