(ns course-mgmt.db
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.operators :refer :all]
            [monger.joda-time])
  (:import  org.bson.types.ObjectId
            [com.mongodb MongoOptions]))

;; localhost, default port, just connect
(let [conn  (mg/connect)
      db   (mg/get-db conn "mongo-test")]


(defn get-all [collection]
    (mc/find-maps db collection))

;; for attendee-management:
(defn insert-attendee [data]
  (mc/insert db "attendees" data))

(defn list-attendees []
    (mc/find-maps db "attendees"))

(defn list-attendees-filtered [params]
  (mc/find-maps db "attendees" {:course (:course params)}))

(defn delete-attendee [id]
  (mc/remove-by-id db "attendees" (ObjectId. id)))

;; for course-management:
(defn get-course [id]
  (mc/find-one-as-map db "courses" { :_id (ObjectId. id)}))

(defn course-manage [input]
  "Depending on input, it will insert a new project or change an existing one."
  (if (= (:id input) "nil")
      (mc/insert db "courses" {:name (:name input) :state (:state input) :supervisor (:supervisor input)})
      ((def comparison (get-course (:id input)))
        ;; compare if course-name changed & change it also for all attendees
        (if-not (= (:id input) (:id comparison))
          (mc/update db "attendees" {:course (:name comparison)} { $set {:course (:name input)} } {:multi true}))
        (mc/update-by-id db "courses" (ObjectId. (:id input)) {:name (:name input) :state (:state input) :supervisor (:supervisor input)}))
      ))

(defn delete-course [id]
  (mc/remove-by-id db "courses" (ObjectId. id)))

(defn count-courseattendees [course]
  (mc/count db "attendees" {:course course}))
)
