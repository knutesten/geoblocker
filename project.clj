(defproject geoblocker "0.1.0-SNAPSHOT"
  :min-lein-version "2.0.0"
  :uberjar-name "geoblocker.jar"
  :dependencies [[org.clojure/data.csv "1.0.0"]
                 [compojure "1.6.1"]
                 [hiccup "1.0.5"]
                 [org.clojure/clojure "1.10.0"]
                 [ring/ring-defaults "0.3.2"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler geoblocker.handler/app
         :nrepl   {:start? true}}
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring/ring-mock "0.3.2"]]}})
