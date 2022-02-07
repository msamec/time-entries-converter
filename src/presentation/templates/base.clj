(ns presentation.templates.base
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [hiccup.element :refer [link-to]]))

(defn base
  [content]
  (html5
   [:head
    [:meta {:charset "UTF-8"}]
    [:meta {:content "IE=edge", :http-equiv "X-UA-Compatible"}]
    [:meta
     {:content "width=device-width, initial-scale=1.0",
      :name "viewport"}]
    [:title "Toggl -> Jira"]
    (include-js "https://cdn.tailwindcss.com")
    [:script {:src "https://unpkg.com/alpinejs@3.x.x/dist/cdn.min.js" :defer true}]]
   [:body.relative.antialiased.bg-gray-100 content]))
