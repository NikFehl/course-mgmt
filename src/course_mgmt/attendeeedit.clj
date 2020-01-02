(ns course-mgmt.attendeeedit
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

(defn attendeeedit
  "edit or delete attendee"
  [params]
  (html5  {:lang "en"}
  htmlheader
  [:body
    [:div.container
      navbar
      [:div.row [:h4 "Anmeldung ändern:" ]]]
  ;;  [:div.container
    ;;  [:div.row
      [:table
        [:thead
          [:tr
            [:td [:b "Kurs"]]
            [:td [:b "Vorname"]]
            [:td [:b "Nachname"]]
            [:td [:b "Geburtsdatum"]]
            [:td [:b "Ansprechperson"]]
            [:td [:b "Email"]]
            [:td [:b "Telefon"]]
            [:td [:b "Kommentar"]]
            [:td ]
            [:td ]]]
          [:tbody
            [:tr
              (log/info (:_id params))
              (form-to [:post "/attendees/edit"] (hidden-field :id (:_id params))
              [:td
                (let [options (for [{:keys [name]} (db/get-courses-filtered {:state "aktiv"})] name)]
                  (drop-down :course options))]
              [:td (text-field :firstname (escape-html (:firstname params)))]
              [:td (text-field :lastname (escape-html (:lastname params)))]
              [:td (text-field :birthdate (escape-html (:birthdate params)))]
              [:td (text-field :contact (escape-html (:contact params)))]
              [:td (text-field :contactemail (escape-html (:contactemail params)))]
              [:td (text-field :contactphone (escape-html (:contactphone params)))]
              [:td (text-field :comment (escape-html (:comment params)))]
              [:td  [:button {:type "submit"} "speichern"] (anti-forgery-field) ])
              [:td (form-to [:delete "/attendees/edit"] [:td (hidden-field :id (:_id params)) [:button.button-clear {:type "submit"} "Teilnehmer löschen"] (anti-forgery-field)])]]]
          ]]
      htmlfooter))
