(ns application.sync-all-entries
  (:require [domain.entry-repository :as entry-repository]
            [domain.credential-repository :as credential-repository]
            [domain.worklog-repository :as worklog-repository]))

(defn execute! [user-id]
  (let [credential (credential-repository/fetch! user-id)
        entries (entry-repository/all! credential)
        entry-ids (map (fn [{:keys [id]}] id) entries)]
    (doall (map #(worklog-repository/save! % credential) entries))
    (entry-repository/add-tags! entry-ids credential)))
