(ns domain.worklog-repository)

(def save! nil)

(defprotocol WorklogRepository
  (-save! [this entry credential]))

(defn set-implementation! [impl]
  (def save! (partial -save! impl)))
