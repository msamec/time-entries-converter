(ns integration.helper
  (:require [integrant.core :as ig]
            [infrastructure.config :as config]
            [migratus.core :as migratus]
            [clojure.walk :refer [prewalk]])
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

(defn apply-mocks [config mock]
  (reduce-kv
   (fn [config' key mocked-key]
     (prewalk (fn [v] (if (= v key) mocked-key v)) config'))
   config
   mock))

(defn start-system! [mock]
  (let [config (config/fetch :test)
        prepped-config (apply-mocks config mock)]
    (ig/load-namespaces prepped-config)
    (ig/init prepped-config)))

(defn halt-system! [system]
  (ig/halt! system))

(defn with-system!
  ([f] (with-system! nil f))
  ([{:keys [mock]} f]
   (try
     (let [pg (start-pg!)
           _ (migratus/migrate migrations-config)
           system (start-system! mock)]
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
         (reset! state* nil))))))

(defn reset-db! [f]
  (try
    (f)
    (catch Exception e
      (do
        (.printStackTrace e)
        (throw e)))
    (finally
      (migratus/reset migrations-config))))

