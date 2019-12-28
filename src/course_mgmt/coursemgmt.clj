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
  [params]
  )

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
                [:td (form-to [:post "./courses/manage"] (hidden-field :id _id) [:button.button-clear {:type "submit"} "ändern"] (anti-forgery-field))]
                [:td (escape-html name)]
                [:td (escape-html state)]
                [:td (escape-html supervisor)]
                [:td (escape-html attendeecount)]]
                [:div  #"\n" "<br>"])]]
                ]])))
