(ns course-mgmt.attendeelisting
  (:require [ring.util.response]
            [course-mgmt.db :as db]
            [ring.util.anti-forgery :refer [anti-forgery-field]])
  (:use
        [hiccup.core]
        [hiccup.page]
        [hiccup.form]
        [hiccup.util]
        [hiccup.element]
        [course-mgmt.pagedefaults]
        [java-time :only [local-date-time as]]))


(defn attendeelisting
  [attendees]
  (html5  {:lang "en"}
  htmlheader
  [:body
    [:div.container
      navbar
      [:div.row [:h4 "Übersicht Anmeldungen:" ]]]
    [:div.container
    ;; liste sortieren nach Anmeldedatum, Nachname, Listenplatz,
      [:table
        [:thead
          [:tr
            [:td ]
            [:td "Anmeldedatum"]
            [:td "Vorname"]
            [:td "Nachname"]
            [:td "Geburtsdatum"]
            [:td "Ansprechperson"]
            [:td "Email"]
            [:td "Telefon"]
            [:td "Kommentar"]]]
          [:tbody
          (for [{:keys [_id, timestamp, firstname, lastname, birthdate, contact, contactemail, contactphone, comment]} attendees]
            [:tr
              (form-to [:delete "./list"] [:td (hidden-field :id _id) [:button.button-clear {:type "submit"} "entfernen"] (anti-forgery-field) ])
              [:td (if timestamp
                     (local-date-time timestamp))]
              [:td (escape-html firstname)]
              [:td (escape-html lastname)]
              [:td (escape-html birthdate)]
              [:td (escape-html contact)]
              [:td (escape-html contactemail)]
              [:td (escape-html contactphone)]
              [:td (escape-html comment)]]
          [:div  #"\n" "<br>"])]]]]
      htmlfooter))
