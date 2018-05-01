(ns my-exercise.home
  (:require [hiccup.page :refer [html5]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [my-exercise.us-state :as us-state]
            [cheshire.core :refer :all]
            [my-exercise.utils :as utils]))

(defn header []
  [:head
    [:meta {:charset "UTF-8"}]
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1.0, maximum-scale=1.0"}]
    [:title "Find my next election"]
    [:link {:rel "stylesheet" :href "default.css"}]
    [:link {:rel "stylesheet" :href "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" :integrity "sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" :crossorigin "anonymous"}]

    [:script {:src "https://code.jquery.com/jquery-3.3.1.min.js" :integrity "sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" :crossorigin "anonymous"}]
    [:script {:src "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" :integrity "sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" :crossorigin "anonymous"}]  
   ]
   
)

(defn upcoming-elections [response]
  (def data (parse-string response true))

  [:div {:class "row"}
    [:div {:class "col-md-12" :style "text-align:center;"}
      (for [result data]
        [:div 
          [:h1 (get result :description)]
          [:span {:style "font-weight: bold;"} "Voting Day: "] (utils/format-date (get result :date))
          [:div {:class "row"}
            [:div {:class "col-md-12" :style "text-align:left;"}
              (for [method (get result :district-divisions)]
                [:div 
                  (for [voter-method (get method :voting-methods)]
                    [:div 
                      [:h3 "Voting Type: " (utils/normalize-string (get voter-method :type))]
                      (if (get voter-method :instructions)
                        (do
                          [:label {:style "font-weight: bold;"} "Instructions: "]
                          [:p (get-in voter-method [:instructions :voting-id])]
                        )
                      )
                    ]
                  )
                  [:a {:href (get result :polling-place-url)} "Polling Place Website"]
                ]
              )
            ]
          ]
        ]
      )
    ]
  ]  
)

(defn address-form []
  [:div {:class "address-form"}
   [:h1 "Find my next election"]
   [:form {:action "/search" :method "post"}
    (anti-forgery-field)
    [:p "Enter the address where you are registered to vote"]
    [:div
     [:label {:for "street-field"} "Street:"]
     [:input {:id "street-field"
              :type "text"
              :name "street"}]]
    [:div
     [:label {:for "street-2-field"} "Street 2:"]
     [:input {:id "street-2-field"
              :type "text"
              :name "street-2"}]]
    [:div
     [:label {:for "city-field"} "City:"]
     [:input {:id "city-field"
              :type "text"
              :name "city"}]
     [:label {:for "state-field"} "State:"]
     [:select {:id "state-field"
               :name "state"}
      [:option ""]
      (for [state us-state/postal-abbreviations]
        [:option {:value state} state])]
     [:label {:for "zip-field"} "ZIP:"]
     [:input {:id "zip-field"
              :type "text"
              :name "zip"
              :size "10"}]]
    [:div.button
     [:button {:type "submit"} "Search"]]]])

(defn page [title content]
  (html5
   (header)

   [:body {:class "container"}
    content
   ]
  )
)
