(ns mal.misc)

(defn map->vec [m]
  (reduce (fn [result [k v]]
            (conj result k v)) [] m))

