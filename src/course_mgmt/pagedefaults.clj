(ns course-mgmt.pagedefaults
  (:use
        [hiccup.core]
        [hiccup.page]
        [hiccup.form]
        [hiccup.util]
        [hiccup.element] ))

(def htmlheader
  [:head
    (include-css "../css/milligram.css")])

(def navbar
  [:div.container
    [:div.row
      [:div.column
        [:b "Navigation: " ]
        (link-to "/" "Welcome") " | "
        (link-to "/list" "AttendeeList") " | "
        (link-to "/register" "AttendeeRegistration") " | "
        (link-to "/courses/manage" "CoursesList")
        ]]
    [:div.row
      [:div.column [:p ]]]])

(def htmlfooter
  [:footer
    [:div.container
      [:div.row
        [:div.column.column-offset-33
          [:p ]
          [:small "Course-Mgmt by "(link-to "https://github.com/NikFehl/course-mgmt" "NikFehl")
                  " - made with "(link-to "https://clojure.org/" "Clojure")
                  " & "(link-to "https://www.mongodb.com/" "MongoDB")
                  " - CSS by " (link-to "https://milligram.io/" "Milligram.io ") ]]]]])
