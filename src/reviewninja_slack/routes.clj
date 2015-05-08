(ns reviewninja-slack.routes
  (:require [reviewninja-slack.api :as api]
            [compojure.core :refer [defroutes GET POST]]
            [clojure.java.io :as io]))

;; message creating functions
(defn- star [username pr-number pr-text pr-threshold pr-stars]
  (str username " has starred #" pr-number ": " pr-text "\n"
       "#" pr-number " has " pr-stars " of " pr-threshold " needed stars."))

(defn- pr-create [username pr-number pr-text pr-threshold]
  (str username " has created pull request #" pr-number ": " pr-text "\n"
       "#" pr-number " needs " pr-threshold " stars to merge."))

(defn- pr-merge [username pr-number pr-text]
  (str username " has merged #" pr-number ": " pr-text "!"))

;; function to determine what message to use
(defn- slack-message [event args]
  (case event
    "star" (star (args :username) (args :pr-number) (args :pr-stars) (args :pr-text) (args :pr-threshold))
    "pr-create" (pr-create (args :username) (args :pr-number) (args :pr-text) (args :pr-threshold))
    "pr-merge" (pr-merge (args :username) (args :pr-number) (args :pr-text))
    "test" "lol"
    ""))

(defn- slack-message-test [event] event)

(defn home-page []
  "hello")

(defn receive [{:keys [params]}]
  (let [event (get params :event)
        repo-uuid (get params :uuid)
        token (get params :token)
        channel (get params :channel "#reviewninja")
        args {:username (get params :username)
              :pr-number (get params :pr-number)
              :pr-text (get params :pr-text)
              :pr-threshold (get params :pr-threshold)
              :pr-stars (get params :pr-stars)}]
    (println params)
    (api/send-message token channel (slack-message-test event))
    (slack-message-test event)))

(defroutes home-routes
  (GET "/" [] (home-page))
  (POST "/" request (receive request)))
