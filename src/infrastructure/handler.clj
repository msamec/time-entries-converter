(ns infrastructure.handler
  (:require [reitit.ring :as ring]
            [presentation.http.controller.user-ctrl :as user-ctrl]
            [integrant.core :as ig]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.oauth2 :refer [wrap-oauth2]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [infrastructure.wrap.wrap-user-id-from-jwt :refer [wrap-user-id-from-jwt]]
            [infrastructure.wrap.wrap-auth :refer [wrap-auth]]
            [infrastructure.wrap.wrap-form-params :refer [wrap-form-params]]))

(defmethod ig/init-key :infrastructure.handler/app [_ {:keys [oauth2]}]
  (->
   [["/login" {:get {:handler user-ctrl/login}}]
    ["/" {:get {:handler user-ctrl/index
                :middleware [wrap-auth wrap-user-id-from-jwt]}}]
    ["/update-credential" {:post {:handler user-ctrl/update-credential
                                  :middleware [wrap-auth wrap-user-id-from-jwt wrap-form-params]}}]
    ["/sync-all-tasks" {:post {:handler user-ctrl/sync-all-tasks
                               :middleware [wrap-auth wrap-user-id-from-jwt]}}]
    ["/sync-task/:task-id" {:post {:handler user-ctrl/sync-task
                                   :middleware [wrap-auth wrap-user-id-from-jwt]}}]]
   ring/router
   ring/ring-handler
   (wrap-oauth2 oauth2)
   (wrap-reload)
   (wrap-params)
   (wrap-content-type {:mime-types {nil "text/html"}})
   (wrap-session {:cookie-attrs {:same-site :lax}})
   ))
