(ns infrastructure.mailer.postal
  (:require [integrant.core :as ig]
            [domain.mailer :refer [Mailer -send!]]
            [presentation.templates.email.send-reminder :as send-reminder]
            [postal.core :as postal]))

(defn get-template [name body]
  (cond
    (= name "send-reminder") (send-reminder/render body)))

(defn send! [{:keys [from] :as config} {:keys [body template] :as message}]
  (println (get-template template body))
  (postal/send-message
   config
   (-> message
       (assoc :from from)
       (assoc :body [{:type "text/html"
                      :content (get-template template body)}]))))

(defmethod ig/init-key :infrastructure.mailer/postal [_ {:keys [config]}]
  (reify Mailer
    (-send! [this message] (send! config message))))
