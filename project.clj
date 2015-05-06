(defproject reviewninja-slack "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [selmer "0.8.2"]
                 [com.taoensso/timbre "3.4.0"]
                 [markdown-clj "0.9.66"]
                 [environ "1.0.0"]
                 [clj-http "1.1.2"]
                 [im.chit/cronj "1.4.3"]
                 [compojure "1.3.3"]
                 [ring/ring-defaults "0.1.4"]
                 [ring/ring-session-timeout "0.1.0"]
                 [ring-middleware-format "0.5.0"]
                 [bouncer "0.3.2"]
                 [prone "0.8.1"]
                 [org.clojure/tools.nrepl "0.2.10"]
                 [ring-server "0.4.0"]
                 [com.novemberain/monger "2.0.1"]]

  :min-lein-version "2.0.0"
  :uberjar-name "reviewninja-slack.jar"
  :jvm-opts ["-server"]

;;enable to start the nREPL server when the application launches
;:env {:repl-port 7001}

  :main reviewninja-slack.core

  :plugins [[lein-ring "0.9.1"]
            [lein-environ "1.0.0"]
            [lein-ancient "0.6.5"]
            ]
  

  
  :ring {:handler reviewninja-slack.handler/app
         :init    reviewninja-slack.handler/init
         :destroy reviewninja-slack.handler/destroy
         :uberwar-name "reviewninja-slack.war"}

  
  
  
  :profiles
  {:uberjar {:omit-source true
             :env {:production true}
             
             :aot :all}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.3.2"]
                        [pjstadig/humane-test-output "0.7.0"]
                        ]
         :source-paths ["env/dev/clj"]
         
         
         
         :repl-options {:init-ns reviewninja-slack.core}
         :injections [(require 'pjstadig.humane-test-output)
                      (pjstadig.humane-test-output/activate!)]
         :env {:dev true}}})
