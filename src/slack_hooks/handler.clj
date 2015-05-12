(ns slack-hooks.handler
  (:require [compojure.core :refer [defroutes routes]]
            [slack-hooks.routes :refer [home-routes]]
            [slack-hooks.middleware
             :refer [development-middleware production-middleware]]
            [compojure.route :as route]))

(defroutes base-routes
           (route/resources "/")
           (route/not-found "Not Found"))

(def app
  (-> (routes
        home-routes
        base-routes)
      development-middleware
      production-middleware))
