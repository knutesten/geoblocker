(ns geoblocker.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [geoblocker.middleware :as m]
            [geoblocker.view :as v]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.util.response :as r]))

(defn- index-handler [req]
  (v/content (:geo-location req)))

(defn- admin-handler [_]
  (v/admin @m/valid-country-codes))

(defn- new-country-code-handler [req]
  (let [code (-> req :params :code clojure.string/upper-case)]
    (when (re-matches #"[A-Z]{2}" code)
      (swap! m/valid-country-codes conj code)))
  (r/redirect "/admin"))

(defn- delete-country-code-handler [req]
  (let [code (-> req :params :code clojure.string/upper-case)]
    (swap! m/valid-country-codes disj code))
  (r/redirect "/admin"))

(defroutes app-routes
  (GET "/" [] (->
                index-handler
                m/wrap-geo-block
                m/wrap-geo-location))
  (GET "/admin" [] admin-handler)
  (POST "/admin/country-code" [] new-country-code-handler)
  (DELETE "/admin/country-code" [] delete-country-code-handler)
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
