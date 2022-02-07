(ns application.sync-entry
  (:require [domain.entry-repository :as entry-repository]
            [domain.credential-repository :as credential-repository]
            [domain.worklog-repository :as worklog-repository]))

(defn execute! [task-id user-id]
  (let [credential (credential-repository/fetch! user-id)]
    (->
     task-id
     (entry-repository/one! credential)
     (worklog-repository/save! credential))
    (entry-repository/add-tags! [task-id] credential)))
