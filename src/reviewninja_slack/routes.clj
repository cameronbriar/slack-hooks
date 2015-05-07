(ns reviewninja-slack.routes
  (:require [reviewninja-slack.layout :as layout]
            [reviewninja-slack.db.core :as db]
            [reviewninja-slack.api :as api]
            [compojure.core :refer [defroutes GET POST]]
            [clojure.java.io :as io]))

;; message creating functions
(defn- star [username pr-number pr-stars pr-text pr-threshold]
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
    ""))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn receive [request]
  (let [event (get-in request [:params :event])
        repo-uuid (get-in request [:params :uuid])
        slack-token (get-in request [:params :slack-token])]
    ;; (slack-methods)
    (db/upsert-uuid-pair repo-uuid slack-token)))

(defroutes home-routes
  (GET "/" [] (home-page))
  (POST "/receive" [] receive))
