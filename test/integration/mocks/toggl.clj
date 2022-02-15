(ns integration.mocks.toggl
  (:require [domain.entry-repository :refer [EntryRepository -all! -one! -add-tags!]]
            [integrant.core :as ig]
            [domain.entry :refer [create-entry]]))

(defn all! [{:keys [user-id]}]
  (cond
    (= user-id "single") (create-entry "id" "TSK | Desc" 100 "2022-02-07T11:59:25+00:00")
    (= user-id "multiple") [] ))

(defn one! [id credential]
  "one")

(defn add-tags! [ids credential]
  "add-tags")

(defmethod ig/init-key :integration.mocks/toggl [_ _]
  (reify EntryRepository
    (-all! [this credential] (all! credential))
    (-one! [this id credential] (one! id credential))
    (-add-tags! [this ids credential] (add-tags! ids credential))))
