(ns mal.reader-test
  (:require [clojure.test :refer :all]
            [mal.reader :refer :all]))

(def simple-form ["(" "myfun" "123" ")" ""])
(def list-in-list ["(" "(" "myfun" "x" ")" "123" ")" ""])
(def crazy-in-list ["(" "(" "a" "b" ")" "(" "c" "d" ")" ")" ""])

(deftest test-
  (testing "read-atom"
    (is (= (read-atom ["42" "rest"] [])
           [["rest"] [42]]))
    (is (= (read-atom ["nil" "rest"] [])
           [["rest"] [nil]])))

  (testing "read-form*"
    (testing "with simple-from"
      (is (= (read-form* simple-form [])
             '[("") [(myfun 123)]])))

    (testing "with inner lists"
      (is (= (read-form* list-in-list [])
             '[("") [((myfun x) 123)]])))
    
    (testing "with crazy list"
      (is (= (read-form* crazy-in-list [])
             '[("") [((a b) (c d))]])))))


