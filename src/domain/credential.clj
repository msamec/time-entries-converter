(ns domain.credential)

(defrecord Credential [id user-id options])

(defn create-credential
  [id user-id options]
  {:pre [(int? id)
         (string? user-id)]}
  (->Credential id user-id options))
