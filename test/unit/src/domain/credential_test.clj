(ns unit.src.domain.credential-test
  (:require [clojure.test :refer [deftest is testing]]
            [domain.credential :as credential]))

(deftest create-credential
  (testing "if record can be created given valid inputs"
    (let [credential (credential/create-credential 1 "user-id" {:key "value"})]
      (is (= 1 (-> credential :id)))
      (is (= "user-id" (-> credential :user-id)))
      (is (= "value" (-> credential :options :key)))))
  (testing "if record creating throws AssertionError exception give invalid inputs"
    (is (thrown? java.lang.AssertionError (credential/create-credential "string" "user-id" {:key "value"})))
    (is (thrown? java.lang.AssertionError (credential/create-credential 1 2 {:key "value"})))))


