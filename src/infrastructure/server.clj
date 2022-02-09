(ns infrastructure.server
  (:require [ring.adapter.jetty :as jetty]
            [infrastructure.handler :as handler]
            [infrastructure.database.credential :as credential-imp]
            [domain.credential-repository :as credential-repository]
            [integrant.core :as ig]
            [aero.core :as aero]
            [clojure.java.io :refer [resource]]))

(defmethod aero/reader 'ig/ref
  [_ _ value]
  (ig/ref value))

(def config
  (aero/read-config (resource "config.edn")))

(defmethod ig/init-key :adapter/jetty [_ {:keys [handler] :as opts}]
  (jetty/run-jetty handler (-> opts (dissoc handler) (assoc :join? false))))

(defmethod ig/init-key :handler/run-app [_ {:keys [oauth2]}]
  (handler/app oauth2))

(defmethod ig/init-key :oauth2/google [_ credentials]
  credentials)

(defmethod ig/init-key :secrets [_ _]) ;TODO does this really have to be here?

(defn -main []
  (let [config config]
    (ig/load-namespaces config)
    (ig/init config)))


