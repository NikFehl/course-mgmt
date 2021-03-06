(ns course-mgmt.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as ring]
            [clojure.tools.logging :as log]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.file :refer [file-request]]
            [course-mgmt.db :as db]
            [course-mgmt.attendeeregistration :refer [attendeeregistration check-registration]]
            [course-mgmt.attendeelisting :refer [attendeelisting exportattendees]]
            [course-mgmt.attendeeedit :refer [attendeeedit]]
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

  ;; Registration for Attendees
  (GET "/attendees/register" [] #'attendeeregistration)
  (POST "/attendees/register" [course firstname lastname birthdate contact contactemail contactphone comment]
          (check-registration {:course course :firstname firstname :lastname lastname :birthdate birthdate :contact contact :contactemail contactemail :contactphone contactphone :comment comment}))

  ;; Attendee Management
  (POST "/attendees/manage" [id] (attendeeedit(db/get-attendee id)))
  (POST "/attendees/edit" [id course firstname lastname birthdate contact contactemail contactphone comment]
        (do
          (db/edit-attendee {:id id :course course :firstname firstname :lastname lastname :birthdate birthdate :contact contact :contactemail contactemail :contactphone contactphone :comment comment})
          (ring/redirect "/attendees/list")))
  (DELETE "/attendees/edit" [id]
        (do
          (db/delete-attendee id)
          (ring/redirect "/attendees/list")))
  (GET "/attendees/list" [] (#'attendeelisting (db/list-attendees)))
  (POST "/attendees/list" [course sort-for] (attendeelisting (db/get-attendees {:course course :sort-for sort-for})))
  (POST "/attendees/export" [course]
        (do
          (def download (str "/exports/" (exportattendees course)))
          (ring/redirect download)))

  ;; Course Management
  (GET "/courses/manage" [] #'courselist)
  (POST "/courses/manage" [id name state registrationclose seats supervisor]
        (do
          (db/course-manage {:id id :name name :state state :registrationclose registrationclose :seats seats :supervisor supervisor})
          #'courselist))
  (DELETE "/courses/manage" [id]
        (do
          (db/delete-course id)
          #'courselist))
  (POST "/courses/edit" [id] (courseedit id))

  ;; Error Defaults
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
