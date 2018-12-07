(ns mal.eval-test
  (:require [clojure.test :refer :all]
            [mal.env]
            [mal.eval :refer :all])
  (:refer-clojure :exclude [eval]))

(def empty-env (mal.env/new-env nil {}))
(def env (mal.env/new-env nil {'+ +}))

(deftest eval-test
  (testing "an ast that evaluates to itself"
    (is (= (eval :a empty-env) [:a empty-env]))
    (is (= (eval 1 empty-env) [1 empty-env]))
    (is (= (eval "lara" empty-env) ["lara" empty-env])))

  (testing "a function call"
    (is (= (eval '(+ 1 2) env) [3 env]))
    (is (= (eval '(+ 1 (+ 3 4)) env) [8 env])
        "recursively eval functions")
    (is (thrown? Exception (eval '(/ 1 2) env))))

  (testing "a vector, evaluates all elements"
    (is (= (eval [1 2] empty-env) [[1 2] empty-env]))
    (is (= (eval [(+ 3 4) (+ 1 2)] env) [[7 3] env])))

  (testing "a map, evaluates all elements"
    (is (= (eval {:a {:b (+ 1 2)}} env) [{:a {:b 3}} env]))
    (is (= (eval {(+ 1 2) {:b :a}} env) [{3 {:b :a}} env])))

  (testing "a special form def!, evaluates and insert to env"
    (let [[result new-env] (eval '(def! three (+ 1 2)) env)]
      (is (= result 3))
      (is (= (mal.env/get-env new-env 'three) 3))))

  (testing "a special form let*, let scope should not leak, not letting you def! stuff globally inside let-bindings"
    (let [[result new-env] (eval '(let* [a 1] a) empty-env)]
      (is (= result 1))
      (is (= empty-env new-env))))

  (testing "a special form let*, later bindings can see earlier ones"
    (let [[result new-env] (eval '(let* [a 1
                                         b (+ a 2)] b)
                                 env)]
      (is (= result 3))))

  (testing "a special form let*, evaluates all expressions in the body"
    (let [[result new-env] (eval '(let* [a 1]
                                    (+ 1 a)
                                    (+ 2 a))
                                 env)]
      (is (= result 3))))

  (testing "a special form `do`, evaluates all exprs and return last"
    (let [[result new-env] (eval '(do (def! x 1)
                                      (+ 3 4)) env)]
      (is (= result 7))
      (is (= (mal.env/get-env new-env 'x) 1) "the expr before the last one was evaluated")))

  (testing "a special form `if`, only evaluates one branch"
    (let [[result new-env] (eval '(if true
                                    (+ 1 2)
                                    (def! x 1)) env)]
      (is (= result 3))
      (is (= new-env env) "else not evaluated"))

    (let [[result new-env] (eval '(if false
                                    (def! x 1)
                                    (+ 1 2)) env)]
      (is (= result 3))
      (is (= new-env env) "then not evaluated")))

  (testing "a special form `if`, `nil` makes else branch be evaluated too"
    (let [[result new-env] (eval '(if nil
                                    (+ 3 4)
                                    (+ 1 2)) env)]
      (is (= result 3))))

  (testing "a special form `if`, without else branch"
    (let [[result new-env] (eval '(if false
                                    (+ 1 2)) env)]
      (is (= result nil)))))
