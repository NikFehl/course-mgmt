(ns course-mgmt.inputchecker
  (:require [course-mgmt.db :as db]
            [clojure.tools.logging :as log] ))


(defn verify-email
  [email]
  (re-matches #"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?" email))

(defn verify-name
  [name]
  (re-matches #"[^a()+*#@{}$%<>|\\]+\w+.{1,30}" name))

(defn verify-phone
  [phone]
  (re-matches #"\+?[0-9\/|-]+" phone))

(defn verify-registration-inputs
  [params]
  (if (verify-email (:contactemail params))
    (if (verify-name (:firstname params))
      (if (verify-name (:lastname params))
        (if (verify-name (:contact params))
          (if (empty? (:contactphone params))
            "Alle Eingaben ok"
            (if (verify-phone (:contactphone params))
              "Alle Eingaben ok"
              "Bitte überprüfe die Eingabe der Telefonnummer!"))
          "Bitte überprüfe die Eingabe der Ansprechperson!")
        "Bitte überprüfe die Eingabe des Nachnamens!")
      "Bitte überprüfe die Eingabe des Vornamens!")
    "Bitte gebe eine korrekte Email Adresse ein!"))
