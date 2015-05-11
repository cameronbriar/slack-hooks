(ns reviewninja-slack.routes
  (:require [reviewninja-slack.api :as api]
            [compojure.core :refer [defroutes GET POST]]
            [clojure.java.io :as io]))

;; message creating functions
(defn- star [{:keys [username pr-number pr-text pr-stars pr-threshold]}]
  (str username " has starred #" pr-number ": " pr-text "\n"
       "#" pr-number " has " pr-stars " of " pr-threshold " needed stars."))

(defn- unstar [{:keys [username pr-number pr-text pr-stars pr-threshold]}]
  (str username " has unstarred #" pr-number ": " pr-text "\n"
       "#" pr-number " has " pr-stars " of " pr-threshold " needed stars."))

(defn- pr-create [{:keys [username pr-number pr-text pr-threshold]}]
  (str username " has created pull request #" pr-number ": " pr-text "\n"
       "#" pr-number " needs " pr-threshold " stars to merge."))

(defn- pr-merge [{:keys [username pr-number pr-text]}]
  (str username " has merged #" pr-number ": " pr-text "!"))

;; function to determine what message to use
(defn- slack-message [event args]
  (case event
    "star" (star args)
    "unstar" (unstar args)
    "pr-create" (pr-create args)
    "pr-merge" (pr-merge args)
    ""))

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
    (api/send-message token channel (slack-message event args))
    (slack-message event args)))

(defroutes home-routes
  (GET "/" [] (home-page))
  (POST "/" request (receive request)))
