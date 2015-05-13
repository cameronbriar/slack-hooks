(ns slack-hooks.routes
  (:require [slack-hooks.api :as api]
            [compojure.core :refer [defroutes GET POST]]
            [clojure.java.io :as io]))

(def EVENTS {})

(defn def-event [name fn]
  (def EVENTS (conj EVENTS {name fn})))

(def-event "star" #(println %1))

(defn home-page []
  "ReviewNinja + Slack = <3")

(defn receive [{:keys [params]}]
  (let [event (get params :event)
        token (get params :token)
        channel (get params :channel "#general")
        args {:username (get-in params [:sender :login])
              :pr-number (get-in params [:pull_request :number])
              :pr-text (get-in params [:pull_request :title])
              :pr-stars (get-in params [:pull_request :stars])
              :pr-threshold (get-in params [:pull_request :threshold])}]
    (println params)
    (println event token channel args)
    (api/send-message token channel ((get EVENTS event) args))
    ((get EVENTS event) 1 2)))

(defroutes home-routes
  (GET "/" [] (home-page))
  (POST "/" request (receive request)))
