(ns course-mgmt.userlisting
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [course-mgmt.db :as db]
            [ring.util.anti-forgery :refer [anti-forgery-field]])
  (:use
        [hiccup.core]
        [hiccup.page]
        [hiccup.form]
        [hiccup.util]
        [java-time :only [local-date-time as]]))


(defn userlisting
  [users]
  (html5  {:lang "en"}
  [:body
    [:div [:h1 "Hier sind die Daten:" ]]
    [:div {:data-offset-top "250"}
      [:table
          [:tr
            [:td "Entfernen?"]
            [:td [:b "Vorname"]]
            [:td [:b "Nachname"]]
            [:td [:b "Anmeldedatum"]]
            [:td [:b "Kommentar"]]]
          (for [{:keys [_id, firstname, lastname, timestamp, comment]} users]
            [:tr
              [:td (form-to [:delete "./list"] (hidden-field :id _id) [:button.btn.btn-mini.btn-link {:type "submit"} [:i.icon-remove ]] (anti-forgery-field))]
              [:td (escape-html firstname)]
              [:td (escape-html lastname)]
              [:td (if timestamp
                     (local-date-time timestamp))]
              [:td (escape-html comment)]]
          [:div  #"\n" "<br>"])]]]))
