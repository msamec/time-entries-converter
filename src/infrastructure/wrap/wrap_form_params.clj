(ns infrastructure.wrap.wrap-form-params
  (:require [ring.util.codec :refer [assoc-conj]]
            [clojure.walk :refer [keywordize-keys]]))

(defn- parse-nested-keys
  [param-name]
  (let [[_ k ks] (re-matches #"(?s)(.*?)((?:\[.*?\])*)" (name param-name))
        keys     (if ks (map second (re-seq #"\[(.*?)\]" ks)))]
    (cons k keys)))

(defn- assoc-vec [m k v]
  (let [m (if (contains? m k) m (assoc m k []))]
    (assoc-conj m k v)))

(defn- assoc-nested
  [m [k & ks] v]
  (if k
    (if ks
      (let [[j & js] ks]
        (if (= j "")
          (assoc-vec m k (assoc-nested {} js v))
          (assoc m k (assoc-nested (get m k {}) ks v))))
      (if (map? m)
        (assoc-conj m k v)
        {k v}))
    v))

(defn- param-pairs
  [params]
  (mapcat
   (fn [[name value]]
     (if (and (sequential? value) (not (coll? (first value))))
       (for [v value] [name v])
       [[name value]]))
   params))

(defn- nest-params
  [params parse]
  (reduce
   (fn [m [k v]]
     (assoc-nested m (parse k) v))
   {}
   (param-pairs params)))

(defn- form-params
  [form-params]
  (if (not (nil? form-params))
    (->
     form-params
     (nest-params parse-nested-keys)
     keywordize-keys)
    form-params))

(defn wrap-form-params [handler]
  (fn [request]
    (let [form-params (form-params (request :form-params))]
      (->
       request
       (assoc :form-params form-params)
       handler))))
