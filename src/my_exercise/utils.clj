(ns my-exercise.utils
    (:require [compojure.core :refer :all]
              [clojure.string :refer :all]
              [clj-time.format :as time-formatter]
              [clj-time.core :as time-core]
    )
)

;; Takes city string and converts it to proper format
;; Ex) Los Angeles -> los_angeles
(defn format-city-string [city]
    (clojure.string/replace (trim (clojure.string/lower-case city)) #" " "_" )
)

;; Takes string such as "in-mail" and normalizes it to a displayable string
;; Ex) in-mail -> In mail
(defn normalize-string [string]
    (clojure.string/capitalize (clojure.string/replace string #"-" " "))    
)

;; Takes date returned from response and formats it to a human readable date string
;; Ex) 2018-06-05T00:00:00Z -> 06/05/2018
(defn format-date [date-string]
    (def date (time-formatter/parse date-string))
    (time-formatter/unparse (time-formatter/formatter "MM/dd/yyy") date)
)