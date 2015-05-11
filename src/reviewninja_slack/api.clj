(ns reviewninja-slack.api
  (:require [clj-http.client :as client]))

(defn send-message [token channel text]
  (client/post "https://slack.com/api/chat.postMessage" 
               {:form-params 
             	{:token token 
              	 :channel channel
              	 :text text 
              	 :username "ReviewNinja"
              	 :mrkdown true
                 :icon_url "https://raw.githubusercontent.com/reviewninja/review.ninja/master/src/client/assets/images/review-ninja-48.png"}}))
