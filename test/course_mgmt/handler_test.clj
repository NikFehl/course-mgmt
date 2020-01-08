(ns course-mgmt.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [course-mgmt.handler :refer :all]))

(deftest testing-gets
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404))))

  (testing "register-form"
    (let [response (app (mock/request :get "/attendees/register"))]
      (is (= (:status response) 200))))

  (testing "list attendees"
    (let [response (app (mock/request :get "/attendees/list"))]
      (is (= (:status response) 200))))

  (testing "list courses"
    (let [response (app (mock/request :get "/courses/manage"))]
      (is (= (:status response) 200)))))

(deftest testing-posts
  (testing "filter attendees"
    (let [response (app (mock/request :post "/attendees/list" {:course "Hundekuchenessen" :sort-for "Alter"}))]
      ;; response is forbidden due to anti-forgery-field.
      (is (= (:status response) 403)))))
