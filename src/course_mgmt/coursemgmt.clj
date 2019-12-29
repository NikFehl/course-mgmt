(ns course-mgmt.coursemgmt
  (:require [ring.util.response]
            [course-mgmt.db :as db]
            [ring.util.anti-forgery :refer [anti-forgery-field]])
  (:use
        [hiccup.core]
        [hiccup.page]
        [hiccup.form]
        [hiccup.util]
        [java-time :only [local-date-time as]]))

(defn courseedit
  [id]
  (if (= id "nil")
      ((def course {:_id "nil" :name "Name" :state "deaktiviert" :supervisor "Verantwortlicher"})
       (def btntype "anlegen")
       (def del-form [:td ]))
      ((def course (db/get-course id))
       (def btntype "ändern")
       (def del-form (form-to [:delete "./manage"] [:td (hidden-field :id (:_id course)) [:button.button-clear {:type "submit"} "Kurs entfernen"] (anti-forgery-field)]))))
  (html5  {:lang "en"}
    [:head
      (include-css "../css/milligram.css")]
    [:body
      [:div.container [:h4 "Kurs:"]]
      [:div.container
        [:table
          [:thead
            [:tr
              [:td [:b "Name"]]
              [:td [:b "Status"]]
              [:td [:b "Verantwortlicher"]]
              [:td ]
              [:td ]]]
          [:tbody
            [:tr
              (form-to [:post "./manage"]
              ;; Problem: Namensänderung des Kurses bei aktivem Kurs...
              [:td (text-field :name (:name course))]
              [:td (let [options ["aktiv" "deaktiviert"]
                    selected (:state course)]
                    (drop-down :state options selected))]
              [:td (text-field :supervisor (:supervisor course))]
              [:td (hidden-field :id (:_id course)) [:button {:type "submit"} btntype] (anti-forgery-field)])
              del-form ]
           ]]]]))

(defn coursedelete
  [params]
  )

(defn courselist
  [params]
    (let [courses (db/get-all "courses")]
    (html5  {:lang "en"}
      [:head
        (include-css "../css/milligram.css")]
      [:body
        [:div.container [:h3 "Kursübersicht:" ]]
        [:div.container
          [:table
            [:thead
              [:tr
                [:td ]
                [:td [:b "Kursname"]]
                [:td [:b "Status"]]
                [:td [:b "Verantwortlicher"]]
                [:td [:b "Anzahl Teilnehmer"]]]]
            [:tbody
              (for [{:keys [_id, name, state, supervisor]} courses]
              [:tr
                (form-to [:post "./edit"]
                  [:td (hidden-field :id _id) [:button.button-outline {:type "submit"} "ändern"] (anti-forgery-field) ])
                [:td (escape-html name)]
                [:td (escape-html state)]
                [:td (escape-html supervisor)]
                [:td (db/count-courseattendees name)]]
                [:div  #"\n" "<br>"])]]
          [:div.row
            [:div.column
              (form-to [:post "./edit"] (hidden-field :id "nil") [:button.button-clear {:type "submit"} "Neuen Kurs anlegen"] (anti-forgery-field))]]]])))
