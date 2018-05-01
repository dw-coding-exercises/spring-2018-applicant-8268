(ns my-exercise.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [my-exercise.utils :as utils]
            [clojure.string :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.reload :refer [wrap-reload]]
            [my-exercise.home :as home]
            [cheshire.core :refer :all]
            [clj-http.client :as client]))

; String interpolates the base endpoint with the state and city
; to produce the final endpoint we'll hit
(defn api-endpoint [state city]
  (str "https://api.turbovote.org/elections/upcoming?district-divisions=ocd-division/country:us/state:" state 
    ",ocd-division/country:us/state:" state "/place:" city
    )
)


;; Extracts relevant info from the form submission
;; and places them into a map
(defn get-user-data-map [request]
    {:street-1 (get-in request [:params :street])
      :street-2 (get-in request [:params :street-2])
      :city (utils/format-city-string (get-in request [:params :city]))
      :state (lower-case (get-in request [:params :state]))
      :zip (get-in request [:params :zip])
    }  
)

;; Makes http get to the Democracy Works API
(defn hit-endpoint [endpoint]
  (get (client/get endpoint {:accept :json}) :body)  
)

(defroutes app
  (GET "/" [] (home/page "Home" (home/address-form )))
  (POST "/search" request
    ;; Extracts data from form submission -> hits api endpoint -> passes results to the results page
    (do
      (def user-data (get-user-data-map request))
      (def results (hit-endpoint (api-endpoint (get user-data :state) (get user-data :city))))
      
      (home/page "Results" (home/upcoming-elections results))
    )  
  )
  (route/resources "/")
  (route/not-found "Not found"))

(def handler
  (-> app
      (wrap-defaults site-defaults)
      wrap-reload))
