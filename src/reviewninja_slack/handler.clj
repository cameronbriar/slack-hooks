(ns reviewninja-slack.handler
  (:require [compojure.core :refer [defroutes routes]]
            [reviewninja-slack.routes :refer [home-routes]]
            [reviewninja-slack.middleware
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
