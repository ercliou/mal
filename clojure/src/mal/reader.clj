(ns mal.reader
  (:require [clojure.pprint]))

(def regex #"[\s,]*(~@|[\[\]{}()'`~^@]|\"(?:\\.|[^\\\"])*\"|;.*|[^\s\[\]{}('\"`,;)]*)")

(defn tokenizer [s]
  (->> (re-seq regex s)
       (map second)))

(defn- ->list [coll]
  (->> coll
       (into '())
       reverse))

(def recursion-lvl (atom 0))
(def ^:dynamic *debug?* false)

(defn dprint [data]
  (when *debug?*
    (dotimes [r @recursion-lvl]
      (print r "\t"))
    (clojure.pprint/pprint data))
  data)

(declare read-form*)

(defn check-balanced-coll [closing-token actual-token tokens]
  (when (not= closing-token actual-token)
    (throw (ex-info "Not balanced!" {:expecting closing-token
                                     :tokens tokens}))))

(defn read-list [tokens result]
  (swap! recursion-lvl inc)
  (let [[t & ts] tokens
        [[at & ats] elements] (read-form* ts [])] 
    (swap! recursion-lvl dec)
    (condp = t
      "(" (do (check-balanced-coll ")" at tokens)
              [ats (->> elements
                        ->list
                        (conj result))])
      "[" (do (check-balanced-coll "]" at tokens)
              [ats (->> elements
                        (into [])
                        (conj result))]))))

(defn read-atom
  "Naive impl, expecting: integer, symbol"
  [[t & ts] result]
  (let [mal-data (read-string t)] ; cheating?
    [ts (conj result mal-data)]))

(defn read-fn [t]
  (if (contains? #{"(" "[" "{"} t)
    read-list
    read-atom))

(defn read-form* [tokens result]
  (let [[t & ts] tokens]
    (if (or (empty? t)
            (contains? #{")"} t))
      [tokens result]
      (let [[tokens result] ((read-fn t) tokens result)]
        (recur tokens result)))))

(defn read-form [tokens]
  (-> tokens
      (read-form* [])
      second
      first))

(defn read-str [s]
  (-> (tokenizer s)
      read-form))

