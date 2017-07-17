(ns mal.step2-eval
  (:require [mal.reader]
            [mal.printer])
  (:gen-class))

(def repl-env {'+ +
               '- -
               '* *
               '/ /})

(declare e-eval)

(defn eval-ast [ast env]
  (condp #(%1 %2) ast
    symbol? (get repl-env ast
                 #(throw (ex-info "Symbol not found" {:symbol ast})))
    list? (map #(e-eval % env) ast)
    ast))

(defn e-read [s] (mal.reader/read-str s))

(defn e-eval [s env]
  (if (list? s)
    (if (empty? s)
      s
      (let [[ast-fn & ast-params] (eval-ast s env)]
        (apply ast-fn ast-params)))
    (eval-ast s env)))

(defn e-print [s] (mal.printer/print-string s))

(defn rep [s env]
  (-> s
      e-read
      (e-eval env)
      e-print))

(defn check-eof [s]
  (if (some? s)
    s
    (do (println "EOF. Exiting..")
        (System/exit 0))))

(defn -main []
  (while true
    (try
      (print "user> ")
      (flush)
      (-> (read-line)
          check-eof
          (rep repl-env)
          println)
      (catch Exception e (println e)))))
