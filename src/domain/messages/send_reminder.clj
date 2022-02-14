(ns domain.messages.send-reminder
  (:require [domain.message :refer [Message to-map]]))

(defrecord SendReminderMessage [to]
  Message
  (to-map [this] {:to to
                  :subject "Reminder"
                  :body "Sync your hours"
                  :template "send-reminder"}))

