(ns moo.core
  (:use
    [midje.sweet :only (unfinished)]))

(unfinished init-model init-view run-shell)
(defn -main
  [& args]
  (init-model)
  (init-view)
  (run-shell))
