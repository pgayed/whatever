(ns clojure-counter-app.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as response] ; Updated
            [ring.middleware.params :as params] ; Updated
            [rum.core :as rum]) ; Added Rum
  (:gen-class))

(defonce counter (atom 0))

(rum/defc counter-page [current-count]
  [:html
   [:head
    [:title "Clojure Rum Counter"]] ; page-styles component removed
   [:body
    [:h1 "Clojure Counter App (Rum)"]
    [:div (str current-count)] ; Removed class attribute
    [:form {:action "/increment" :method "post"}
     [:button {:type "submit"} "Click me to Increment!"]]
    ]])

(defn handler [request]
  (when (and (= (:uri request) "/increment")
             (= (:request-method request) :post))
    (swap! counter inc))
  
  (let [current-count @counter]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (rum/render-html (counter-page current-count))})) ; Use Rum to render HTML

(def app
  (params/wrap-params handler) ; Updated middleware
      ;; Note: The original resource/content-type/not-modified middlewares are removed
      ;; as per the example in the prompt implicitly by not being in the new ns :require
      ;; and not being in the new app pipeline. If they were still needed,
      ;; the :require would need to include them and they'd be in the pipeline.
      ;; For this refactoring, assuming they are replaced by params or no longer needed.
      ))

(defn -main [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (jetty/run-jetty app {:port port :join? false})))
