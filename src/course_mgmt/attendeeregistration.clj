(ns course-mgmt.attendeeregistration
  (:refer-clojure :exclude [format])
  (:require
        [course-mgmt.db :as db]
        [clojure.tools.logging :as log]
        [ring.util.anti-forgery :refer [anti-forgery-field]])
  (:use
        [hiccup.core]
        [hiccup.page]
        [hiccup.form]
        [hiccup.util]
        [hiccup.element]
        [course-mgmt.pagedefaults]
        [java-time :only [local-date format]]))

(defn registrationresult
  "show results of registration"
  [params]
  (html5  {:lang "en"}
    htmlheader
    [:body
      [:div.container
        navbar
        [:div.row
          [:div.column.column-50 {:style "text-align: center; data-offset-top: 150px;"} [:h4 "Glückwunsch!" ]]]
        [:div.row
          [:div.column.column-50 {:style "text-align: center;"} "Du bist erfolgreich registiert und Nr. " [:b (:position params) ] " auf der Warteliste."]]
        [:div.row [:p ]]]
      [:div.container
        [:div.row
          [:div.column.column-20 [:label "Kurs:"]]
          [:div.column.column-25 (escape-html (:course params))]]
        [:div.row
          [:div.column.column-20 [:label "Vorname:"]]
          [:div.column.column-25 (escape-html (:firstname params))]]
        [:div.row
          [:div.column.column-20 [:label "Nachname:"]]
          [:div.column.column-25 (escape-html (:lastname params))]]
        [:div.row
          [:div.column.column-20 [:label "Geburtsdatum:"]]
          [:div.column.column-25 (format "dd/MM/yyyy" (local-date (:birthdate params))) ]]
        [:div.row
          [:div.column.column-20 [:label "Ansprechperson:"]]
          [:div.column.column-25 (escape-html (:contact params))]]
        [:div.row
          [:div.column.column-20 [:label "Email:"]]
          [:div.column.column-25 (escape-html (:contactemail params))]]
        [:div.row
          [:div.column.column-20 [:label "Telefon:"]]
          [:div.column.column-25 (escape-html (:contactphone params)) ]]
        [:div.row
          [:div.column.column-20 [:label "Kommentar:"]]
          [:div.column.column-25 (escape-html (:comment params))]]
      ]
    ]
    htmlfooter
    [:headers {}]))


(defn attendeeregistration
  "Registration Form."
  [request]
    (html5  {:lang "en"}
      htmlheader
      [:body
        [:div.container
          navbar
          [:div.row [:h4 "Anmeldung" ]]]
        [:div.container {:style "data-offset-top: 150;"}
            (form-to [:post "./register"]
              [:fieldset
                [:div.row
                    [:div.column.column-25 [:label "Kurs:"]]
                    [:div.column.column-25
                    ;; Status wird nicht berücksichtigt
                        (let [options (for [{:keys [name]} (db/get-all "courses")] name)]
                        (drop-down :course options))]]
                [:div.row
                    [:div.column.column-25 [:label "Vorname:"]]
                    [:div.column.column-25 (text-field {:required "true"} :firstname)]]
                [:div.row
                    [:div.column.column-25 [:label "Nachname:"]]
                    [:div.column.column-25 (text-field {:required "true"} :lastname)]]
                [:div.row
                    [:div.column.column-25 [:label "Geburtsdatum:"]]
                    [:div.column.column-25 [:input {:id "birthdate" :name "birthdate" :type "date" :required "true"}]]]
                [:div.row
                    [:div.column.column-25 [:label "Ansprechperson:"]]
                    [:div.column.column-25 (text-field {:required "true"} :contact)]]
                [:div.row
                    [:div.column.column-25 [:label "Email:"]]
                    [:div.column.column-25 (text-field {:required "true"} :contactemail)]]
                [:div.row
                    [:div.column.column-25 [:label "Telefon:"]]
                    [:div.column.column-25 (text-field :contactphone)]]
                [:div.row
                    [:div.column.column-25 [:label "Kommentar:"]]
                    [:div.column.column-25 (text-area {:rows 5} :comment)]]
                [:div.row
                    [:div.column [:input {:type "checkbox" :required "true"}] "Hiermit bestätige ich, dass meine Daten für die Anmeldung gespeichert werden. "]]
                [:div.row
                    [:div.column [:button {:type "submit"} "unverbindlich Anmelden"]]]
                    (anti-forgery-field)])]]
      htmlfooter
      [:headers {}]))
