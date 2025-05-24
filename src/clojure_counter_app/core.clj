(ns clojure-counter-app.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as response] ; Updated
            [rum.core :as rum] ; Added Rum
            [ring.middleware.params :as params] ; Re-adding for query-params
            [ring.middleware.cookies :as cookies])
  (:gen-class))

(defonce counter (atom 0))
(defonce users-credentials (atom {}))
(defonce active-sessions (atom #{}))

(defn generate-random-string [length]
  (apply str (repeatedly length #(rand-nth "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"))))

(rum/defc counter-page [current-count logged-in? username password] ; Changed argument names
  [:html
   [:head
    [:title "Clojure Rum Counter"]]
   [:body
    [:h1 "Clojure Counter App (Rum)"]
    [:p (str "Debug: logged-in? is " logged-in?)] ; <<< DEBUG LINE
    [:div (str current-count)]
    [:form {:action "/increment" :method "post"}
     [:button {:type "submit"} "Click me to Increment!"]]
    
    (if logged-in?
      [:form {:action "/logout" :method "post"}
       [:button {:type "submit"} "Logout"]]
      [:form {:action "/login" :method "post"}
       [:button {:type "submit"} "Login"]])
    
    (when (and logged-in? username password) ; Changed condition and variables
      [:div
       [:p (str "Your username: " username)] ; Changed variable
       [:p (str "Your password: " password)]]) ; Changed variable
    ]])

(defn handler [request]
  (let [session-token (get-in request [:cookies "session-token" :value])]
    (cond
      ;; Login route
      (and (= (:uri request) "/login") (= (:request-method request) :post))
      (let [new-username (generate-random-string 10)
            new-password (generate-random-string 10)
            new-session-token (generate-random-string 20)]
        (swap! users-credentials assoc new-session-token {:username new-username :password new-password})
        (swap! active-sessions conj new-session-token)
        (-> (response/redirect "/") ; Redirect to /
            (assoc-in [:cookies "session-token"] {:value new-session-token :path "/"})))

      ;; Logout route
      (and (= (:uri request) "/logout") (= (:request-method request) :post) session-token)
      (do
        (swap! active-sessions disj session-token)
        (swap! users-credentials dissoc session-token)
        (-> (response/redirect "/")
            (assoc-in [:cookies "session-token"] {:value "" :path "/" :max-age 0}))) ; Expire cookie

      ;; Increment counter
      (and (= (:uri request) "/increment") (= (:request-method request) :post))
      (do
        (swap! counter inc)
        (response/redirect "/"))

      ;; Main page
      (= (:uri request) "/")
      (let [current-count @counter
            logged-in? (and session-token (contains? @active-sessions session-token))
            creds (when logged-in? (get @users-credentials session-token))] ; Fetch if logged in
        {:status 200
         :headers {"Content-Type" "text/html"}
         :body (rum/render-html (counter-page current-count logged-in? (:username creds) (:password creds)))})

      ;; Default not found
      :else
      {:status 404
       :headers {"Content-Type" "text/html"}
       :body "<h1>404 Not Found</h1>"})))

(def app
  (-> handler
      params/wrap-params
      cookies/wrap-cookies) ; Middleware pipeline
      ;; Note: The original resource/content-type/not-modified middlewares are removed
      ;; as per the example in the prompt implicitly by not being in the new ns :require
      ;; and not being in the new app pipeline. If they were still needed,
      ;; the :require would need to include them and they'd be in the pipeline.
      ;; For this refactoring, assuming they are replaced by params or no longer needed.
      )

(defn -main [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (jetty/run-jetty app {:port port :join? false})))
