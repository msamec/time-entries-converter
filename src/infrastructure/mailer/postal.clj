(ns infrastructure.mailer.postal
  (:require [integrant.core :as ig]
            [domain.mailer :refer [Mailer -send!]]
            [postal.core :as postal]))

(defn send! [{:keys [from] :as config} message]
  (postal/send-message config (assoc message :from from)))

(defmethod ig/init-key :infrastructure.mailer/postal [_ {:keys [config]}]
  (reify Mailer
    (-send! [this message] (send! config message))))
