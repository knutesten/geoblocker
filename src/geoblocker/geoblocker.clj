(ns geoblocker.geoblocker
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(defn ip-number
  "IP Number = 16777216*w + 65536*x + 256*y + z
  IP Address = w.x.y.z "
  [ip]
  (try
    (->> (clojure.string/split ip #"\.")
         (map #(Integer/parseInt %))
         (map * [16777216 65536 256 1])
         (reduce +))
    (catch Exception e 0)))

(defn- in-range? [ipn range]
  (let [{from :from
         to   :to} range]
    (and (<= from ipn)
         (<= ipn to))))

(defonce ip-ranges (atom nil))

(defn- parse-csv-row [row]
  {:from    (Long/parseLong (get row 0))
   :to      (Long/parseLong (get row 1))
   :code    (get row 2)
   :country (get row 3)})

(defn load-ip-ranges-file! []
  (with-open [reader (io/reader (io/resource "ip.csv"))]
    (->> (csv/read-csv reader)
         (mapv parse-csv-row)
         (reset! ip-ranges))))

(when (nil? @ip-ranges )
  (load-ip-ranges-file!))

(defn geo-location [ip]
  (->> @ip-ranges
       (filter (partial in-range? (ip-number ip)))
       first))
