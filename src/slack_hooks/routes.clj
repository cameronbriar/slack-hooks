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

(def-event "star" [{:keys [username userlink number text stars threshold url]}]
  (api/create-attachment username 
                         userlink
                         (str username "has starred #" number ": " text "\n"
                              "#" number " has " stars " of " threshold " Ninja stars needed to merge.")
                         (str "has starred _<" url "|#" number ": " text ">_" "\n"
                              "#" number " has " stars " of " threshold " Ninja stars needed to merge.")))

(def-event "unstar" [{:keys [username userlink number text stars threshold url]}]
  (api/create-attachment username 
                         userlink
                         (str username "has unstarred #" number ": " text "\n"
                              "#" number " has " stars " of " threshold " Ninja stars needed to merge.")
                         (str "has unstarred _<" url "|#" number ": " text ">_" "\n"
                              "#" number " has " stars " of " threshold " Ninja stars needed to merge.")))

(def-event "pull_request" [{:keys [username userlink number text threshold url]}]
  (api/create-attachment username 
                         userlink
                         (str username "has created pull request #" number ": " text "\n"
                              "#" number " needs " threshold " Ninja stars to merge.")
                         (str "has created pull request _<" url "|#" number ": " text ">_" "\n"
                              "#" number " needs " threshold " Ninja stars to merge.")))

(def-event "merge" [{:keys [username userlink number text url]}]
  (api/create-attachment username 
                         userlink
                         (str username "has merged pull request #" number ": " text "!")
                         (str "has merged pull request _<" url "|#" number ": " text ">_" "!")))

(defn home-page []
  "ReviewNinja + Slack = <3")

(defn handle [{:keys [params]}]
  (let [event   (get params :event)
        token   (get params :token)
        channel (get params :channel "#general")
        args {:username  (get-in params [:sender :login])
              :userlink  (get-in params [:sender :html_url])
              :number    (get-in params [:pull_request :number])
              :text      (get-in params [:pull_request :title])
              :stars     (get-in params [:pull_request :stars])
              :threshold (get-in params [:pull_request :threshold])
              :url       (get-in params [:url])}]
    (def attachment (fire event args))
    (if (not= attachment "Unsupported event")
      (api/send-message token channel attachment))
    (str attachment)))

(defroutes home-routes
  (GET "/" [] (home-page))
  (POST "/" request (handle request)))
