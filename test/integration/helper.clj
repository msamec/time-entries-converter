(ns integration.helper
  (:require [integrant.core :as ig]
            [infrastructure.config :as config]
            [migratus.core :as migratus])
  (:import [com.opentable.db.postgres.embedded EmbeddedPostgres]))

(def state* (atom nil))

(defn get-system []
  (-> state* deref :system))

(defn start-pg! []
  (-> (EmbeddedPostgres/builder)
      (.setServerConfig "fsync" "off")
      (.setServerConfig "full_page_writes" "off")
      (.setPort 59432)
      (.start)))

(defn halt-pg! [pg]
  (.close pg))

(defn start-system! []
  (let [config config/all]
    (ig/load-namespaces config)
    (ig/init config)))

(defn halt-system! [system]
  (ig/halt! system))

(defn with-system! [f]
  (try
    (let [pg (start-pg!)
          system (start-system!)]
      (migratus/up (-> config/fetch :migrations))
      (reset! state* {:pg pg :system system})
      (f))
    (catch Exception e
      (do
        (.printStackTrace e)
        (throw e)))
    (finally
      (when-let [state @state*]
        (halt-system! (:system state))
        (halt-pg! (:pg state))
        (reset! state* nil)))))

(defn reset-db! [f]
  (try
    (f)
    (catch Exception e
      (do
        (.printStackTrace e)
        (throw e)))
    (finally
      (migratus/down (-> config/fetch :migrations))
      (migratus/up (-> config/fetch :migrations)))))
