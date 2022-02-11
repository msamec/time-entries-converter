(ns domain.message)

(defprotocol Message
  (to-map [this]))
