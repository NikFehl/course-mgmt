(ns course-mgmt.attendeelisting
  (:refer-clojure :exclude [format])
  (:require [ring.util.response]
            [clojure.tools.logging :as log]
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

  (def courseseats (Integer/parseInt (:seats (db/get-course-by-name (:course (first attendees))))))

  (html5  {:lang "en"}
  htmlheader
  [:body
    navbar
    [:div.container
      [:div.row [:h4 "Übersicht Anmeldungen:" ]]
      [:table
        [:tr
          (form-to [:post "/attendees/list"]
          [:td ]
          [:td
            (let [options (conj (for [{:keys [name]} (db/get-all "courses")] name) "Alle Kurse")
                  selected "Alle Kurse"]
                (drop-down :course options selected))]
          [:td
            (let [options ["Nachname" "Vorname" "Anmeldedatum" "Alter" "ohne Sortierung"]
                  selected "ohne Sortierung"]
                (drop-down :sort-for options selected))]
          [:td
            [:button {:type "submit"} "auswählen"] (anti-forgery-field) ]
          [:td ][:td ] [:td ])]]]
      [:table
        [:thead
          [:tr
            [:td ]
            [:td [:b "Anmeldedatum"]]
            (if (:position (first (seq attendees)))
              [:td [:b "Listenplatz"]])
            [:td [:b "Vorname"]]
            [:td [:b "Nachname"]]
            [:td [:b "Alter"]]
            [:td [:b "Ansprechperson"]]
            [:td [:b "Email"]]
            [:td [:b "Telefon"]]
            [:td [:b "Kommentar"]]]]
          [:tbody
          (for [{:keys [_id, timestamp, firstname, lastname, birthdate, contact, contactemail, contactphone, comment, position]} attendees]
            [:tr (if position
                    (if (> position courseseats)
                    {:style "text-align: center; background-color: #FFFCFD;"}))
              (form-to [:post "/attendees/manage"] [:td (hidden-field :id _id) [:button.button-clear {:type "submit"} "bearbeiten"] (anti-forgery-field) ])
              [:td [:small
                     (format "dd.MM.yyyy HH:mm" (local-date-time timestamp))]]
              (if position
                [:td (if (<= position courseseats)
                        {:style "text-align: center;"}
                        {:style "text-align: center; color: firebrick;"})
                      position])
              [:td (escape-html firstname)]
              [:td (escape-html lastname)]
              [:td {:title (format "dd.MM.yyyy" (local-date birthdate))}
                    (.getYears (period (local-date birthdate) (local-date)))]
              [:td (escape-html contact)]
              [:td (mail-to (escape-html contactemail))]
              [:td (escape-html contactphone)]
              [:td (escape-html comment)]]
          [:div  #"\n" "<br>"])]]
      ]
      htmlfooter))
