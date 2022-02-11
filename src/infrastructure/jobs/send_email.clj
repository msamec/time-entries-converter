(ns infrastructure.jobs.send-email
  (:require [integrant.core :as ig]))

(defmethod ig/init-key :infrastructure.jobs/send-email [_ _]
  (fn [payload]
    (clojure.pprint/pprint payload)))
