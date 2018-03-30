(ns mal.eval
  (:require [mal.misc :as misc]
            [mal.env])
  (:refer-clojure :exclude [eval]))

(declare eval)

(defn- eval-every-element-v
  [ast-seq env]
  (reduce (fn [[partial-result partial-env] a]
            (let [[eval-result post-eval-env] (eval a partial-env)]
              [(conj partial-result eval-result) post-eval-env]))
          [[] env]
          ast-seq))

(defn- eval-every-element [ast env]
  (-> (eval-every-element-v ast env)
      (update 0 (partial apply list))))

(defn- eval-map [ast env]
  (-> ast
      misc/map->vec
      (eval-every-element-v env)
      (update 0 #(apply hash-map %))))

(defn- eval-ast
  "Returns result and new env"
  [ast env]
  (condp #(%1 %2) ast
    symbol? [(mal.env/get-env env ast) env]
    list? (eval-every-element ast env)
    vector? (eval-every-element-v ast env)
    map? (eval-map ast env)
    [ast env]))

(defn eval-and-create-sym [[sym expr] env]
  (let [[value env2] (eval expr env)
        new-env (mal.env/set-env env2 sym value)]
    [value new-env]))

(defn- eval-def! [l env]
  (let [[_ sym expr] l]
    (eval-and-create-sym [sym expr] env)))

(defn- eval-normal-function [l env]
  (let [[[ast-fn & ast-params] new-env] (eval-ast l env)
        result (apply ast-fn ast-params)]
    [result new-env]))

(defn- eval-let-bindings [binding-list env]
  (assert (even? (count binding-list)) "let binding list must be even")
  (->> (partition 2 binding-list)
       (reduce (fn [env pair]
                 (second (eval-and-create-sym pair env)))
               env)))

(defn- eval-let [l env]
  (let [[_let* binding-list body] l
        new-env (eval-let-bindings binding-list env)
        [result _] (eval body new-env)]
    [result env]))

(defn- eval-list
  "Eval a form. Returns result and new env"
  [l env]
  (if (empty? l)
    [l env]
    (case (first l)
      def! (eval-def! l env)
      let* (eval-let l env)
      (eval-normal-function l env))))

(defn eval [s env]
  (if (list? s)
    (eval-list s env)
    (eval-ast s env)))
