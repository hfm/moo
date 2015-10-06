(ns moo.core
  (:use
    [midje.sweet :only (unfinished)]))

(unfinished handle-command print-result)

(def command-table
  {"new" :new
   "help" :help
   "quit" :quit
   "exit" :exit})

(defn command-from-line [line]
  (if (nil? line)
    [:exit]
    (if-let
      [[_ _ code]
       (re-find #"^(guess )? *(\d{3})$" line)]
      [:guess (mapv #(- (int %) (int \0))
                    (seq code))]
      [(command-table line :unknown)])))

(defn read-command []
  (command-from-line (read-line)))

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
