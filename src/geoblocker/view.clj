(ns geoblocker.view
  (:use [hiccup core form page]
        [ring.util.anti-forgery]))

(defn content [geo]
  (let [{code    :code
         country :country
         region  :region
         city    :city} geo]
    (html5 [:h1 "Your geo location"]
           [:table
            [:tr [:td [:b "Code"]] [:td code]]
            [:tr [:td [:b "Country"]] [:td country]]])))

(defn admin [codes]
  (html5
    [:h1 "Change restirctions"]

    [:div "Country codes that currently have access"]
    [:ul
     (for [code codes]
       [:li
        (form-to [:delete "/admin/country-code"]
                 (anti-forgery-field)
                 [:span (str (h code) " ")]
                 [:input {:id :code :name :code :type "hidden" :value code}]
                 [:button {:type "submit"} "Remove"])])]

    (form-to [:post "/admin/country-code"]
             (anti-forgery-field)
             (label :code "Country code to give access")
             [:br]
             (text-field :code)
             [:button {:type "submit"} "Add"])))
