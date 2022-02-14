(ns presentation.templates.email.send-reminder
  (:require [hiccup.page :refer [html5]]))

(defn render [body]
  (html5
   [:main.container.mx-w-6xl.mx-auto.py-4
    [:div.flex.flex-col
     [:div.overflow-x-auto.sm:-mx-6.lg:-mx-8
      [:p body]]]]))
