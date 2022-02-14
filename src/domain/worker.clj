(ns domain.worker
  (:require [integrant.core :as ig]))

(def enqueue! nil)

(defprotocol Worker
  (-enqueue! [this job-type payload]))

(defmethod ig/init-key :domain/worker [_ {:keys [impl]}]
  (def enqueue! (partial -enqueue! impl)))

(comment
  (enqueue! :send-reminder-email "to@email.com"))
