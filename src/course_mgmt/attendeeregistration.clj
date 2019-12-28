(ns course-mgmt.attendeeregistration
  (:require
        [ring.util.anti-forgery :refer [anti-forgery-field]])
  (:use
        [hiccup.core]
        [hiccup.page]
        [hiccup.form]
        [hiccup.util]))

(defn attendeeregistration
  [request]
    (html5  {:lang "en"}
      [:head
        (include-css "css/milligram.css")]
      [:body
        [:div.container [:h2 "Anmeldung" ]]
        [:div.container {:data-offset-top "250"}
            (form-to [:post "./register"]
              [:fieldset
                [:div.row
                    [:div.column.column-25 [:label "Kurs:"]]
                    [:div.column.column-25
                        (let [options ["Babyschwimmen" "Anfängerschwimmen" "Seepferdchen"]
                              selected "Seepferdchen"]
                        (drop-down :course options selected))]]
                [:div.row
                    [:div.column.column-25 [:label "Vorname:"]]
                    [:div.column.column-25 (text-field :firstname)]]
                [:div.row
                    [:div.column.column-25 [:label "Nachname:"]]
                    [:div.column.column-25 (text-field :lastname)]]
                [:div.row
                    [:div.column.column-25 [:label "Geburtsdatum:"]]
                    [:div.column.column-25 (text-field :birthdate "01-01-2000")]]
                [:div.row
                    [:div.column.column-25 [:label "Ansprechperson:"]]
                    [:div.column.column-25 (text-field :contact)]]
                [:div.row
                    [:div.column.column-25 [:label "Email:"]]
                    [:div.column.column-25 (text-field :contactemail)]]
                [:div.row
                    [:div.column.column-25 [:label "Telefon:"]]
                    [:div.column.column-25 (text-field :contactphone)]]
                [:div.row
                    [:div.column.column-25 [:label "Kommentar:"]]
                    [:div.column.column-25 (text-area {:rows 5} :comment)]]
                [:div.row
                    [:div.column [:input {:type "checkbox"}] "Hiermit bestätige ich, dass meine Daten für die Anmeldung gespeichert werden. "]]
                [:div.row
                    [:div.column [:button {:type "submit"} "unverbindlich Anmelden"]]]
                    (anti-forgery-field)])]]
      [:headers {}]))
