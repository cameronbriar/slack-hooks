(ns reviewninja-slack.db
    (:require [monger.core :as mg]
              [monger.collection :as mc]
              [monger.operators :refer :all]))

;; Tries to get the Mongo URI from the environment variable
;; MONGOHQ_URL, otherwise default it to localhost

(defonce db (let [uri (get (System/getenv) "MONGOLAB_URL" "mongodb://127.0.0.1/rnslack")
                  {:keys [conn db]} (mg/connect-via-uri uri)]
              db))

(defn upsert-uuid-pair [uuid token]
  (mc/update db "pairs" {:uuid (int uuid)}
             {$set {:token token}} {:upsert true}))

(defn delete-uuid-pair [uuid]
  (mc/remove db "pairs" {:uuid uuid}))

(defn get-uuid-pair [uuid]
  (mc/find-one-as-map db "pairs" {:uuid uuid}))
