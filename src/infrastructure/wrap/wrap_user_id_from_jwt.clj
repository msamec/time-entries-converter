(ns infrastructure.wrap.wrap-user-id-from-jwt
  (:require [clj-jwt.core :refer [str->jwt]]))

(defn- user-from-jwt [request]
  (->
   (get-in request [:session :ring.middleware.oauth2/access-tokens :google :id-token])
   str->jwt
   :claims
   :sub))

(defn wrap-user-id-from-jwt [handler]
  (fn [request]
    (let [user-id (user-from-jwt request)]
      (->
       request
       (assoc-in [:session :user-id] user-id)
       handler))))


