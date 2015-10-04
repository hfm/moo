(ns moo.core
  (:use
    [midje.sweet :only (unfinished)]))

(unfinished run-shell)

(defn init-view []
  (println "Welcome to Moo!")
  (println "Type 'help' to see how to play."))

(def moo (atom nil))

(defn init-model []
  (reset! moo nil))

(defn -main
  [& args]
  (init-model)
  (init-view)
  (run-shell))
