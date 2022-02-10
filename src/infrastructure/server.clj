(ns infrastructure.server
  (:require [ring.adapter.jetty :as jetty]
            [integrant.core :as ig]
            [aero.core :as aero]
            [clojure.java.io :refer [resource]]))

(defmethod aero/reader 'ig/ref
  [_ _ value]
  (ig/ref value))

(def config
  (aero/read-config (resource "config.edn")))

(defmethod ig/init-key :infrastructure.server/jetty [_ {:keys [handler] :as opts}]
  (jetty/run-jetty handler (-> opts (dissoc handler) (assoc :join? false))))

(defmethod ig/init-key :secrets [_ _]) ;TODO does this really have to be here?

(defn -main []
  (let [config config]
    (ig/load-namespaces config)
    (ig/init config)))


