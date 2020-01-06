(ns course-mgmt.attendeelisting
  (:refer-clojure :exclude [format])
  (:require [ring.util.response]
            [clojure.tools.logging :as log]
            [course-mgmt.db :as db]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io])
  (:use
        [hiccup.core]
        [hiccup.page]
        [hiccup.form]
        [hiccup.util]
        [hiccup.element]
        [course-mgmt.pagedefaults]
        [java-time :only [local-date-time local-date format period]]))


(defn exportattendees
  "Creates CSV file on selected course and results the path & filename"
  [course]
  (let [data (if (= course "Alle Kurse")
                (db/list-attendees)
                (db/get-attendee-position {:course course}))
        datapath (str "resources/public/exports/")
        exportfile (str (format "yyyyMMddHHmm"(local-date-time)) "-" course ".csv")]
    (with-open [writer (io/writer (str datapath exportfile))]
      (csv/write-csv writer
        (cons ["Anmeldedatum", "Position", "Vorname", "Nachname", "Geburtsdatum", "Ansprechperson", "Email", "Telefon", "Kommentar"]
          (for [{:keys [timestamp, position, firstname, lastname, birthdate, contact, contactemail, contactphone, comment]} data]
            [timestamp position firstname lastname birthdate contact contactemail contactphone comment]))))
    exportfile))



(defn attendeelisting
  "List all attendees, incl. age-calculation in years and contact details"
  [attendees]

  (def courseseats (Integer/parseInt (:seats (db/get-course-by-name (:course (first attendees))))))
  (def allcourses (for [{:keys [name]} (db/get-all "courses")] name))

  (html5  {:lang "en"}
  htmlheader
  [:body
    navbar
    [:div.container
      [:div.row [:h4 "Übersicht Anmeldungen:" ]]
      [:details [:summary [:b "Filtern?"]]
        [:table
          [:tr
            (form-to [:post "/attendees/list"]
            [:td ]
            [:td
              (let [options (conj allcourses "Alle Kurse")
                    selected "Alle Kurse"]
                  (drop-down :course options selected))]
            [:td ]
            [:td
              (let [options ["Nachname" "Vorname" "Anmeldedatum" "Alter" "ohne Sortierung"]
                    selected "ohne Sortierung"]
                (drop-down :sort-for options selected))]
            [:td ]
            [:td
              [:button {:type "submit"} "filtern"] (anti-forgery-field) ]
            [:td ][:td ])]]]
      [:details [:summary [:b "Exportieren?"]]
        [:table
          [:tr
            (form-to [:post "/attendees/export"]
            [:td ]
            [:td
              (let [options (conj allcourses "Alle Kurse")
                    selected "Alle Kurse"]
                  (drop-down :course options selected))]
            [:td ]
            [:td
              [:button {:type "submit"} "exportieren"] (anti-forgery-field) ]
            [:td ][:td ])]]]]
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
