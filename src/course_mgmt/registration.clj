(ns course-mgmt.registration
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [course-mgmt.db :as db]
            [ring.util.anti-forgery :refer [anti-forgery-field]])
  (:use
        [hiccup.core]
        [hiccup.page]
        [hiccup.form]
        [hiccup.util]
        [java-time :only [local-date-time as]]))

(defn registration
  [request]
    (html5  {:lang "en"}
      [:head
        (include-css "css/milligram.css")]
      [:body
        [:div.container [:h1 "Registration" ]]
        [:div.container {:data-offset-top "250"}
            (form-to [:post "./register"]
              [:fieldset
                [:div.row
                    [:div.column.column-25 [:label "Vorname:"]]
                    [:div.column.column-25 (text-field :firstname )]]
                [:div.row
                    [:div.column.column-25 [:label "Nachname"]]
                    [:div.column.column-25 (text-field :lastname )]]
                [:div.row
                    [:div.column.column-25 [:label "Kommentar"]]
                    [:div.column.column-25 (text-area {:rows 5} :comment )]]
                [:div.row
                    [:div.column.column-25 [:button {:type "submit"} "registrieren"]]]
                    (anti-forgery-field)])]]
      [:headers {}]))
