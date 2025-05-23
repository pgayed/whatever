(ns clojure-counter-app.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as response] ; Updated
            [ring.middleware.params :as params] ; Updated
            [rum.core :as rum]) ; Added Rum
  (:gen-class))

(defonce counter (atom 0))

(rum/defc page-styles []
  [:style
   {:type "text/css"}
   "body { font-family: sans-serif; display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100vh; margin: 0; background-color: #f0f0f0; text-align: center; }"
   "h1 { color: #333; }"
   ".counter-value { font-size: 3em; color: #007bff; margin: 20px 0; }"
   "form { margin-top: 10px; }"
   "button { padding: 10px 20px; font-size: 1em; color: white; background-color: #007bff; border: none; border-radius: 5px; cursor: pointer; }"
   "button:hover { background-color: #0056b3; }"
   "button.reset { background-color: #6c757d; }"
   "button.reset:hover { background-color: #545b62; }"])

(rum/defc counter-page < rum/reactive [current-count]
  [:html
   [:head
    [:title "Clojure Rum Counter"]
    (page-styles)]
   [:body
    [:h1 "Clojure Counter App (Rum)"]
    [:div.counter-value (str current-count)] ; Rum components expect strings or other components
    [:form {:action "/increment" :method "post"}
     [:button {:type "submit"} "Click me to Increment!"]]
    [:form {:action "/" :method "get"}
     [:button.reset {:type "submit"} "Show Current Count"]]]])

(defn handler [request]
  (when (and (= (:uri request) "/increment")
             (= (:request-method request) :post))
    (swap! counter inc))
  
  (let [current-count @counter]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (rum/render-html (counter-page current-count))})) ; Use Rum to render HTML

(def app
  (-> handler
      params/wrap-params ; Updated middleware
      ;; Note: The original resource/content-type/not-modified middlewares are removed
      ;; as per the example in the prompt implicitly by not being in the new ns :require
      ;; and not being in the new app pipeline. If they were still needed,
      ;; the :require would need to include them and they'd be in the pipeline.
      ;; For this refactoring, assuming they are replaced by params or no longer needed.
      ))

(defn -main [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (println (str "Starting web server on port " port "."))
    (jetty/run-jetty app {:port port :join? false})))
