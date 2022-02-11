(ns integration.helper
  (:require [integrant.core :as ig]
            [infrastructure.config :as config]
            [migratus.core :as migratus])
  (:import [com.opentable.db.postgres.embedded EmbeddedPostgres]))

(def state* (atom nil))

(def migrations-config (->
                        :test
                        (config/fetch)
                        :infrastructure.config/configuration
                        :migrations))

(defn get-system []
  (-> state* deref :system))

(defn start-pg! []
  (-> (EmbeddedPostgres/builder)
      (.setPort 54321)
      (.start)))

(defn halt-pg! [pg]
  (.close pg))

(defn start-system! []
  (let [config (config/fetch :test)]
    (ig/load-namespaces config)
    (ig/init config)))

(defn halt-system! [system]
  (ig/halt! system))

(defn with-system! [f]
  (try
    (let [pg (start-pg!)
          _ (migratus/migrate migrations-config)
          system (start-system!)]
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
      (migratus/reset migrations-config))))

