(ns course-mgmt.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [course-mgmt.db :as db]
            [course-mgmt.registration :refer [registration]]
            [course-mgmt.userlisting :refer [userlisting]]
            [ring.util.anti-forgery :refer [anti-forgery-field]])
  (:use
        [hiccup.core]
        [hiccup.page]
        [hiccup.form]
        [hiccup.util]
        [java-time :only [local-date-time as]]))

(defn welcome
  [request]
  (let [message (db/get-all "welcome")
        welcome-message (map :name message)]
    (html5  {:lang "en"}
     [:body
        [:div [:h1 (first welcome-message) ]]]
     [:headers {}])))



(defroutes app-routes
  (GET "/" [] #'welcome)
  (GET "/register" [] #'registration)
  (POST "/register" [firstname lastname comment]
        (do
          (db/insert-user {:firstname firstname :lastname lastname :comment comment :timestamp (java.util.Date.)})
          ;;  :time (java.util.Date.) - UTC Zeit
          (#'userlisting (db/list-users))))
  (DELETE "/list" [id]
        (do
          (db/delete-id id)
          (#'userlisting (db/list-users))))
  (GET "/list" [] (#'userlisting (db/list-users)))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
