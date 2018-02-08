(ns jimw-clj.todos
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r :refer [atom]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [cemerick.url :refer (url url-encode)]
            [clojure.string :as str]))

(defn api-root [url] (str (-> js/window .-location .-origin) url))

(defn get-api-token
  []
  (->
   (.getItem js/localStorage "[\"~#'\",\"~:api-token\"]")
   (clojure.string/split "\"")
   (get 3)))

(def memoized-api-token (memoize get-api-token))

(defn record-event
  [event_name event_data op-fn]
  (go (let [response
            (<!
             (http/post (api-root "/record-event")
                        {:headers {"jimw-clj-token" (memoized-api-token)}
                         :json-params
                         {:event_name event_name :event_data event_data}}))]
        (let [data (:body response)]
          (op-fn data)))))

;; (get-todos-list 4857 #(-> (zipmap  (map :id %) %) prn))
(defn get-todos-list
  [blog op-fn]
  (go (let [response
            (<!
             (http/get (api-root "/todos")
                       {:with-credentials? false
                        :headers {"jimw-clj-token" (memoized-api-token)}
                        :query-params {:blog blog}}))]
        (let [body (:body response)]
          (op-fn body)))))

;; (create-todo "dasdsadsa" 12 2222 #(prn %))
(defn create-todo [text parid blog op-fn]
  (go (let [response
            (<!
             (http/post (api-root "/create-todo")
                        {:with-credentials? false
                         :headers {"jimw-clj-token" (memoized-api-token)}
                         :query-params {:content text :parid parid :blog blog}}))]
        (if (= (:status response) 200)
          (op-fn (:body response))
          (js/alert "Create todo failure!")))))

;; (update-todo 11 "aaadasdsadsaoooo" 12 2222 #(prn %))
(defn update-todo [id text #_parid blog done op-fn]
  (go (let [response
            (<!
             (http/put (str (api-root "/update-todo/") id)
                       {:with-credentials? false
                        :headers {"jimw-clj-token" (memoized-api-token)}
                        :query-params
                        (if (nil? done)
                          {:content text :blog blog}
                          {:content text #_:parid #_parid :blog blog :done done})}))]
        (if (= (:status response) 200)
          (op-fn (:body response))
          (js/alert "Update todo failure!")))))

(defn update-todo-parid [blog id parid op-fn]
  (go (let [response
            (<!
             (http/put (str (api-root "/update-todo/") id)
                       {:with-credentials? false
                        :headers {"jimw-clj-token" (memoized-api-token)}
                        :query-params
                        {:parid parid :blog blog}}))]
        (if (= (:status response) 200)
          (op-fn (:body response))
          (js/alert "Update todo failure!")))))

;; (delete-todo 11 #(prn %))
(defn delete-todo [id op-fn]
  (go (let [response
            (<!
             (http/delete (api-root "/delete-todo")
                          {:with-credentials? false
                           :headers {"jimw-clj-token" (memoized-api-token)}
                           :query-params {:id id}}))]
        (if (= (:status response) 200)
          (op-fn (:body response))
          (js/alert "Delete todo failure!")))))

(defn update-todo-sort [origins response target op-fn]
  (go (let [response
            (<!
             (http/post (api-root "/update-todo-sort")
                        {:with-credentials? false
                         :headers {"jimw-clj-token" (memoized-api-token)}
                         :json-params {:origins origins :response response :target target}}))]
        (if (= (:status response) 200)
          (op-fn (:body response))
          (js/alert "Update todo sort failure!")))))

(defn todo-input [{:keys [content on-save on-stop search-fn]}]
  (let [val (r/atom content)
        stop #(do (reset! val "")
                  (if on-stop (on-stop)))
        save #(let [v (-> @val str clojure.string/trim)]
                (if-not (empty? v) (on-save v))
                (stop))]
    (fn [{:keys [id class placeholder]}]
      [:input {:type "text" :value @val
               :id id :class class :placeholder placeholder               
               #_:on-blur #_(do (if (fn? search-fn)
                               (do
                                 (search-fn @val)
                                 (if (empty? @val) nil
                                     (do
                                       (record-event "search-todo" @val identity)
                                       )
                                     )
                                 )
                               (save))
                             (set! (.-display (.-style (. js/document (getElementById "bdsug-search")))) "none"))
               :on-focus #(let [bdsug-stat (->> "bdsug-search" getElementById (. js/document) .-style .-display)]
                            (if (= bdsug-stat "none") (set! (.-display (.-style (. js/document (getElementById "bdsug-search")))) "block")))
               :on-change #(do
                             (let [valu (-> % .-target .-value)]
                               #_(if (fn? search-fn)
                                 (prn (search-fn valu)) nil)
                               #_(if search-text
                                   (reset! search-text valu))
                               (reset! val valu)
                               ;;(record-event "search-todo" valu identity)
                               )
                             )
               :on-key-down #(case (.-which %)
                               13 (if (fn? search-fn)
                                    (do
                                      (search-fn @val)
                                      (if (empty? @val) nil
                                          (do
                                            (record-event "search-todo" @val identity)
                                            )
                                          )
                                      )
                                    (save))
                               27 (stop)
                               nil)}])))

(defn todo-input-par [{:keys [id content on-save on-stop on-blur]}]
  (let [val (r/atom content)
        stop #(do (reset! val "")
                  (if on-stop (on-stop)))
        save #(let [v (-> @val str clojure.string/trim)]
                (if-not (empty? v) (on-save v))
                (stop))]
    (fn [{:keys [id class placeholder]}]
      [:input.input-par {:type "text" :value @val
                         :id id :class class :placeholder placeholder
                         :on-blur #(if on-blur (do (save) (on-blur)) (save))
                         :on-change #(reset! val (-> % .-target .-value))
                         :on-key-down #(case (.-which %)
                                         13 (save)
                                         27 (stop)
                                         nil)}])))

(def todo-edit (with-meta todo-input
                 {:component-did-mount #(.focus (r/dom-node %))}))


(defn todo-stats [{:keys [filt active done]}]
  (let [props-for (fn [name]
                    {:class (if (= name @filt) "selected")
                     :on-click #(reset! filt name)})]
    [:div
     [:span#todo-count
      [:strong active] " " #_(case active 1 "item" "items") " left"]
     [:ul#filters
      [:li [:a (props-for :all) "All"]]
      [:li [:a (props-for :active) "Active"]]
      [:li [:a (props-for :done) "Completed"]]]
     #_(when (pos? done)
         [:button#clear-completed ;; {:on-click clear-done}
          "Clear completed " done])]))

(defn todo-stats-tmp [{:keys [filt active done]}]
  (let [props-for (fn [name]
                    {:class (if (= name @filt) "selected")
                     :on-click #(reset! filt name)})]
    [:div
     [:ul#filters
      [:li [:a (props-for :all) "A"]]
      [:li [:a (props-for :active) "O"]]
      [:li [:a (props-for :done) "C"]]]
     (when (pos? done)
       [:button#clear-completed ;; {:on-click clear-done}
        "Clear completed " done])]))

(def new-todo-par
  (fn [sort_id blog-list blog-id on-blur]
    [todo-input-par
     {:id sort_id
      :type "text"
      :placeholder (str "Subneed to be done for " sort_id "?")
      :on-blur on-blur
      :on-save
      (fn [content]
        (create-todo
         content sort_id blog-id
         (fn [data]
           (swap! blog-list update-in
                  [(:blog data) :todos]
                  #(assoc % (:sort_id data) {:id (:sort_id data) :sort_id (:id data)
                                             :search true
                                             :parid (:parid data) :content (:content data)})))))}]))

(defn get-todo-sort-id [id items]
  (->
   (filter
    (fn [x] x (= (last x) id)) items)
   first last))

(defn todo-parid-input [{:keys [parid-val on-blur on-save]}]
  [:input {:type "number"
           :value @parid-val
           :on-blur on-blur
           :on-change #(reset! parid-val (-> % .-target .-value))
           :on-key-down #(case (.-which %)
                           13 (on-save @parid-val)
                           27 (on-save @parid-val)
                           nil)}])

(defn todo-item []
  (let [editing (r/atom false)]
    (fn [{:keys [id done content sort_id parid]} blog-list blog-id
         todo-target todo-begin origins]
      (let [parid-val (r/atom "")
            _ (reset! parid-val parid)]
        [:li {:class (str (if done "completed ")
                          (if @editing "editing"))
              :draggable true
              :on-drag-start #(do (prn (str "开始拖动" sort_id))
                                  (reset! todo-begin sort_id))
              :on-drag-end (fn []
                             (do
                               (prn (str "目标位置" @todo-target))
                               (update-todo-sort
                                (vec origins)
                                @todo-begin
                                (get-todo-sort-id @todo-target (vec origins))
                                (fn [data]
                                  (count
                                   (str 
                                    (for [mdata data]
                                      (swap! blog-list update-in
                                             [blog-id :todos]
                                             #(assoc % (:sort_id mdata) {:id (:sort_id mdata)
                                                                         :sort_id (:id mdata)
                                                                         :parid (:parid mdata)
                                                                         :content (:content mdata)})))))))))
              ;; 一直打印出来: TODOS修改经过上方的颜色
              :on-drag-over #(reset! todo-target id)}
         [:div.view
          [:input.toggle-checkbox
           {:type "checkbox"
            :checked done
            :on-change
            (fn []
              (let [done-stat (if (true? done) false true)]
                (swap! blog-list update-in
                       [blog-id :todos id :done] (fn [x] done-stat))
                (update-todo
                 sort_id nil blog-id done-stat
                 #(prn %))))}]
          [:label.todo-front-size {:on-double-click #(reset! editing true)} (str sort_id "◔" content)]
          [:button.destroy {:on-click
                            (fn []
                              (delete-todo
                               sort_id
                               (fn [data]
                                 (swap! blog-list update-in
                                        [blog-id :todos] #(dissoc % id)))))}]
          [:button.reply {:on-click #(set! (.-display (.-style (. js/document (getElementById (str "input-label-id-" id)))) ) "block")}]
          [:div.input-label {:id (str "input-label-id-" id)}
           (new-todo-par sort_id blog-list blog-id
                         #(set! (.-display (.-style (. js/document (getElementById (str "input-label-id-" id)))) ) "none"))]
          [:button.button-parid {:on-click #(set! (.-display (.-style (. js/document (getElementById (str "input-parid-id-" id)))) ) "block")}]
          [:label.input-parid {:id (str "input-parid-id-" id)}
           [todo-parid-input
            {:parid-val parid-val
             :on-save #(update-todo-parid blog-id sort_id % (fn [] (set! (.-display (.-style (. js/document (getElementById (str "input-parid-id-" id)))) ) "none")))
             :on-blur #(set! (.-display (.-style (. js/document (getElementById (str "input-parid-id-" id)))) ) "none")}]]]
         (when @editing
           [todo-edit {:class "edit" :content content
                       :on-save
                       (fn [content]
                         (update-todo
                          sort_id content blog-id nil
                          #(swap! blog-list update-in [blog-id :todos id :content] (fn [x] (:content %)))))
                       :on-stop #(reset! editing false)}])]))))

(defn new-todo [blog-list blog-id items parid-first-id search-fn]
  [todo-input {:id "new-todo"
               :placeholder "Search todo"
               :search-fn search-fn
               :on-save
               (fn [content]
                 (create-todo
                  content @parid-first-id blog-id
                  (fn [data]
                    (if (= @parid-first-id 1)
                      (reset! parid-first-id (:id data)))
                    (swap! blog-list update-in
                           [(:blog data) :todos]
                           #(assoc % (:sort_id data) {:id (:sort_id data) :sort_id (:id data)
                                                      :search true
                                                      :parid (:parid data) :content (:content data)})))))}])

;; (search-match-fn "完成websocket某某功能" "完成 功能") ;; => true
;; (search-match-fn "完成websocket某某功能" "完成 功能 aaa") ;; => false
(defn search-match-fn [item search-text]
  (if (empty? search-text)
    true
    (every?
     true?
     (map
      (fn [x]
        (if (re-matches (re-pattern (str "(.*)" x "(.*)")) item) true false))
      (str/split search-text " ")))))

(defn todo-app [blog-list blog-id]
  (let [filt (r/atom :all)
        search-text (r/atom "")]
    (fn []
      (let [items (vals (get-in @blog-list [blog-id :todos]))
            parid-first-id (-> (if (= (count items) 0) 1
                                   (->
                                    (filter #(= (:parid %) 1) items)
                                    first :sort_id)) r/atom)
            done (->> items (filter :done) count)
            active (- (count items) done)
            todo-target (atom 0)
            todo-begin (atom 0)
            origins (map #(vector (:sort_id %) (:id %)) items)
            set-search-fn (fn [id true-or-false]
                            (swap! blog-list update-in
                                   [blog-id :todos id :search] (fn [x] true-or-false)))
            search-fn #(do
                         (reset! search-text %)
                         ;;
                         (->
                          (for [{:keys [content id] :as todo} items]
                            (do
                              (set-search-fn id (search-match-fn content @search-text))
                              1)
                            ) str prn)
                         #_(prn "AAAAAAAA")
                         ;;
                         @search-text)]
        [:div
         #_[todo-stats-tmp {:active active :done done :filt filt}]
         #_[:br]
         [:section#todoapp
          [:header#header
           (new-todo blog-list blog-id items parid-first-id search-fn)]
          [:div {:class "bdsug" :id "bdsug-search"}
           #_[:ul 
            [:li {:data-key "哒哒加速器", :class "bdsug-overflow"}
             "哒哒加速器"] 
            [:li {:data-key "大道争锋", :class "bdsug-overflow"}
             "大道争锋"] 
            [:li {:data-key "哒哒英语", :class "bdsug-overflow"}
             "哒哒英语"] 
            [:li {:data-key "达达外卖", :class "bdsug-overflow"}
             "达达外卖"]]]
          (when (-> items count pos?)
            [:div
             [:section#main
              [:ul#todo-list
               (for [todo
                     (filter
                      (fn [item]
                        (= (:search item) true)
                        #_(if (empty? @search-text)
                          true
                          (every?
                           true?
                           (map
                            (fn [x]
                              (if (re-matches (re-pattern (str "(.*)" x "(.*)")) (str item)) true false))
                            (str/split @search-text " ")))))
                      (filter
                       (fn [item] (not (re-matches #"\d" (:content item))))
                       (filter
                        (case @filt
                          :active (complement :done)
                          :done :done
                          :all identity)
                        items)))]
                 ^{:key (:id todo)} [todo-item todo blog-list blog-id
                                     todo-target todo-begin origins])]]
             [:footer#footer
              [todo-stats {:active active :done done :filt filt}]]])]
         #_[:footer#info
            [:p "Double-click to edit a todo"]]]))))
