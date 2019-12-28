(ns course-mgmt.core
  (:require [ring.adapter.jetty :as webserver]
            [ring.middleware.reload :refer [wrap-reload]]
            [compojure.core :refer [routes]]
            [course-mgmt.handler :as handler]))


(defn -dev-main
  "A very simple webserver using Ring & Jetty for auto-reload on development"
  [port-number]
  (do
    (webserver/run-jetty
      (wrap-reload #'handler/app)
      {:port    (Integer/parseInt port-number)
       :join? false})))

(defn -main
  "A very simple webserver using Ring & Jetty"
  [port-number]
  (do
    (webserver/run-jetty handler/app
      {:port    (Integer/parseInt port-number)})))
