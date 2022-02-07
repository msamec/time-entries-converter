(ns infrastructure.wrap.wrap-auth
  (:require [ring.util.response :refer [redirect]]))

(defn wrap-auth [handler]
  (fn [request]
    (if (get-in request [:session :ring.middleware.oauth2/access-tokens])
      (handler request)
      (redirect "/login"))))
