(ns course-mgmt.registration
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

(defn registration
  [request]
    (html5  {:lang "en"}
      [:body
        [:div [:h1 "Hier kommen die Daten rein:" ]]
        [:div {:data-offset-top "250"}
                    (form-to [:post "./register"]
                             [:fieldset
                            [:p   [:label "firstname"]
                              (text-field :firstname )]
                            [:p   [:label "lastname"]
                              (text-field :lastname )]
                            [:p   [:label "Comment"]
                              (text-area {:rows 5} :comment )]
                              [:button.btn {:type "submit"} "Submit!"]
                              (anti-forgery-field)])]]
      [:headers {}]))
