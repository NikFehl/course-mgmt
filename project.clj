(defproject course-mgmt "0.1.0-SNAPSHOT"
  :description "For managing courses. functions: registration, waitlist, auto-reply"
  :url "https://github.com/NikFehl/course-mgmt"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [compojure "1.6.1"]
                 [ring "1.8.0"]
                 [ring/ring-defaults "0.3.2"]
                 [com.novemberain/monger "3.5.0"]
                 [clojure.java-time "0.3.2"]
                 [org.clojure/tools.logging "0.5.0"]
                 [org.clojure/data.csv "0.1.4"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler course-mgmt.handler/app}
  :main course-mgmt.core
  :profiles
      {:dev {:main course-mgmt.core/-dev-main
             :dependencies [[javax.servlet/servlet-api "2.5"]
                            [ring/ring-mock "0.3.2"]]}})
