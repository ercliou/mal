(ns mal.env)

(defn new-env
  ([outer data]
   {:outer outer
    :data data})
  ([outer]
   (new-env outer {})))

(defn set-env [env sym value]
  (assoc-in env [:data sym] value))

(defn find-env
  "Recursively find and return the env that contains the sym"
  [env sym]
  (when env
    (if (contains? (:data env) sym)
      env
      (find-env (:outer env) sym))))

(defn get-env [env sym]
  (if-let [env-result (find-env env sym)]
    (get (:data env) sym)
    (throw (ex-info "Symbol not found" {:symbol sym
                                        :env env}))))

