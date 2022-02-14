(ns application.fetch-credential
  (:require [domain.credential-repository :as credential-repository]
            [domain.mailer :as mailer]
            [domain.message :refer [to-map]]
            [domain.messages.send-reminder :refer [->SendReminderMessage]]))

(defn execute! [user-id]
  (credential-repository/fetch! user-id))
