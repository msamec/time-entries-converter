(ns domain.mailer
  (:require [integrant.core :as ig]))

(def send! nil)

(defprotocol Mailer
  (-send! [this message]))

(defmethod ig/init-key :domain/mailer [_ {:keys [impl]}]
  (def send! (partial -send! impl)))
