(ns cool-clojure-code.core
  (:require [clojure.core.async :as async :refer [<! >! go chan]]))

(defn fake-long-computation [id duration-ms]
  (go
    (println (str "Starting computation " id "..."))
    (<! (async/timeout duration-ms)) ; Simulate work
    (let [result (str "Computation " id " done! Result: " (rand-int 100))]
      (println result)
      result)))

(defn -main [& args]
  (let [c1 (fake-long-computation "A" 1000)
        c2 (fake-long-computation "B" 1500)]
    (println "Launched computations, waiting for results...")
    (println (str "Final result from A: " (<! c1)))
    (println (str "Final result from B: " (<! c2)))
    (println "All computations finished."))
  (System/exit 0)) ; Ensure the program exits after -main is done
