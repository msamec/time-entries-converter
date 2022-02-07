(ns unit.src.domain.entry-test
  (:require [clojure.test :refer [deftest is]]
            [domain.entry :as entry]))

(deftest create-entry
  (let [entry (entry/create-entry "id" "task-id | description" 100 "2022-02-07T11:59:25+00:00")]
    (is (= "id" (-> entry :id)))
    (is (= "task-id" (-> entry :task-id)))
    (is (= "description" (-> entry :description)))
    (is (= 100 (-> entry :duration)))
    (is (= "2022-02-07" (-> entry :start-date)))
    (is (= "11:59:25" (-> entry :start-time))))
  (is (thrown? java.lang.AssertionError (entry/create-entry 1 "task-id | description" 100 "2022-02-07T11:59:25+00:00")))
  (is (thrown? java.lang.AssertionError (entry/create-entry "id" 1 100 "2022-02-07T11:59:25+00:00")))
  (is (thrown? java.lang.AssertionError (entry/create-entry "id" "task-id" "string" "2022-02-07T11:59:25+00:00")))
  (is (thrown? java.lang.AssertionError (entry/create-entry "id" "task-id" 100 100)))
  (is (thrown? java.lang.AssertionError (entry/create-entry "id" "wrong description" 100 "2022-02-07T11:59:25+00:00")))
  )
