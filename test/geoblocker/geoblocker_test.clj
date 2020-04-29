(ns geoblocker.geoblocker-test
  (:require [geoblocker.geoblocker :refer :all]
            [clojure.test :refer :all]))

(deftest test-ip-number
  (testing "called with 202.186.13.4 should return 3401190660"
    (is (= 3401190660 (ip-number "202.186.13.4"))))

  (testing "called with 62.16.228.100 should return 1041294436"
    (is (= 1041294436 (ip-number "62.16.228.100")))))

(deftest test-geo-location
  (testing "geo-location should return geo-information for ip"
    (let [trondheim {:from    1041293312
                     :to      1041294591
                     :code    "NO"
                     :country "Norway"
                     :region  "Sor-Trondelag"
                     :city    "Trondheim"}]
      (with-redefs [ip-ranges (atom (list trondheim))]
        (is (= trondheim (geo-location "62.16.228.100")))))))
