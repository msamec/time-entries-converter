(ns infrastructure.jobs.send-reminder-email
  (:require [integrant.core :as ig]
            [domain.mailer :as mailer]
            [domain.message :refer [to-map]]
            [domain.messages.send-reminder :refer [->SendReminderMessage]]))

(defmethod ig/init-key :infrastructure.jobs/send-reminder-email [_ _]
  (fn [to]
    (mailer/send! (to-map (->SendReminderMessage to)))))
