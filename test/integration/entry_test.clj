(ns integration.entry-test
  (:require [clojure.test :refer [use-fixtures]]
            [integration.helper :refer [with-system! reset-db! get-system]]
            [state-flow.api :as flow :refer [flow]]
            [state-flow.cljtest :refer [defflow]]
            [application.fetch-entires :as fetch-entries]
            [state-flow.assertions.matcher-combinators :refer [match?]]
            [integration.credential-test :as credential]))

(use-fixtures :once (partial with-system! {:mock {:infrastructure.http/toggl :integration.mocks/toggl}}))

(use-fixtures :each reset-db!)

(defn init []
  (let [system (get-system)]
    {:system system}))

(defn fetch [user-id]
  (flow "Fetch entries"
        (flow/swap-state
         (fn [state]
           (let [entries (fetch-entries/execute! user-id)]
             (assoc state :entries entries))))
        (flow/get-state :entries)))

(defflow fetch-multiple-entries
  {:init init}
  (credential/create-credential "single" {})
  [entries (fetch "single")]
  (match? {:id "id"
           :task-id "TSK"
           :description "Desc"
           :start-date "2022-02-07"
           :start-time "11:59:25"} entries)
  )

