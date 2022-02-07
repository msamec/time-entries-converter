(ns infrastructure.database.credential
  (:require [com.verybigthings.penkala.next-jdbc :refer [get-env insert! select-one!]]
            [com.verybigthings.penkala.relation :as r]
            [domain.credential :refer [create-credential]]
            [domain.credential-repository :refer [CredentialRepository -save! -fetch!]]))

(def credentials-spec {:name "credentials"
                       :columns ["id" "user_id" "options"]
                       :pk ["id"]})
(def credentials-rel (r/spec->relation credentials-spec))
(defn credentials-ins [db-credential] (r/->insertable (:credentials db-credential)))

(defn fetch! [db-credential user-id]
  (let [credential (select-one! db-credential (-> credentials-rel (r/where [:= user-id :user-id])))]
    (if (some? credential)
      (let [{:credentials/keys [id user-id options]} credential]
        (create-credential id user-id options)))))

(defn save! [db-credential user-id options]
  (let [insert-data {:user-id user-id
                     :options options}]
    (insert! db-credential (-> (credentials-ins db-credential) (r/on-conflict-do-update [:user-id] insert-data)) insert-data)))

(defn new-credential-repository [db-uri]
  (let [db-credential (get-env db-uri)]
    (reify CredentialRepository
      (-save! [this user-id options] (save! db-credential user-id options))
      (-fetch! [this user-id] (fetch! db-credential user-id)))))
