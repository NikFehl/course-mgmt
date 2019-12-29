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
       (def btntype "anlegen"))
      ((def course (db/get-course id))
       (def btntype "ändern")))
  (html5  {:lang "en"}
    [:head
      (include-css "../css/milligram.css")]
    [:body
      [:div.container [:h4 "Kurs:"]]
      [:div.container
        (form-to [:post "../courses/manage"]
        [:table
          [:thead
            [:tr
              [:td [:b "Name"]]
              [:td [:b "Status"]]
              [:td [:b "Verantwortlicher"]]
              [:td
              ]]]
          [:tbody
            [:tr
              [:td (text-field :name (:name course))]
              [:td (let [options ["aktiv" "deaktiviert"]
                    selected (:state course)]
                    (drop-down :state options selected))]
              [:td (text-field :supervisor (:supervisor course))]
              [:td (hidden-field :id (:_id course)) [:button.button-clear {:type "submit"} btntype] (anti-forgery-field)]]
           ]])
              ]]))

(defn coursedelete
  [params]
  )

(defn courseinsert
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
                [:td "ändern?"]
                [:td [:b "Status"]]
                [:td [:b "Kurs"]]
                [:td [:b "Verantwortlicher"]]
                [:td [:b "Anzahl der Teilnehmer"]]]]
            [:tbody
              (for [{:keys [_id, name, state, supervisor, attendeecount]} courses]
              [:tr
                [:td (form-to [:post "./edit"] (hidden-field :id _id) [:button.button-clear {:type "submit"} "ändern"] (anti-forgery-field))]
                [:td (escape-html state)]
                [:td (escape-html name)]
                [:td (escape-html supervisor)]
                [:td (escape-html attendeecount)]]
                [:div  #"\n" "<br>"])]]
          [:div.row
            [:div.column
              (form-to [:post "./edit"] (hidden-field :id "nil") [:button.button-clear {:type "submit"} "Neuen Kurs anlegen"] (anti-forgery-field))]]]])))
