(ns course-mgmt.coursemgmt
  (:require [ring.util.response]
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
        [java-time :only [local-date-time as]]))

(defn courseedit
  "Depending on input, it will give you a form for editing
   an existing project (and deletion button) or for creation of a new one."
  [id]
  (if (= id "nil")
      ((def course {:_id "nil" :name "Name" :state "deaktiviert" :supervisor "Verantwortlicher"})
       (def btntype "anlegen")
       (def del-form [:td ]))
      ((def course (db/get-course id))
       (def btntype "ändern")
       (def del-form (form-to [:delete "./manage"] [:td (hidden-field :id (:_id course)) [:button.button-clear {:type "submit"} "Kurs entfernen"] (anti-forgery-field)]))))
  (html5  {:lang "en"}
    htmlheader
    [:body
      [:div.container
        navbar
        [:div.row
          [:div.column [:h4 "Kurs " btntype ]]]]
      [:div.container
        [:table
          [:thead
            [:tr
              [:td [:b "Name"]]
              [:td [:b "Anmeldung"]]
              [:td [:b "Verantwortlicher"]]
              [:td ]
              [:td ]]]
          [:tbody
            [:tr
              (form-to [:post "./manage"]
              [:td (text-field :name (:name course))]
              [:td (let [options ["aktiv" "deaktiviert"]
                    selected (:state course)]
                    (drop-down :state options selected))]
              [:td (text-field :supervisor (:supervisor course))]
              [:td (hidden-field :id (:_id course)) [:button {:type "submit"} btntype] (anti-forgery-field)])
              del-form ]]]]]
      htmlfooter))

(defn courselist
  "Simple listing of all courses with attendee-count."
  [params]
    (let [courses (db/get-all "courses")]
    (html5  {:lang "en"}
      htmlheader
      [:body
        [:div.container
          navbar
          [:div.row [:h4 "Kursübersicht:" ]]]
        [:div.container
          [:table
            [:thead
              [:tr
                (form-to [:post "./edit"]
                  [:td  (hidden-field :id "nil") [:button {:type "submit"} "Neuen Kurs anlegen"] (anti-forgery-field) ])
                [:td [:b "Kursname"]]
                [:td [:b "Anmeldung"]]
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
                [:div  #"\n" "<br>"])]]]]
      htmlfooter)))
