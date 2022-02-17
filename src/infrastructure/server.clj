(ns infrastructure.server
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [integrant.core :as ig]
            [infrastructure.config :as config]))

(defmethod ig/init-key :infrastructure.server/jetty [_ {:keys [handler] :as opts}]
  (jetty/run-jetty handler (-> opts (dissoc handler) (assoc :join? false))))

(defmethod ig/halt-key! :infrastructure.server/jetty [_ server]
  (.stop server))

(defn -main []
  (let [config (config/fetch)]
    (ig/load-namespaces config)
    (ig/init config)))

