(ns mal.step1-read-print
  (:require [mal.reader]
            [mal.printer])
  (:gen-class))

(defn e-read [s] (mal.reader/read-str s))
(defn e-eval [s] s)
(defn e-print [s] (mal.printer/print-string s))

(defn rep [s]
  (-> s e-read e-eval e-print))

(defn -main []
  (while true
    (print "user> ")
    (flush)
    (-> (read-line)
        rep
        println)))
