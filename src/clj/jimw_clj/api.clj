(ns jimw-clj.api
  (:require
   [cheshire.core :as cjson]
   [compojure.core :refer :all]
   [compojure.route :as route]
   [clojure.tools.logging :as log]
   [clj-time.core :as t]
   [clj-time.local :as l]
   [clj-time.coerce :as ct]
   [clj-time.format :as f]
   [clj-time.coerce :as time-coerce]
   [jimw-clj.db.core :as db]
   [ring.util.http-response :refer :all]
   [ring.util.response :refer [response]]
   [taoensso.timbre :refer [error debug info]])
  (:gen-class))

(defn get-blogs
  [{{:keys [q limit offset]
     :or   {limit 10 offset 0 q ""}} :params}]
  (ok (db/search-blogs {:db db/*db* :q q :limit (Integer/parseInt limit) :offset (Integer/parseInt offset)})))

(defn update-blog
  [{{:keys [id name content]} :params}]
  (let [res (db/update-blog {:db db/*db* :id (Integer/parseInt id) :name name :content content})]
    (if res
      (ok res) (not-found))))

(defroutes api-routes
  (GET "/blogs" [] get-blogs)
  (PUT "/update-blog/:id" [] update-blog))
