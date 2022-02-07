(ns application.fetch-entires
  (:require [domain.entry-repository :as entry-repository]
            [domain.credential-repository :as credential-repository]))

(defn execute! [user-id]
  (let [credential (credential-repository/fetch! user-id)]
    (entry-repository/all! credential)))
