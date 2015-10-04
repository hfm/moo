(ns moo.core
  (:use
    [midje.sweet :only (unfinished)]))

(unfinished read-command handle-command print-result)

(defn run-shell []
  (loop [cmd (read-command)]
    (let [res (handle-command cmd)]
      (print-result res)
      (when res
        (recur (read-command))))))

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
