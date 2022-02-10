(ns infrastructure.server
  (:require [ring.adapter.jetty :as jetty]
            [integrant.core :as ig]
            [infrastructure.config :as config]))

(defmethod ig/init-key :infrastructure.server/jetty [_ {:keys [handler] :as opts}]
  (jetty/run-jetty handler (-> opts (dissoc handler) (assoc :join? false))))

(defn -main []
  (let [config config/all]
    (ig/load-namespaces config)
    (ig/init config)))
