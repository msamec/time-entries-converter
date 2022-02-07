(ns application.update-credential
  (:require [domain.credential-repository :as credential-repository]))

(defn execute! [user-id options]
  (credential-repository/save! user-id options))
