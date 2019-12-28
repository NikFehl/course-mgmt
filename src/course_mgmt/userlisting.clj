(ns course-mgmt.userlisting
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


(defn userlisting
  [users]
  (html5  {:lang "en"}
  [:head
      (include-css "css/milligram.css")]
  [:body
    [:div.container [:h1 "Übersicht Anmeldungen:" ]]
    [:div.container {:data-offset-top "250"}
      [:table
        [:thead
          [:tr
            [:td "Entfernen?"]
            [:td [:b "Vorname"]]
            [:td [:b "Nachname"]]
            [:td [:b "Anmeldedatum"]]
            [:td [:b "Kommentar"]]]]
          [:tbody
          (for [{:keys [_id, firstname, lastname, timestamp, comment]} users]
            [:tr
              [:td (form-to [:delete "./list"] (hidden-field :id _id) [:button.button-clear {:type "reset"} "entfernen"] (anti-forgery-field))]
              [:td (escape-html firstname)]
              [:td (escape-html lastname)]
              [:td (if timestamp
                     (local-date-time timestamp))]
              [:td (escape-html comment)]]
          [:div  #"\n" "<br>"])]]]]))
