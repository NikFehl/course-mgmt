(ns course-mgmt.attendeeregistration
  (:refer-clojure :exclude [format])
  (:require
        [course-mgmt.db :as db]
        [clojure.tools.logging :as log]
        [course-mgmt.inputchecker :refer [verify-registration-inputs]]
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
      navbar
        (let [successmessage  (if (<= (:position params) (:courseseats params))
                              [:h4 " Glückwunsch! " ]
                              [:h4 " Registrierung erfolgreich! " ])
              coursepos     (if (<= (:position params) (:courseseats params))
                              [:p "Du bist erfolgreich registiert und Teilnehmer Nr. " [:b (:position params) ] " im Kurs."]
                              [:p "Leider haben wir bereits viele Anmeldungen. Du bist Nr. " [:b (- (:position params)(:courseseats params)) ] " auf der Warteliste."])]
          [:div.container
            [:div.row
              [:div.column.column-50.column-offset-25 {:style "text-align: center;"} successmessage (log/info successmessage) ]]
            [:div.row
              [:div.column.column-50.column-offset-25 {:style "text-align: center;"} coursepos ]]
            [:div.row [:p ]]])
      [:div.container
        [:div.row
          [:div.column.column-20.column-offset-25 [:label "Kurs:"]]
          [:div.column.column-25 (escape-html (:course params))]]
        [:div.row
          [:div.column.column-20.column-offset-25 [:label "Vorname:"]]
          [:div.column.column-25 (escape-html (:firstname params))]]
        [:div.row
          [:div.column.column-20.column-offset-25 [:label "Nachname:"]]
          [:div.column.column-25 (escape-html (:lastname params))]]
        [:div.row
          [:div.column.column-20.column-offset-25 [:label "Geburtsdatum:"]]
          [:div.column.column-25 (format "dd/MM/yyyy" (local-date (:birthdate params))) ]]
        [:div.row
          [:div.column.column-20.column-offset-25 [:label "Ansprechperson:"]]
          [:div.column.column-25 (escape-html (:contact params))]]
        [:div.row
          [:div.column.column-20.column-offset-25 [:label "Email:"]]
          [:div.column.column-25 (escape-html (:contactemail params))]]
        [:div.row
          [:div.column.column-20.column-offset-25 [:label "Telefon:"]]
          [:div.column.column-25 (escape-html (:contactphone params)) ]]
        [:div.row
          [:div.column.column-20.column-offset-25 [:label "Kommentar:"]]
          [:div.column.column-25 (escape-html (:comment params))]]
      ]
    ]
    htmlfooter
    [:headers {}]))


(defn attendeeregistration
  "Registration Form."
  [params]
    (html5  {:lang "en"}
      htmlheader
      [:body
        navbar
        [:div.container
          [:div.row
            [:div.column.column-offset-25 [:h4 "Anmeldung" ]]]
          [:div.row
            [:div.column.column-offset-25
              [:p {:style "color: red;"}(:checkresult params)]]]]
        [:div.container {:style "data-offset-top: 150;"}
            (form-to [:post "/attendees/register"]
              [:fieldset
                [:div.row
                    [:div.column.column-25.column-offset-25 [:label "Kurs:"]]
                    [:div.column.column-25
                        (let [options (for [{:keys [name]} (db/get-courses-filtered {:state "aktiv"})] name)
                              selected (:course params)]
                        (drop-down :course options selected))]]
                [:div.row
                    [:div.column.column-25.column-offset-25 [:label "Vorname:"]]
                    [:div.column.column-25 (text-field {:required "true"} :firstname (:firstname params))]]
                [:div.row
                    [:div.column.column-25.column-offset-25 [:label "Nachname:"]]
                    [:div.column.column-25 (text-field {:required "true"} :lastname (:lastname params))]]
                [:div.row
                    [:div.column.column-25.column-offset-25 [:label "Geburtsdatum:"]]
                    [:div.column.column-25 [:input {:id "birthdate" :name "birthdate" :type "date" :required "true" :value (:birthdate params)}]]]
                [:div.row
                    [:div.column.column-25.column-offset-25 [:label "Ansprechperson:"]]
                    [:div.column.column-25 (text-field {:required "true"} :contact (:contact params))]]
                [:div.row
                    [:div.column.column-25.column-offset-25 [:label "Email:"]]
                    [:div.column.column-25 (text-field {:required "true"} :contactemail (:contactemail params))]]
                [:div.row
                    [:div.column.column-25.column-offset-25 [:label "Telefon:"]]
                    [:div.column.column-25 (text-field :contactphone (:contactphone params))]]
                [:div.row
                    [:div.column.column-25.column-offset-25 [:label "Kommentar:"]]
                    [:div.column.column-25 (text-area {:rows 5} :comment (:comment params))]]
                [:div.row
                    [:div.column.column-offset-25 [:input {:type "checkbox" :required "true"}] "Hiermit bestätige ich, dass meine Daten für die Anmeldung gespeichert werden. "]]
                [:div.row
                    [:div.column.column-offset-25 [:button {:type "submit"} "unverbindlich Anmelden"]]]
                    (anti-forgery-field)])]]
      htmlfooter
      [:headers {}]))


(defn check-registration
  [params]
  (let [checkresult (verify-registration-inputs params)]
    (if (= checkresult "Alle Eingaben ok")
      (registrationresult (db/insert-attendee {:course (:course params) :firstname (:firstname params) :lastname (:lastname params) :birthdate (:birthdate params) :contact (:contact params) :contactemail (:contactemail params) :contactphone (:contactphone params) :comment (:comment params) :timestamp (java.util.Date.)}))
      (attendeeregistration (merge params {:checkresult checkresult})))))
