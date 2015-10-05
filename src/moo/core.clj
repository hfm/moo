(ns moo.core
  (:use
    [midje.sweet :only (unfinished)]))

(unfinished read-command handle-command print-result)

(defn run-shell []
  (->> (repeatedly read-command)
       (map (comp print-result handle-command))
       (some nil?)))

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
