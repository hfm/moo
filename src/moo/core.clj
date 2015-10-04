(ns moo.core
  (:use
    [midje.sweet :only (unfinished)]))

(unfinished init-view run-shell)

(def moo (atom nil))

(defn init-model []
  (reset! moo nil))

(defn -main
  [& args]
  (init-model)
  (init-view)
  (run-shell))

