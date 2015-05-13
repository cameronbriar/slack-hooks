(ns slack-hooks.routes
  (:require [slack-hooks.api :as api]
            [compojure.core :refer [defroutes GET POST]]
            [clojure.java.io :as io]))

(def EVENTS {})

(defn fire [event args]
  (if (contains? EVENTS event)
    ((get EVENTS event) args)
    (str "Unsupported event")))

(defn def-event-fn [name fn]
  (def EVENTS (conj EVENTS {name fn})))

(defmacro def-event [name args & fn]
  `(def-event-fn ~name (fn ~args ~@fn)))

(def-event "star" [{:keys [username number text stars threshold]}]
  (str username " has starred #" number ": " text "\n"
       "#" number " has " stars " of " threshold " needed stars."))

(def-event "unstar" [{:keys [username number text stars threshold]}]
  (str username " has unstarred #" number ": " text "\n"
       "#" number " has " stars " of " threshold " needed stars."))

(def-event "pr-create" [{:keys [username number text threshold]}]
  (str username " has created pull request #" number ": " text "\n"
       "#" number " needs " threshold " stars to merge."))

(def-event "pr-merge" [{:keys [username number text]}]
  (str username " has merged #" number ": " text "!"))

(defn home-page []
  "ReviewNinja + Slack = <3")

(defn handle [{:keys [params]}]
  (let [event   (get params :event)
        token   (get params :token)
        channel (get params :channel "#general")
        args {:username  (get-in params [:sender :login])
              :number    (get-in params [:pull_request :number])
              :text      (get-in params [:pull_request :title])
              :stars     (get-in params [:pull_request :stars])
              :threshold (get-in params [:pull_request :threshold])}]
    (def text (fire event args))
    (if (not= text "Unsupported event")
      (api/send-message token channel text))
    (str text)))

(defroutes home-routes
  (GET "/" [] (home-page))
  (POST "/" request (handle request)))
