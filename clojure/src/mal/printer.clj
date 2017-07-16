(ns mal.printer
  (:require [clojure.string :as string]))

(declare print-string)

(defn list->str [l]
  (->> (mapv print-string l)
       (string/join " ")
       (#(str "(" % ")"))))

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
    list? (list->str data)
    string? (escape-str data)
    :else (ex-info "No printer mapped" {:data data :class (class data)})))

