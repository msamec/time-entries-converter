(ns infrastructure.config
  (:require [integrant.core :as ig]
            [aero.core :as aero]
            [clojure.java.io :refer [resource]]))

(defmethod aero/reader 'ig/ref
  [_ _ value]
  (ig/ref value))

(defmethod ig/init-key :infrastructure.config/configuration [_ _])

(defn fetch
  ([] (fetch :default))
  ([profile] (->
              "config.edn"
              resource
              (aero/read-config {:profile profile}))))
