(ns mal.printer
  (:require [clojure.string :as string]))

(declare print-string)

(defn coll->str [l]
  (let [[prefix suffix] (condp #(%1 %2) l
                          list? ["(" ")"]
                          vector? ["[" "]"])]
    (->> (mapv print-string l)
         (string/join " ")
         (#(str prefix % suffix)))))

(defn escape-str [s]
  (-> (clojure.string/replace s #"\"" "\\\\\"")
      (#(str \" % \"))))

(defn print-string [data]
  ;; not using prn to print, seems like cheating
  (condp #(%1 %2) data
    nil? "nil"
    true? (str data)
    false? (str data)
    symbol? (str data)
    number? (str data)
    coll? (coll->str data)
    string? (escape-str data)
    :else (ex-info "No printer mapped" {:data data :class (class data)})))

