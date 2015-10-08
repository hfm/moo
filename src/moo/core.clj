(ns moo.core
  (:use
    [midje.sweet :only (unfinished)]))

(declare init-model create-game)

(unfinished create-game calc-state command-fits-state? result-text)

(def moo (atom nil))

(defn print-result
  [[op & params :as res]]
  (when res
    (println (result-text op params)))
  res)

(def handler-table
  {:new (fn [_] (create-game))
   :guess #(@moo %)
   :help (fn [_] [:keep :help])
   :quit (fn [_] (@moo :quit))
   :exit (fn [_] nil)
   :unknown (fn [_] [:keep :bad-command])})

(defn handle-command
  [[cmd param]]
  (if (command-fits-state? cmd (calc-state))
    (let [[op :as res]
          ((handler-table cmd) param)]
      (when (= op :pre-game) (init-model))
      res)
    [:keep :bad-state]))

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

(defn init-model []
  (reset! moo nil))

(defn -main
  [& args]
  (init-model)
  (init-view)
  (run-shell))
