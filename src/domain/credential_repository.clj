(ns domain.credential-repository)

(def save! nil)
(def fetch! nil)

(defprotocol CredentialRepository
  (-save! [this user-id options])
  (-fetch! [this user-id]))

(defn set-implementation! [impl]
  (def save! (partial -save! impl))
  (def fetch! (partial -fetch! impl)))
