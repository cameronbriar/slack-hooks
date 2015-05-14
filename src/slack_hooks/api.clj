(ns slack-hooks.api
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json]))

(defn create-attachment [title title_link fallback text]
    {:color "#3e9a94"
     :title title
     :title_link title_link
     :fallback fallback
     :text text
     :mrkdwn_in ["text"]})

(defn send-message [token channel attachment]
  (client/post "https://slack.com/api/chat.postMessage" 
               {:form-params 
                {:token token 
                 :channel channel
                 :username "ReviewNinja"
                 :attachments (json/write-str [attachment])
                 :icon_url "https://raw.githubusercontent.com/reviewninja/review.ninja/master/src/client/assets/images/review-ninja-48.png"}}))
