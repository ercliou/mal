(ns mal.printer-test
  (:require [clojure.test :refer :all]
            [mal.printer :refer :all]))

(def simple-form ["(" "myfun" "123" ")" ""])
(def list-in-list ["(" "(" "myfun" "x" ")" "123" ")" ""])
(def crazy-in-list ["(" "(" "a" "b" ")" "(" "c" "d" ")" ")" ""])

(deftest test-
  (testing "print-string"
    (is (= (print-string "a string")
           "\"a string\""))
    (is (= (print-string "ab\"cd")
           "\"ab\\\"cd\""))))


