(ns reviewninja-slack.routes.home
  (:require [reviewninja-slack.layout :as layout]
            [reviewninja-slack.db.core :as db]
            [reviewninja-slack.api :as api]
            [compojure.core :refer [defroutes GET POST]]
            [clojure.java.io :as io]))

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
