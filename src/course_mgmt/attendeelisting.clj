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
        [java-time :only [local-date-time local-date format period]]))


(defn attendeelisting
  "List all attendees, incl. age-calculation in years and contact details"
  [attendees]
  (html5  {:lang "en"}
  htmlheader
  [:body
    [:div.container
      navbar
      [:div.row [:h4 "Übersicht Anmeldungen:" ]]
      [:table
        [:tr
          (form-to [:post "./list"]
          [:td ]
          [:td
            (let [options (for [{:keys [name]} (db/get-all "courses")] name)]
                (drop-down :course options))]
          [:td
            [:button {:type "submit"} "auswählen"] (anti-forgery-field) ]
          [:td ][:td ] [:td ])]]]
    [:div.container
      [:div.row
      [:table
        [:thead
          [:tr
            [:td ]
            [:td [:b "Anmeldedatum"]]
            [:td [:b "Vorname"]]
            [:td [:b "Nachname"]]
            [:td [:b "Alter"]]
            [:td [:b "Ansprechperson"]]
            [:td [:b "Email"]]
            [:td [:b "Telefon"]]
            [:td [:b "Kommentar"]]]]
          [:tbody
          (for [{:keys [_id, timestamp, firstname, lastname, birthdate, contact, contactemail, contactphone, comment]} attendees]
            [:tr
              (form-to [:delete "./list"] [:td (hidden-field :id _id) [:button.button-clear {:type "submit"} "entfernen"] (anti-forgery-field) ])
              [:td [:small
                     (format "dd/MM/yyyy HH:mm" (local-date-time timestamp))]]
              [:td (escape-html firstname)]
              [:td (escape-html lastname)]
              [:td {:title (format "dd/MM/yyyy" (local-date birthdate))}
                    (.getYears (period (local-date birthdate) (local-date)))]
              [:td (escape-html contact)]
              [:td (mail-to (escape-html contactemail))]
              [:td (escape-html contactphone)]
              [:td (escape-html comment)]]
          [:div  #"\n" "<br>"])]]]]]
      htmlfooter))
