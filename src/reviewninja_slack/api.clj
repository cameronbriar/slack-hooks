(ns reviewninja-slack.api
  (:require [clj-http.client :as client]))

(def token-test (System/getenv "TEST_TOKEN"))

(def ^:private slack-chat-url "https://slack.com/api/chat.postMessage")
(def ^:private icon-url "https://raw.githubusercontent.com/reviewninja/ux.review.ninja/master/Artwork/logos/slack/rnslack.png")

(defn send-message [token channel text bot-name]
  (client/post slack-chat-url {:form-params 
                             	{:token token 
                              	 :channel channel
                              	 :text text 
                              	 :username bot-name
                              	 :mrkdown true
                                 :icon_url icon-url}}))