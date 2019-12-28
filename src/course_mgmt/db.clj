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

(defn insert-attendee [data]
  (mc/insert db "users" data))

(defn list-attendees []
    (mc/find-maps db "users"))

(defn delete-attendee [id]
  (mc/remove-by-id db "users" (ObjectId. id))))
