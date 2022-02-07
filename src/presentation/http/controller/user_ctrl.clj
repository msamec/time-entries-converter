(ns presentation.http.controller.user-ctrl
  (:require [ring.util.response :refer [response redirect]]
            [presentation.templates.index :as index]
            [presentation.templates.login :as login]
            [application.fetch-entires :as fetch-entries]
            [application.sync-entry :as sync-entry]
            [application.update-credential :as update-credential]
            [application.fetch-credential :as fetch-credential]
            [application.sync-all-entries :as sync-all-entries]))

(defn login [_]
  (->
   (login/render)
   response))

(defn index [{{:keys [user-id]} :session}]
  (let [entries (fetch-entries/execute! user-id)
        credential (fetch-credential/execute! user-id)]
    (->
     entries
     (index/render credential)
     response)))

(defn update-credential [{{:keys [user-id]} :session :as request}] ;TODO validate input data
  (let [options (-> request :form-params :options)]
    (update-credential/execute! user-id options))
  (redirect "/")) ;TODO add some message

(defn sync-task [{{:keys [user-id]} :session :as request}]
  (let [task-id (get-in request [:path-params :task-id])]
    (sync-entry/execute! task-id user-id))
  (redirect "/")) ;TODO add some message

(defn sync-all-tasks [{{:keys [user-id]} :session}]
  (sync-all-entries/execute! user-id)
  (redirect "/"))

