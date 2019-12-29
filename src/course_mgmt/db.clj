(ns course-mgmt.db
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.operators :refer :all]
            [monger.joda-time])
  (:import  org.bson.types.ObjectId
            [com.mongodb MongoOptions]))

;; localhost, default port
(let [conn  (mg/connect)
      db   (mg/get-db conn "mongo-test")]

;; not needed now:
;;(defn- transform-id-to-string [document]
;;  (if-let [id (:_id document)]
;;  (assoc document :_id (.toString id))))

(defn get-all [collection]
;;  (map transform-id-to-string (mc/find-maps db collection))))
    (mc/find-maps db collection))

;; for attendee-management:
(defn insert-attendee [data]
  (mc/insert db "attendees" data))

(defn list-attendees []
    (mc/find-maps db "attendees"))

(defn delete-attendee [id]
  (mc/remove-by-id db "attendees" (ObjectId. id)))

;; for course-management:
(defn get-course [id]
  (mc/find-one-as-map db "courses" { :_id (ObjectId. id)}))

(defn course-manage [data]
  (if (= (:id data) "nil")
      (mc/insert db "courses" {:name (:name data) :state (:state data) :supervisor (:supervisor data)})
      (mc/update-by-id db "courses" (ObjectId. (:id data)) {:name (:name data) :state (:state data) :supervisor (:supervisor data)})))

(defn delete-course [id]
  (mc/remove-by-id db "courses" (ObjectId. id)))

(defn count-courseattendees [course]
  (mc/count db "attendees" {:course course}))
)
