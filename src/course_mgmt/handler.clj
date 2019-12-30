(ns course-mgmt.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [course-mgmt.db :as db]
            [course-mgmt.attendeeregistration :refer [attendeeregistration]]
            [course-mgmt.attendeelisting :refer [attendeelisting]]
            [course-mgmt.coursemgmt :refer :all]
            [ring.util.anti-forgery :refer [anti-forgery-field]])
  (:use
        [hiccup.core]
        [hiccup.page]
        [hiccup.form]
        [hiccup.util]
        [course-mgmt.pagedefaults]))

(defn welcome
  [request]
  (let [message (db/get-all "welcome")
        welcome-message (map :name message)]
    (html5  {:lang "en"}
     [:body
        [:div [:h1 (first welcome-message) ]]]
     [:headers {}])))



(defroutes app-routes
  "Default Routing for all incoming requests"
  (GET "/" [] #'welcome)
  (GET "/register" [] #'attendeeregistration)
  (POST "/register" [course firstname lastname birthdate contact contactemail contactphone comment]
        (do
          (db/insert-attendee {:course course :firstname firstname :lastname lastname :birthdate birthdate :contact contact :contactemail contactemail :contactphone contactphone :comment comment :timestamp (java.util.Date.)})
          (#'attendeelisting (db/list-attendees))))
  (DELETE "/list" [id]
        (do
          (db/delete-attendee id)
          (#'attendeelisting (db/list-attendees))))
  (GET "/list" [] (#'attendeelisting (db/list-attendees)))
  (POST "/list" [course] (attendeelisting (db/list-attendees-filtered {:course course})))
  (GET "/courses/manage" [] #'courselist)
  (POST "/courses/manage" [id name state supervisor]
        (do
          (db/course-manage {:id id :name name :state state :supervisor supervisor})
          #'courselist))
  (DELETE "/courses/manage" [id]
        (do
          (db/delete-course id)
          #'courselist))
  (POST "/courses/edit" [id] (courseedit id))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
