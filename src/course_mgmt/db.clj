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
  (mc/insert db "attendees" data))

(defn list-attendees []
    (mc/find-maps db "attendees"))

(defn attendees-sorted
  "gets attendees sorted via :sort-for and optional :course"
  [params]
  (let [sorting-for (case (:sort-for params)
                          ("Nachname") :lastname
                          ("Vorname") :firstname
                          ("Anmeldedatum") :timestamp
                          ("Alter") :birthdate)
        course-filter (if (= (:course params) "Alle Kurse")
                          (array-map )
                          (array-map :course (:course params)))]
    (mq/with-collection db "attendees"
      (mq/find course-filter)
      (mq/sort (array-map sorting-for 1 )))))

(defn attendees-filtered
  "gets attendees filtered via :course"
  [params]
  (let [course-filter (array-map :course (:course params))]
    (mc/find-maps db "attendees" course-filter)))

(defn get-attendees
  "gets attendees with sorts :sort-for and filter :course"
  [params]
  (if (= (:sort-for params) "ohne Sortierung")
    ;; without sort
    (if (= (:course params) "Alle Kurse")
      ;; plain result without sort or filter
      (list-attendees)
      ;; result with course-filter and loop for adding position-number
      (loop [loopobject (attendees-filtered {:course (:course params)})
             outputobject []
             pos 1]
        (if (seq loopobject)
          (recur (rest loopobject)
                 (merge outputobject (merge (first loopobject) {:position pos}))
                 (inc pos))
          outputobject)))
    ;; with sorting
    (attendees-sorted {:sort-for (:sort-for params) :course (:course params)})))

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
