(ns integration.src.application.credential-test
  (:require [clojure.test :refer [use-fixtures]]
            [state-flow.assertions.matcher-combinators :refer [match?]]
            [state-flow.api :as flow :refer [flow]]
            [state-flow.cljtest :refer [defflow]]
            [application.update-credential :as update-credential]
            [application.fetch-credential :as fetch-credential]
            [integration.helper :refer [with-system! reset-db! get-system]]))

(use-fixtures :once with-system!)

(use-fixtures :each reset-db!)

(def user-id "user-id")
(def options {:key "value"})

(defn init []
  (let [system (get-system)]
    {:system system}))

(defn create-credential [user-id options]
  (flow "Create credential"
        (flow/swap-state
         (fn [state]
           (let [credential (update-credential/execute! user-id options)]
             (assoc state :credential-created credential))))
        (flow/get-state :credential-created)))

(defn fetch-credential [user-id]
  (flow "Fetch credential"
        (flow/swap-state
         (fn [state]
           (let [credential (fetch-credential/execute! user-id)]
             (assoc state :credential-by-id credential))))
        (flow/get-state :credential-by-id)))

(defflow credential-create
  {:init init}
  [credential (create-credential user-id options)]
  (match? (:user-id credential) user-id)
  (match? (:options credential) options))

(defflow credential-fetch
  {:init init}
  (create-credential user-id options)
  [credential (fetch-credential user-id)]
  (match? (:user-id credential) user-id)
  (match? (:options credential) options))

(defflow fetch-non-existan-credential
  {:init init}
  [credential (fetch-credential "non-exstsn")]
  (match? credential nil))
