(ns course-mgmt.core
  (:refer-clojure :exclude [read-string])
  (:require [ring.adapter.jetty :as webserver]
            [ring.middleware.reload :refer [wrap-reload]]
            [compojure.core :refer [routes]]
            [course-mgmt.handler :as handler]
            [clojure.edn :refer [read-string]]))

;; load config file
(def jettyconfig (read-string (slurp "config/jetty.edn")))


(defn -dev-main
  "A very simple webserver using Ring & Jetty for auto-reload on development"
  []
  (do
    (webserver/run-jetty
      (wrap-reload #'handler/app)
      {:host    (:hostname jettyconfig)
       :port    (Integer/parseInt (:port jettyconfig))
       :join? false})))

(defn -main
  "A very simple webserver using Ring & Jetty"
  []
  (do
    (webserver/run-jetty handler/app
      {:host    (:hostname jettyconfig)
       :port    (Integer/parseInt (:port jettyconfig))})))
