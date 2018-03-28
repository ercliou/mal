(ns mal.step3-env
  (:require [mal.reader]
            [mal.printer]
            [mal.misc :as misc]
            [mal.eval]
            [mal.env])
  (:gen-class))

(def repl-env (mal.env/new-env nil {'+ +
                                    '- -
                                    '* *
                                    '/ /}))

(defn- tap [x] (println "tap=>") (println x) x)

(def e-read mal.reader/read-str)
(def e-eval mal.eval/eval)
(def e-print mal.printer/print-string)

(def env2 (mal.env/set-env repl-env 'x 3))
(comment (rep "x" env2))

(defn rep
  "Read Eval Print
  Returns stringified result and new env modified by Eval."
  [s env]
  (let [[result new-env] (-> s
                             e-read
                             (e-eval env))]
    [(e-print result) new-env]))

(defn check-eof [s]
  (if (some? s)
    s
    (do (println "EOF. Exiting..")
        (System/exit 0))))

(defn -main []
  (try
    (loop [env repl-env]
      (print "user> ")
      (flush)
      (let [[result-str new-env] (-> (read-line)
                                     check-eof
                                     (rep env))]
        (println result-str)
        (recur new-env)))
    (catch Exception e (println e))))
