(defproject slack-hooks "0.1.0"

  :description "Slack receivers for ReviewNinja"
  :url "https://github.com/reviewninja/slack-hooks"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.6"]
                 [selmer "0.8.2"]
                 [com.taoensso/timbre "3.4.0"]
                 [clj-http "1.1.2"]
                 [compojure "1.3.3"]
                 [environ "1.0.0"]
                 [ring/ring-defaults "0.1.4"]
                 [ring/ring-session-timeout "0.1.0"]
                 [ring-middleware-format "0.5.0"]
                 [prone "0.8.1"]
                 [org.clojure/tools.nrepl "0.2.10"]
                 [ring-server "0.4.0"]
                 [ring-basic-authentication "1.0.5"]]

  :min-lein-version "2.0.0"
  :uberjar-name "slack-hooks.jar"
  :jvm-opts ["-server"]

;;enable to start the nREPL server when the application launches
; :env {:repl-port 7001}

  :main slack-hooks.core

  :plugins [[lein-ring "0.9.1"]
            [lein-environ "1.0.0"]
            [lein-ancient "0.6.5"]]

  :ring {:handler slack-hooks.handler/app
         :init    slack-hooks.handler/init
         :destroy slack-hooks.handler/destroy
         :uberwar-name "slack-hooks.war"}

  :profiles
  {:uberjar {:omit-source true
             :env {:production true}
             
             :aot :all}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.3.2"]
                        [pjstadig/humane-test-output "0.7.0"]
                        ]
         :source-paths ["env/dev/clj"]

         :repl-options {:init-ns slack-hooks.core}
         :injections [(require 'pjstadig.humane-test-output)
                      (pjstadig.humane-test-output/activate!)]
         :env {:dev true}}})
