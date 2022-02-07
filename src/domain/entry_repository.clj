(ns domain.entry-repository)

(def all! nil)
(def one! nil)
(def add-tags! nil)

(defprotocol EntryRepository
  (-all! [this options])
  (-one! [this id options])
  (-add-tags! [this ids options]))

(defn set-implementation! [impl]
  (def all! (partial -all! impl))
  (def one! (partial -one! impl))
  (def add-tags! (partial -add-tags! impl)))
