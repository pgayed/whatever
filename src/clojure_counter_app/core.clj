(ns clojure-counter-app.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.resource :as resource]
            [ring.middleware.content-type :as content-type]
            [ring.middleware.not-modified :as not-modified])
  (:gen-class))

(defonce counter (atom 0))

(defn handler [request]
  ;; Check if the request is a POST to /increment
  (when (and (= (:uri request) "/increment")
             (= (:request-method request) :post))
    (swap! counter inc)) ; Increment the counter
  
  (let [current-count @counter]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (str "<html>"
                "<head>"
                "<title>Clojure Counter</title>"
                "<style>"
                "body { font-family: sans-serif; display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100vh; margin: 0; background-color: #f0f0f0; text-align: center; }"
                "h1 { color: #333; }"
                ".counter-value { font-size: 3em; color: #007bff; margin: 20px 0; }"
                "form { margin-top: 10px; }"
                "button { padding: 10px 20px; font-size: 1em; color: white; background-color: #007bff; border: none; border-radius: 5px; cursor: pointer; }"
                "button:hover { background-color: #0056b3; }"
                "button.reset { background-color: #6c757d; }"
                "button.reset:hover { background-color: #545b62; }"
                "</style>"
                "</head>"
                "<body>"
                "<h1>Clojure Counter App</h1>"
                "<div class='counter-value'>" current-count "</div>"
                
                ;; Form to increment the counter
                "<form action='/increment' method='post'>" 
                "<button type='submit'>Click me to Increment!</button>"
                "</form>"
                
                ;; Form to go to the root path (effectively a reset/refresh view)
                "<form action='/' method='get'>"
                "<button type='submit' class='reset'>Show Current Count</button>"
                "</form>"
                
                "</body>"
                "</html>")}))

(def app
  (-> handler
      (resource/wrap-resource "public")
      content-type/wrap-content-type
      not-modified/wrap-not-modified))

(defn -main [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (println (str "Starting web server on port " port "."))
    (jetty/run-jetty app {:port port :join? false})))
