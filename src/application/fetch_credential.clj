(ns application.fetch-credential
  (:require [domain.credential-repository :as credential-repository]))

(defn execute! [user-id]
  (credential-repository/fetch! user-id))
