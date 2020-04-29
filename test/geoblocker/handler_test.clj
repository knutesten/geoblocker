(ns geoblocker.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [geoblocker.geoblocker :refer [ip-ranges]]
            [geoblocker.middleware :refer [valid-country-codes]]
            [geoblocker.handler :refer :all]))

(deftest test-app
  (let [trondheim {:from    1041293312
                   :to      1041294591
                   :code    "NO"
                   :country "Norway"
                   :region  "Sor-Trondelag"
                   :city    "Trondheim"}]
    (with-redefs [ip-ranges           (atom (list trondheim))
                  valid-country-codes (atom #{})]

      (testing "index route with no access should return status 403"
        (let [response (app (-> (mock/request :get "/")
                                (mock/header "x-forwarded-for" "62.16.228.100")))]
          (is (= 403 (:status response)))))

      (testing "index route with access should return status 200"
        (reset! valid-country-codes #{"NO"})
        (let [response (app (-> (mock/request :get "/")
                                (mock/header "x-forwarded-for" "62.16.228.100")))]
          (is (= 200 (:status response)))))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= 404 (:status response))))))
