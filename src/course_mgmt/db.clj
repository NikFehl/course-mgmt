(ns course-mgmt.db
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.query :as mq]
            [monger.operators :refer :all]
            [monger.joda-time]
            [clojure.tools.logging :as log])
  (:import  org.bson.types.ObjectId
            [com.mongodb MongoOptions]))

;; localhost, default port, just connect
(let [conn  (mg/connect)
      db   (mg/get-db conn "mongo-test")]


(defn get-all [collection]
    (mc/find-maps db collection))

;; for attendee-management:
(defn insert-attendee [data]
  (merge
    (mc/insert-and-return db "attendees" data)
    {:position (mc/count db "attendees" {:course (:course data)})}))

(defn list-attendees []
    (mc/find-maps db "attendees"))

(defn get-attendee-position
  "get list of attendees sorted by :position"
  [params]
  (loop [loopobject (mc/find-maps db "attendees" {:course (:course params)})
         outputobject []
         pos 1]
      (if (seq loopobject)
        (recur (rest loopobject)
               (merge outputobject (merge (first loopobject) {:position pos}))
               (inc pos))
       outputobject)))

(defn get-attendees
  "gets attendees with sorts :sort-for and filter :course"
  [params]
  (if (= (:sort-for params) "ohne Sortierung")
    ;; without sort
    (if (= (:course params) "Alle Kurse")
      ;; plain result without sort or filter
      (list-attendees)
      ;; result with course-filter and loop for adding position-number
      (get-attendee-position {:course (:course params)}))
    ;; with sorting
    (let [sorting-for (case (:sort-for params)
                            ("Nachname") :lastname
                            ("Vorname") :firstname
                            ("Anmeldedatum") :timestamp
                            ("Alter") :birthdate)]
      (if (= (:course params) "Alle Kurse")
        (sort-by sorting-for (list-attendees))
        (sort-by sorting-for (get-attendee-position {:course (:course params)}))))
    ))

(defn delete-attendee [id]
  (mc/remove-by-id db "attendees" (ObjectId. id)))

;; for course-management:
(defn get-course [id]
  (mc/find-one-as-map db "courses" { :_id (ObjectId. id)}))

(defn get-courses-filtered
  "Get courses filter (by status for example)"
  [params]
  (log/info params)
  (mc/find-maps db "courses" params))

(defn course-manage
  "Depending on input, it will insert a new project or change an existing one."
  [input]
  (if (= (:id input) "nil")
      (mc/insert db "courses" {:name (:name input) :state (:state input) :supervisor (:supervisor input)})
      ((def comparison (get-course (:id input)))
        ;; compare if course-name changed & change it also for all attendees
        (if-not (= (:name input) (:name comparison))
          (mc/update db "attendees" {:course (:name comparison)} { $set {:course (:name input)} } {:multi true}))
        (mc/update-by-id db "courses" (ObjectId. (:id input)) {:name (:name input) :state (:state input) :supervisor (:supervisor input)}))
      ))

(defn delete-course [id]
  (mc/remove-by-id db "courses" (ObjectId. id)))

(defn count-courseattendees [course]
  (mc/count db "attendees" {:course course}))
)
