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
      [:div.row
        [:div.column.column-offset-25.column-25 [:h4 "Anmeldung ändern:" ]]]
      (form-to [:post "/attendees/edit"] (hidden-field :id (:_id params))
      [:div.row
        [:div.column.column-offset-25.column-25 [:b "Kurs"]]
        [:div.column.column-25
          (let [options (for [{:keys [name]} (db/get-all "courses")] name)]
            (drop-down :course options))]]
      [:div.row
        [:div.column.column-offset-25.column-25 [:b "Vorname"]]
        [:div.column.column-25 (text-field :firstname (escape-html (:firstname params)))]]
      [:div.row
        [:div.column.column-offset-25.column-25 [:b "Nachname"]]
        [:div.column.column-25 (text-field :lastname (escape-html (:lastname params)))]]
      [:div.row
        [:div.column.column-offset-25.column-25 [:b "Geburtsdatum"]]
        [:div.column.column-25 (text-field :birthdate (escape-html (:birthdate params)))]]
      [:div.row
        [:div.column.column-offset-25.column-25 [:b "Ansprechperson"]]
        [:div.column.column-25 (text-field :contact (escape-html (:contact params)))]]
      [:div.row
        [:div.column.column-offset-25.column-25 [:b "Email"]]
        [:div.column.column-25 (text-field :contactemail (escape-html (:contactemail params)))]]
      [:div.row
        [:div.column.column-offset-25.column-25 [:b "Telefon"]]
        [:div.column.column-25 (text-field :contactphone (escape-html (:contactphone params)))]]
      [:div.row
        [:div.column.column-offset-25.column-25 [:b "Kommentar"]]
        [:div.column.column-25 (text-area {:rows 5} :comment (escape-html (:comment params)))]]
      [:div.row
        [:div.column.column-offset-25.column-25 ]
        [:div.column.column-25 [:button {:type "submit"} "Änderungen speichern"] (anti-forgery-field) ]])
      [:div.row
        [:div.column.column-offset-25.column-25 ]
        [:div.column.column-25 (form-to [:delete "/attendees/edit"] [:td (hidden-field :id (:_id params)) [:button.button-clear {:type "submit"} "Teilnehmer löschen"] (anti-forgery-field)]) ]]
      ]]
  htmlfooter))
