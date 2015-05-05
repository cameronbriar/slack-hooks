(ns reviewninja-slack.db.core
    (:require [monger.core :as mg]
              [monger.collection :as mc]
              [monger.operators :refer :all]))

;; Tries to get the Mongo URI from the environment variable
;; MONGOHQ_URL, otherwise default it to localhost
(defonce db (let [uri (get (System/getenv) "MONGOLAB_URL" "mongodb://127.0.0.1/reviewninja-slack")
                  {:keys [conn db]} (mg/connect-via-uri uri)]
              db))

(defn create-uuid-pair [pair]
  (mc/insert db "pairs" pair))

(defn upsert-uuid-pair [uuid token]
  (mc/update db "pairs" {:uuid uuid}
             {$set {:token token}} {:upsert true}))

(defn get-uuid-pair [uuid]
  (mc/find-one-as-map db "users" {:uuid uuid}))
