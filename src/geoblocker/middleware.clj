(ns geoblocker.middleware
  (:require
   [ring.util.response :refer [response status content-type]]
   [geoblocker.geoblocker :refer [geo-location]]))

(defonce valid-country-codes (atom #{}))

(defn wrap-geo-block [handler]
  (fn [req]
    (let [code (-> req :geo-location :code)]
      (if (contains? @valid-country-codes code)
        (handler req)
        (->
          (str "Requests from your country (" code ") are not allowed to see this page")
          (response)
          (content-type "text/html")
          (status 403))))))

(defn- last-ip-from-x-forwared-for [req]
  (let [header (-> req :headers (get "x-forwarded-for"))]
    (-> (or header "")
        (clojure.string/split #",\s*")
        (last))))

(defn wrap-geo-location [handler]
  (fn [req]
    (let [ip  (or (last-ip-from-x-forwared-for req)
                  (:remote-addr req))
          geo (geo-location ip)]
      (handler (assoc req :geo-location geo)))))
