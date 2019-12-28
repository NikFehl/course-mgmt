(ns course-mgmt.attendeelisting
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


(defn attendeelisting
  [users]
  (html5  {:lang "en"}
  [:head
      (include-css "css/milligram.css")]
  [:body
    [:div.container [:h1 "Übersicht Anmeldungen:" ]]
    [:div.container {:data-offset-top "250"}
    ;; liste sortieren nach Anmeldedatum, Nachname, Listenplatz,
      [:table
        [:thead
          [:tr
            [:td "Entfernen?"]
            [:td [:b "Anmeldedatum"]]
            [:td [:b "Vorname"]]
            [:td [:b "Nachname"]]
            [:td [:b "Geburtsdatum"]]
            [:td [:b "Ansprechperson"]]
            [:td [:b "Email"]]
            [:td [:b "Telefon"]]
            [:td [:b "Kommentar"]]]]
          [:tbody
          (for [{:keys [_id, timestamp, firstname, lastname, birthdate, contact, contactemail, contactphone, comment]} users]
            [:tr
              [:td (form-to [:delete "./list"] (hidden-field :id _id) [:button.button-clear {:type "submit"} "entfernen"] (anti-forgery-field))]
              [:td (if timestamp
                     (local-date-time timestamp))]
              [:td (escape-html firstname)]
              [:td (escape-html lastname)]
              [:td (escape-html birthdate)]
              [:td (escape-html contact)]
              [:td (escape-html contactemail)]
              [:td (escape-html contactphone)]
              [:td (escape-html comment)]]
          [:div  #"\n" "<br>"])]]]]))
