(ns mal.step0-repl
  (:gen-class))

(defn e-read [s] s)
(defn e-eval [s] s)
(defn e-print [s] s)

(defn rep [s]
  (-> s e-read e-eval e-print))

(defn -main []
  (while true
    (print "user> ")
    (flush)
    (-> (read-line)
        rep
        println)))
