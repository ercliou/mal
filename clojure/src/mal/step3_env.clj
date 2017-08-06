(ns mal.step3-env
  (:require [mal.reader]
            [mal.printer]
            [mal.misc :as misc]
            [mal.env])
  (:gen-class))

;; TODO stopped here
(def repl-env (mal.env/new-env nil {'+ +
                                    '- -
                                    '* *
                                    '/ /}))

(declare e-eval)

(defn eval-ast [ast env]
  (condp #(%1 %2) ast
    symbol? (mal.env/get-env env ast)
    list? (map #(e-eval % env) ast)
    vector? (mapv #(e-eval % env) ast)
    map? (->> ast
              misc/map->vec
              (mapv #(e-eval % env))
              (apply hash-map))
    ast))

(defn- eval-list [l env]
  (if (empty? l)
      l
      (let [[ast-fn & ast-params] (eval-ast l env)]
        (apply ast-fn ast-params))))

(defn e-read [s] (mal.reader/read-str s))

(defn e-eval [s env]
  (if (list? s)
    (eval-list s env)
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
