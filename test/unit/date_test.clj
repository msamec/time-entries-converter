(ns unit.date-test
  (:require [clojure.test :refer [deftest is testing]]
            [domain.date :as date]))

(deftest format-date
  (testing "if date can be formatted given valid datetime"
    (is (= "2022-02-07" (date/format-date "2022-02-07T11:59:25+00:00")))
    (is (= "2022-02-07" (date/format-date "2022-02-07T11:59:25"))))
  (testing "if it throws ParseException given invalid datetime"
    (is (thrown? java.text.ParseException (date/format-date "invalid")))))

(deftest format-time
  (testing "if time can be formatted give valid datetime"
    (is (= "11:59:25" (date/format-time "2022-02-07T11:59:25+00:00")))
    (is (= "11:59:25" (date/format-time "2022-02-07T11:59:25"))))
  (testing "if it throws ParseException give invalid datetime"
    (is (thrown? java.text.ParseException (date/format-time "invalid")))))

(deftest seconds->duration
  (testing "if seconds translate to human readable given int"
    (is (= "10 sec" (date/seconds->duration 10)))
    (is (= "1 min, 40 sec" (date/seconds->duration 100)))
    (is (= "2 hr, 46 min, 40 sec" (date/seconds->duration 10000)))
    (is (= "1 d, 3 hr, 46 min, 40 sec" (date/seconds->duration 100000))))
  (testing "if throws AssertionError given invalid input"
    (is (thrown? java.lang.AssertionError (date/seconds->duration "string")))))
