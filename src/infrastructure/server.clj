(ns infrastructure.server
  (:require [ring.adapter.jetty :as jetty]
            [infrastructure.handler :as handler]
            [infrastructure.http.tempo :as worklog-imp]
            [infrastructure.database.credential :as credential-imp]
            [domain.credential-repository :as credential-repository]
            [domain.worklog-repository :as worklog-repository]
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

(defmethod ig/init-key :di/worklog [_ _]
  (worklog-repository/set-implementation! (worklog-imp/new-worklog-repository)))

(defmethod ig/init-key :di/credential [_ {:keys [db-uri]}]
  (credential-repository/set-implementation! (credential-imp/new-credential-repository db-uri)))

(defn -main []
  (let [config config]
    (ig/load-namespaces config)
    (ig/init config)))


