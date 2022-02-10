(ns infrastructure.config
  (:require [integrant.core :as ig]
            [aero.core :as aero]
            [clojure.java.io :refer [resource]]))

(defmethod aero/reader 'ig/ref
  [_ _ value]
  (ig/ref value))

(defmethod ig/init-key :infrastructure.config/secrets [_ _]) ;TODO does this really have to be here?

(defmethod ig/init-key :infrastructure.config/configuration [_ _])

(def all
  (->
   "config.edn"
   resource
   aero/read-config))

(def fetch
  (->
   "config.edn"
   resource
   aero/read-config
   :infrastructure.config/configuration))
