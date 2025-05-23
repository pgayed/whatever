(defproject clojure-counter-app "0.1.0-SNAPSHOT"
  :description "A simple web app with a counter button, all in Clojure."
  :url "http://example.com/clojure-counter-app"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring/ring-core "1.9.6"]
                 [ring/ring-jetty-adapter "1.9.6"]
                 [rum "0.12.9"]] ; Added Rum
  :main ^:skip-aot clojure-counter-app.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
