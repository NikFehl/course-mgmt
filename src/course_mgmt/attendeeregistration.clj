(ns course-mgmt.attendeeregistration
  (:require
        [course-mgmt.db :as db]
        [ring.util.anti-forgery :refer [anti-forgery-field]])
  (:use
        [hiccup.core]
        [hiccup.page]
        [hiccup.form]
        [hiccup.util]
        [hiccup.element]
        [course-mgmt.pagedefaults]))

(defn attendeeregistration
  [request]
    (html5  {:lang "en"}
      htmlheader
      [:body
        [:div.container
          navbar
          [:div.row [:h4 "Anmeldung" ]]]
        [:div.container {:data-offset-top "250"}
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
      htmlfooter
      [:headers {}]))
