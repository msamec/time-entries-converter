(ns application.send-email
  (:require [domain.mailer :as mailer]))

(defn execute! [to subject body]
  (mailer/send! to subject body))
