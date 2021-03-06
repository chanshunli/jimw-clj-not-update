(ns jimw-clj.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [clojure.core.async :as async :refer [<! >!]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            ;;[markdown.core :refer [md->html]]
            ;;[jimw-clj.ajax :refer [load-interceptors!]]
            ;;[ajax.core :refer [GET POST]]
            [cljs-http.client :as http]
            [cljsjs.marked]
            [cljsjs.highlight]
            [cljsjs.highlight.langs.1c]
            [cljsjs.highlight.langs.abnf]
            [cljsjs.highlight.langs.accesslog]
            [cljsjs.highlight.langs.actionscript]
            [cljsjs.highlight.langs.ada]
            [cljsjs.highlight.langs.apache]
            [cljsjs.highlight.langs.applescript]
            [cljsjs.highlight.langs.arduino]
            [cljsjs.highlight.langs.armasm]
            [cljsjs.highlight.langs.asciidoc]
            [cljsjs.highlight.langs.aspectj]
            [cljsjs.highlight.langs.autohotkey]
            [cljsjs.highlight.langs.autoit]
            [cljsjs.highlight.langs.avrasm]
            [cljsjs.highlight.langs.awk]
            [cljsjs.highlight.langs.axapta]
            [cljsjs.highlight.langs.bash]
            [cljsjs.highlight.langs.basic]
            [cljsjs.highlight.langs.bnf]
            [cljsjs.highlight.langs.brainfuck]
            [cljsjs.highlight.langs.cal]
            [cljsjs.highlight.langs.capnproto]
            [cljsjs.highlight.langs.ceylon]
            [cljsjs.highlight.langs.clean]
            [cljsjs.highlight.langs.clojure]
            [cljsjs.highlight.langs.clojure-repl]
            [cljsjs.highlight.langs.cmake]
            [cljsjs.highlight.langs.coffeescript]
            [cljsjs.highlight.langs.coq]
            [cljsjs.highlight.langs.cos]
            [cljsjs.highlight.langs.cpp]
            [cljsjs.highlight.langs.crmsh]
            [cljsjs.highlight.langs.crystal]
            [cljsjs.highlight.langs.cs]
            [cljsjs.highlight.langs.csp]
            [cljsjs.highlight.langs.css]
            [cljsjs.highlight.langs.d]
            [cljsjs.highlight.langs.dart]
            [cljsjs.highlight.langs.delphi]
            [cljsjs.highlight.langs.diff]
            [cljsjs.highlight.langs.django]
            [cljsjs.highlight.langs.dns]
            [cljsjs.highlight.langs.dockerfile]
            [cljsjs.highlight.langs.dos]
            [cljsjs.highlight.langs.dsconfig]
            [cljsjs.highlight.langs.dts]
            [cljsjs.highlight.langs.dust]
            [cljsjs.highlight.langs.ebnf]
            [cljsjs.highlight.langs.elixir]
            [cljsjs.highlight.langs.elm]
            [cljsjs.highlight.langs.erb]
            [cljsjs.highlight.langs.erlang]
            [cljsjs.highlight.langs.erlang-repl]
            [cljsjs.highlight.langs.excel]
            [cljsjs.highlight.langs.fix]
            [cljsjs.highlight.langs.flix]
            [cljsjs.highlight.langs.fortran]
            [cljsjs.highlight.langs.fsharp]
            [cljsjs.highlight.langs.gams]
            [cljsjs.highlight.langs.gauss]
            [cljsjs.highlight.langs.gcode]
            [cljsjs.highlight.langs.gherkin]
            [cljsjs.highlight.langs.glsl]
            [cljsjs.highlight.langs.go]
            [cljsjs.highlight.langs.golo]
            [cljsjs.highlight.langs.gradle]
            [cljsjs.highlight.langs.groovy]
            [cljsjs.highlight.langs.haml]
            [cljsjs.highlight.langs.handlebars]
            [cljsjs.highlight.langs.haskell]
            [cljsjs.highlight.langs.haxe]
            [cljsjs.highlight.langs.hsp]
            [cljsjs.highlight.langs.htmlbars]
            [cljsjs.highlight.langs.http]
            [cljsjs.highlight.langs.hy]
            [cljsjs.highlight.langs.inform7]
            [cljsjs.highlight.langs.ini]
            [cljsjs.highlight.langs.irpf90]
            [cljsjs.highlight.langs.java]
            [cljsjs.highlight.langs.javascript]
            [cljsjs.highlight.langs.jboss-cli]
            [cljsjs.highlight.langs.json]
            [cljsjs.highlight.langs.julia]
            [cljsjs.highlight.langs.julia-repl]
            [cljsjs.highlight.langs.kotlin]
            [cljsjs.highlight.langs.lasso]
            [cljsjs.highlight.langs.ldif]
            [cljsjs.highlight.langs.leaf]
            [cljsjs.highlight.langs.less]
            [cljsjs.highlight.langs.lisp]
            [cljsjs.highlight.langs.livecodeserver]
            [cljsjs.highlight.langs.livescript]
            [cljsjs.highlight.langs.llvm]
            [cljsjs.highlight.langs.lsl]
            [cljsjs.highlight.langs.lua]
            [cljsjs.highlight.langs.makefile]
            [cljsjs.highlight.langs.markdown]
            [cljsjs.highlight.langs.mathematica]
            [cljsjs.highlight.langs.matlab]
            [cljsjs.highlight.langs.maxima]
            [cljsjs.highlight.langs.mel]
            [cljsjs.highlight.langs.mercury]
            [cljsjs.highlight.langs.mipsasm]
            [cljsjs.highlight.langs.mizar]
            [cljsjs.highlight.langs.mojolicious]
            [cljsjs.highlight.langs.monkey]
            [cljsjs.highlight.langs.moonscript]
            [cljsjs.highlight.langs.n1ql]
            [cljsjs.highlight.langs.nginx]
            [cljsjs.highlight.langs.nimrod]
            [cljsjs.highlight.langs.nix]
            [cljsjs.highlight.langs.nsis]
            [cljsjs.highlight.langs.objectivec]
            [cljsjs.highlight.langs.ocaml]
            [cljsjs.highlight.langs.openscad]
            [cljsjs.highlight.langs.oxygene]
            [cljsjs.highlight.langs.parser3]
            [cljsjs.highlight.langs.perl]
            [cljsjs.highlight.langs.pf]
            [cljsjs.highlight.langs.php]
            [cljsjs.highlight.langs.pony]
            [cljsjs.highlight.langs.powershell]
            [cljsjs.highlight.langs.processing]
            [cljsjs.highlight.langs.profile]
            [cljsjs.highlight.langs.prolog]
            [cljsjs.highlight.langs.protobuf]
            [cljsjs.highlight.langs.puppet]
            [cljsjs.highlight.langs.purebasic]
            [cljsjs.highlight.langs.python]
            [cljsjs.highlight.langs.q]
            [cljsjs.highlight.langs.qml]
            [cljsjs.highlight.langs.r]
            [cljsjs.highlight.langs.rib]
            [cljsjs.highlight.langs.roboconf]
            [cljsjs.highlight.langs.routeros]
            [cljsjs.highlight.langs.rsl]
            [cljsjs.highlight.langs.ruby]
            [cljsjs.highlight.langs.ruleslanguage]
            [cljsjs.highlight.langs.rust]
            [cljsjs.highlight.langs.scala]
            [cljsjs.highlight.langs.scheme]
            [cljsjs.highlight.langs.scilab]
            [cljsjs.highlight.langs.scss]
            [cljsjs.highlight.langs.shell]
            [cljsjs.highlight.langs.smali]
            [cljsjs.highlight.langs.smalltalk]
            [cljsjs.highlight.langs.sml]
            [cljsjs.highlight.langs.sqf]
            [cljsjs.highlight.langs.sql]
            [cljsjs.highlight.langs.stan]
            [cljsjs.highlight.langs.stata]
            [cljsjs.highlight.langs.step21]
            [cljsjs.highlight.langs.stylus]
            [cljsjs.highlight.langs.subunit]
            [cljsjs.highlight.langs.swift]
            [cljsjs.highlight.langs.taggerscript]
            [cljsjs.highlight.langs.tap]
            [cljsjs.highlight.langs.tcl]
            [cljsjs.highlight.langs.tex]
            [cljsjs.highlight.langs.thrift]
            [cljsjs.highlight.langs.tp]
            [cljsjs.highlight.langs.twig]
            [cljsjs.highlight.langs.typescript]
            [cljsjs.highlight.langs.vala]
            [cljsjs.highlight.langs.vbnet]
            [cljsjs.highlight.langs.vbscript]
            [cljsjs.highlight.langs.vbscript-html]
            [cljsjs.highlight.langs.verilog]
            [cljsjs.highlight.langs.vhdl]
            [cljsjs.highlight.langs.vim]
            [cljsjs.highlight.langs.x86asm]
            [cljsjs.highlight.langs.xl]
            [cljsjs.highlight.langs.xml]
            [cljsjs.highlight.langs.xquery]
            [cljsjs.highlight.langs.yaml]
            [cljsjs.highlight.langs.zephir]
            [jimw-clj.edit :as edit]
            [jimw-clj.edit-md :as edit-md]
            [jimw-clj.todos :as todos]
            [alandipert.storage-atom :refer [local-storage]]
            [myexterns.viz]
            [myexterns.wordcloud]
            [myexterns.markjs]
            [re-frame.core :as re-frame]
            [jimw-clj.events :as msg-events]
            [jimw-clj.subs :as subs]
            [jimw-clj.views :as views]
            [re-frame.core :as re-frame]
            [jimw-clj.something :as something]
            cljsjs.clipboard)
  (:import goog.History))

(defn clipboard-button [label target]
  (let [clipboard-atom (atom nil)]
    (r/create-class
     {:display-name "clipboard-button"
      :component-did-mount
      #(let [clipboard (new js/Clipboard (r/dom-node %))]
         (reset! clipboard-atom clipboard)
         #_(debugf "Clipboard mounted"))
      :component-will-unmount
      #(when-not (nil? @clipboard-atom)
         (.destroy @clipboard-atom)
         (reset! clipboard-atom nil)
         #_(debugf "Clipboard unmounted"))
      :reagent-render
      (fn []
        [:button.clipboard
         {:data-clipboard-target target}
         label])})))

(declare blog-list)

(defn json-parse
  [json]
  (->
   (.parse js/JSON json)
   (js->clj :keywordize-keys true)))

(declare pcm-ip)

(re-frame/reg-event-db
 :msg/push-all
 (fn [db [_ {:keys [msgs]}]]
   #_(prn (str "----" msgs))
   ;;
   (let [{:keys [kind table columnnames columnvalues oldkeys]} (first (:change (json-parse msgs)))
         {:keys [id blog parid content created_at updated_at done
                 sort_id wctags app_id file islast percent begin mend origin_content]}
         (zipmap (map keyword columnnames) columnvalues)]
     (cond
       ;; 1. todos表的Websocket的同步
       (= table "todos")
       ;;(prn (str "------" content))
       (cond (= kind "insert")
             ;;
             (do (prn (str id "------insert" content))
                 (swap! blog-list update-in
                        [blog :todos]
                        #(assoc % sort_id {:id sort_id :sort_id id
                                           :search true                                                   
                                           :parid parid
                                           :content content}))
                 )
             ;;
             (= kind "update")
             (do (prn (str id "------update" content))
                 (swap! blog-list update-in [blog :todos sort_id :content] (fn [x] content))
                 (swap! blog-list update-in [blog :todos sort_id :done] (fn [x] done))
                 )             
             (= kind "delete")
             (do (prn
                  (str
                   (first (:keyvalues oldkeys)) "------delete" content))                 
                 #_(swap! blog-list update-in
                          [blog :todos] #(dissoc % (first (:keyvalues oldkeys))))
                 )
             :else (prn "todos other operation"))

       ;; 2. pcmip表的Websocket的同步
       (= table "pcmip")
       (reset! pcm-ip (second columnvalues))
       ;; 3. 其他表的更新
       :else (prn (str table " update"))
       )
     )
   ;;
   #_(assoc db :msgs msgs)
   ))

(.setOptions js/marked
             (clj->js
              {:table true
               :highlight #(.-value (.highlightAuto js/hljs %))}))

(defn api-root [url] (str (-> js/window .-location .-origin) url))
(defn s-height [] (.. js/document -body -scrollHeight))

(defn s-top [] (.. js/document -body -scrollTop))
(defn sd-top [] (.. js/document -documentElement -scrollTop))
(defn ss-top [] (.. js/window -pageYOffset))

(defn o-height [] (.. js/document -body -offsetHeight))

(defn is-page-end []
  (<=
   (- (s-height)
      (ss-top) #_(s-top))
   (o-height)))

(defn is-page-end-m-pc []
  (>=
   (+ (ss-top) (o-height) 60)
   (s-height)))

;;
#_(let [results {:A 1 :B 2 :C 2 :D 5 :E 1 :F 1}]
    (into (sorted-map-by (fn [key1 key2]
                           (compare (get results key2)
                                    (get results key1))))
          results))
;;=> 尾实体 {:D 5, :B 2, :A 1}

;; H头实体: {:A [11 1] :B [22 8] :C [33 2] :D [44 5] :E [55 7] :F [66 10]}
;; R符合关系映射: into-sorted-map-by-fn
#_(let [results {:A [11 1] :B [22 8] :C [33 2] :D [44 5] :E [55 7] :F [66 10]}]
    (into (sorted-map-by (fn [key1 key2]
                           (prn (str key1 "-----" key2))
                           (prn (str (get results key1) "======" (get results key2)))
                           (compare (last (get results key2))
                                    (last (get results key1)))))
          results))
;; =>T尾实体 {:F [66 10], :B [22 8], :E [55 7], :D [44 5], :C [33 2], :A [11 1]}

(defonce page-offset (r/atom 0))
(defonce blog-list-bak (r/atom {}))
(defonce blog-list (r/atom
                    ;;(sorted-map-by >)
                    (sorted-map-by (fn [key1 key2]
                                       (let [fblog (:unix_time (get @blog-list-bak key1))
                                             bblog (:unix_time (get @blog-list-bak key2))]
                                         (compare bblog fblog)
                                         )
                                       )
                                     )
                    )) ;;=> 1111 ,2222, 3333

#_(let [results @blog-list]  
    (map #(-> % last :name)
         (into (sorted-map-by (fn [key1 key2]
                                (let [fblog (:unix_time (get results key1))
                                      bblog (:unix_time (get results key2))]
                                  (compare bblog fblog)
                                  )
                                )
                              )
               results))
    ) ;;=> ("33333333333333" "22222222222" "111111111")
      
(defn get-todo-root-id [blog-id]
  (-> (filter #(= (:parid (last %)) 1)
              (:todos (@blog-list blog-id)))
      first
      last
      :sort_id))

(def api-token (local-storage (r/atom "") :api-token))
(def pcm-ip (local-storage (r/atom "0.0.0.0") :pcm-ip))

(defonce search-key (r/atom ""))

(defonce search-viz-en (r/atom (sorted-map-by >)))
(defonce search-wolframalpha-en (r/atom {}))
(defonce focus-bdsug-blog-id (r/atom nil))

;; (viz-string "digraph { a -> b; }")
;; Chrome: jimw_clj.core.viz_string("digraph { a -> b; }")
;; (let [graph (.querySelector js/document "#gv-output-9845")] (.appendChild graph (viz-string "digraph { a -> b; }")))
(defn viz-string
  [st]
  (.-documentElement
   (.parseFromString
    (js/DOMParser.)
    (->
     (js/Viz st)
     (clojure.string/replace-first #"width=\"\d+pt\"" "")
     (clojure.string/replace-first #"height=\"\d+pt\"" ""))
    "image/svg+xml")))

(defonce source-names (r/atom []))
(defonce active-source (r/atom "BLOG"))

(defonce source-names-list-init
  (go (let [response
            (<!
             (http/get (api-root "/source-nams")
                       {:with-credentials? false
                        :headers {"jimw-clj-token" @api-token}
                        :query-params {}}))]
        (let [data (:body response)]
          (reset! active-source (first data))
          (reset! source-names data)))))

(defn select-source-page []
  (let [page
        [:select#select-source
         {:on-change
          (fn [e]
            (let [selected-val (.-value (. js/document (getElementById "select-source")))]
              (reset! active-source selected-val)))
          :value @active-source}
         (map
          (fn [opt] [:option {:value opt} opt])
          @source-names)]]
    page))

;;
(defonce project-names (r/atom []))
(defonce active-project (r/atom ""))

(defonce project-names-list-init
  (go (let [response
            (<!
             (http/get (api-root "/project-nams")
                       {:with-credentials? false
                        :headers {"jimw-clj-token" @api-token}
                        :query-params {}}))]
        (let [data (:body response)]
          (reset! active-project (first data))
          (reset! project-names data)))))

(defn select-project-page []
  (let [page
        [:select#select-project
         {:on-change
          (fn [e]
            (let [selected-val (.-value (. js/document (getElementById "select-project")))]
              (reset! active-project selected-val)))
          :value @active-project}
         (map
          (fn [opt] [:option {:value opt} opt])
          @project-names)]]
    page))
;;

(defn login
  [username password op-fn]
  (go (let [response
            (<!
             (http/post (api-root "/login")
                       {:with-credentials? false
                        :query-params {:username username :password password}}))]
        (let [data (:body response)]
          (op-fn data)))))

(defn get-blog-list
  [q offset op-fn]
  (go (let [{:keys [status body]}
            (<!
             (http/get (api-root "/blogs")
                       {:with-credentials? false
                        :headers {"jimw-clj-token" @api-token}
                        :query-params
                        (merge {:q q :limit 5
                                :offset (* offset 5)
                                :source @active-source}
                               (if (and (seq @active-project)
                                        (or (= @active-source "SEMANTIC_SEARCH") (= @active-source "REVERSE_ENGINEERING")))
                                 {:project @active-project}
                                 {}))}))]
        (if (= status 200)
          (op-fn body)
          (js/alert "Unauthorized !")))))

(defn update-blog
  [id name content op-fn]
  (go (let [{:keys [status body]}
            (<!
             (http/put (str (api-root "/update-blog/") id)
                       {:headers {"jimw-clj-token" @api-token}
                        :json-params
                        {:name name :content content}}))]
        (if (= status 200)
          (op-fn body)
          (js/alert "Unauthorized !")))))

(defn create-default-blog
  [op-fn]
  (go (let [response
            (<!
             (http/post (api-root "/create-blog")
                        {:headers {"jimw-clj-token" @api-token}
                         :json-params
                         {:name (str "给我一个lisp的支点" (js/Date.now)) :content "### 我可以撬动整个地球!"}}))]
        (let [data (:body response)]
          (swap! blog-list assoc (:id data)
                 {:id (:id data) :content (:content data) :name (:name data)
                  :todos (sorted-map-by >)})
          (op-fn)))))

(def swap-blog-list
  (fn [data]
    (->
     (map (fn [li]
            (do
              (swap! blog-list-bak assoc (:id li)
                     {:unix_time (:unix_time li)})
              (swap! blog-list assoc (:id li)
                     {:id (:id li) :name (:name li) :content (:content li)
                      :unix_time (:unix_time li)
                      :stags (:stags li)
                      :todos (into
                              (sorted-map-by >)
                              (map (fn [x] (vector (:id x)
                                                   (merge x {:search true})
                                                   ;;x
                                                   )) (:todos li)))})
              (:id li))) data) str prn)))

(defonce blog-list-init
  (get-blog-list "" @page-offset
                 (fn [data] (swap-blog-list data))))

(def is-end (atom true))

(set!
 js/window.onscroll
 #(if (and (is-page-end-m-pc) @is-end (= (session/get :page) :home))
    (do
      (swap! page-offset inc)
      (reset! is-end false)
      (get-blog-list @search-key @page-offset
                     (fn [data]
                       (swap-blog-list data)
                       (reset! is-end true))))
    nil))

;; 无效的两个函数
(set!
 js/window.onbeforeunload
 #(do
    (js/alert 312321321)
    )
 )

(set!
 js/window.onunload
 #(do
    (js/alert 32132111111111)
    )
 )

(declare record-event)
(declare searchbar-mode)

;; (get-selector-current-blog-id (.getSelection js/window)) ;; => "current-blog-id-61016"
(defn get-selector-current-blog-id [selector]
  (let [bp-ele (-> selector .-baseNode .-parentElement)]
    ((fn [n]
       (loop [cnt n]
         (if (re-matches #"current-blog-id-(\d+)" (.-id cnt))
           (.-id cnt)
           (recur (.-parentElement cnt))))) bp-ele)))

(defn get-url-params []
  (let [stri (str (.-search js/location))]
    (if (empty? stri)
      {}
      (->>
       (->
        stri
        (clojure.string/replace-first "?" "")
        (clojure.string/split #"&"))
       (map
        (fn [stri]
          (clojure.string/split stri "=")))
       (into {})))))

;; 找不到prn
#_(when (.-body js/document)  
    (js/alert (.-search js/location))
    )

(declare get-selector-current-blog-id)

(set!
 (.-onload js/window)
 (fn []
   (prn (str "=======" (.-search js/location)))
   (if (re-matches #"\?google=(.*)" (str (.-search js/location)))
     (let [q-hash (get-url-params)
           stri (js/decodeURI (get q-hash "google"))]
       ;;(reset! searchbar-mode false)
       ;;(set! (.-value (.getElementById js/document "google-input")) stri) ;; 这一行设置无效!
       (record-event "search-google-event" stri identity)
       ;;(.click (.getElementById js/document "google-input-button"))
       (.open js/window (str "https://www.google.com/search?q=" stri))
       )
     nil)
   ;;
   )
 )

(declare ctrlkey-todo-node-select-edit)

(declare get-selector-stri-and-anchor-stri)
(declare set-color)

(set!
 js/window.onmouseup
 (fn [e]
   (if (empty? (.toString (.getSelection js/window)))
     nil
     #_(prn (get-selector-stri-and-anchor-stri))
     ;; (set-color) ;; OLD: 只能支持单个element的上色
     (something/highlight "" "red" (.getRangeAt (.getSelection js/window) 0))
     )
   )
 )

;; TODOS: Emacs 的键位设计用在CLJS身上
(set!
 js/window.onkeydown
 (fn [e]
   (let [keycode (.-keyCode e)
         ;; 0~9 => 48~57
         ctrlkey (.-ctrlKey e)
         ;; true or false
         metakey (.-metaKey e)]
     ;; Ctrl的组合键
     (if (and ctrlkey (not= keycode 17))
       (cond
         ;; Ctrl+数字键: 可以用于CLJS输入法
         ((set (range 47 58)) keycode)
         (let [key-num (- keycode 48)
               content (get @search-wolframalpha-en (keyword (str key-num)))]
           (prn (str "数字键" key-num ", " content))
           (something/copyToClipboard content))
         (= 71 keycode) ;; C-g键位
         (let [selector (.getSelection js/window)
               select-stri (.toString selector)]
           (reset! searchbar-mode false)
           (set! (.-value (.getElementById js/document "google-input")) (str select-stri))
           (record-event "search-google-event" (str select-stri) identity
                         (clojure.string/replace (get-selector-current-blog-id selector)
                                                 "current-blog-id-" ""))
           (.click (.getElementById js/document "google-input-button"))
           )
         ;; TODO: 代码语义搜索的search记录
         ;; 1. 新开一个页面,然后搜索: 可以直接从url里面传入搜索词和project和source_type
         ;; 2. 可以一个页面控制多个页面: 通过中间的Websocket和pg streaming来实现
         (= 83 keycode) ;; C-s键位
         (prn 1111111)
         (= 84 keycode) ;; C-t键位: 直接加入C-g的搜索标签,避免搜索跳转
         (record-event "search-google-event" (str (-> js/window .getSelection .toString)) identity
                       (clojure.string/replace (get-selector-current-blog-id (.getSelection js/window))
                                               "current-blog-id-" ""))
         (= 86 keycode) ;; C-v键位 来todos树上跳转编辑列表
         (ctrlkey-todo-node-select-edit)
         ;;
         :else (prn keycode))
       nil)
     ;; Meta的组合键TODO
     )
   )
 )

(defn nav-link [uri title page collapsed?]
  [:li.nav-item
   {:class (when (= page (session/get :page)) "active")}
   [:a.nav-link
    {:href uri
     :on-click #(do
                  (if (= page :show)
                    (set! (.-display (.-style (. js/document (getElementById "wordcloud")))) "block")
                    (set! (.-display (.-style (. js/document (getElementById "wordcloud")))) "none"))
                  (cond (= page :create-blog)
                        (create-default-blog
                         (fn [] (set! (.. js/window -location -href) (api-root ""))))
                        (= page :logout) (reset! api-token "")
                        :else
                        (reset! collapsed? true)))} title]])

;; TODO: record-event改成通用的keys参数编写规范
(defn record-event
  [event_name event_data op-fn & blog-id]
  (go (let [response
            (<!
             (http/post (api-root "/record-event")
                        {:headers {"jimw-clj-token" @api-token}
                         :json-params
                         (if blog-id
                           {:event_name event_name :event_data event_data :blog (first blog-id)}
                           {:event_name event_name :event_data event_data})}))]
        (let [data (:body response)]
          (op-fn data)))))

(defn search-map-zh2en
  [q op-fn]
  (go (let [{:keys [status body]}
            (<!
             (http/get (api-root "/search-map-zh2en")
                       {:with-credentials? false
                        :headers {"jimw-clj-token" @api-token}
                        :query-params {:q q}}))]
        (if (= status 200)
          (op-fn (:data body))
          (js/alert "Unauthorized !")))))

(defonce searchbar-mode (atom true))
  
(defn searchbar []
  (let [search-str (r/atom "")
        google-q (r/atom "")
        github-q (r/atom "")
        youtube-q (r/atom "")
        wolfram-alpha-q (r/atom "")
        pcm-ip-txt (r/atom "")
        search-fn (fn []
                    (do
                      (reset! blog-list
                              (sorted-map-by (fn [key1 key2]
                                               (let [fblog (:unix_time (get @blog-list-bak key1))
                                                     bblog (:unix_time (get @blog-list-bak key2))]
                                                 (compare bblog fblog)
                                                 )
                                               )
                                             )
                              #_(sorted-map-by >))
                      (reset! page-offset 0)
                      (reset! search-key @search-str)
                      (get-blog-list
                       @search-str @page-offset
                       (fn [data]
                         (swap-blog-list data)))
                      (set! (.-title js/document) @search-str)
                      (record-event "search-blog-event" @search-str identity)))
        append-stri (r/atom "")]
    (fn []
      [:div
       [:div#adv-search.input-group.search-margin
        [:input {:type "text", :class "form-control", :placeholder "Search for blogs"
                 :on-change #(reset! search-str (-> % .-target .-value))
                 :on-key-down #(case (.-which %)
                                 13 (search-fn)
                                 nil)}]
        [:div {:class "input-group-btn"}
         [:div {:class "btn-group", :role "group"}
          [:div {:class "dropdown dropdown-lg"}]
          [:button {:type "button", :class "btn btn-primary"
                    :on-click search-fn}
           [:span {:class "glyphicon glyphicon-search", :aria-hidden "true"}]]]]]
       ;;
       [:form {:target "_blank", :action "http://www.google.com/search", :method "get"} 
        [:input {:id "google-input"
                 :type "text"
                 :on-change #(do
                               (reset! searchbar-mode true)
                               (reset! google-q (-> % .-target .-value)))
                 :on-key-down #(case (.-which %)
                                 13 (if (and (not-empty @google-q) @searchbar-mode)
                                      (record-event "search-google-event" @google-q identity)
                                      nil)
                                 nil)
                 :on-blur #(reset! searchbar-mode false)
                 :name "q"}] 
        [:input {:type "submit", :value "Google"
                 :id "google-input-button"
                 #_:on-click #_(if (and (not-empty @google-q) @searchbar-mode)
                              (record-event "search-google-event" @google-q identity)
                              nil)}]]
       ;;
       [:div.viz-container
        [:div#adv-search.input-group.search-margin
         [:form {:target "_blank", :action "https://www.wolframalpha.com/input", :method "get"}
          [:input {:id "wolfram-alpha-input"
                   :type "text"
                   :on-change #(do
                                 (reset! wolfram-alpha-q (-> % .-target .-value))
                                 (search-map-zh2en @wolfram-alpha-q (fn [data] (reset! search-wolframalpha-en data))))
                   :on-key-down #(case (.-which %)
                                   13 (do (prn 111) (record-event "search-wolfram-alpha-event" @wolfram-alpha-q identity))
                                   nil)
                   :name "i"}]
          [:input {:type "submit", :value "WolframAlpha"
                   :on-click #(record-event "search-wolfram-alpha-event" @wolfram-alpha-q identity)}]]
         (let [alpha-input (.getElementById js/document "wolfram-alpha-input")
               google-input (.getElementById js/document "google-input")]
           [:ul
            (if (nil? @focus-bdsug-blog-id)
              (for [item @search-wolframalpha-en]
                [:li {:on-click #(do (reset! append-stri (str (last item)))
                                     (set! (.-value alpha-input) (str  (.-value alpha-input) " "  (str (last item))))
                                     (set! (.-value google-input) (str  (.-value google-input) " "  (str (last item))))
                                     )}
                 (str (first item) ". " (last item))]
                ))
            ]
           )
         ]]
       ;;[:p]
       [:form {:target "_blank", :action "https://github.com/search?utf8=✓", :method "get"} 
        [:input {:type "text"
                 :on-change #(reset! github-q (-> % .-target .-value))
                 :on-key-down #(case (.-which %)
                                 13 (record-event "search-github-event" @github-q identity)
                                 nil)
                 :name "q"}] 
        [:input {:type "submit" :value "Github"
                 :on-click #(record-event "search-github-event" @github-q identity)}]]
       [:p]
       [:form {:target "_blank", :action "https://www.youtube.com/results", :method "get"}
        [:input {:type "text"
                 :on-change #(reset! youtube-q (-> % .-target .-value))
                 :on-key-down #(case (.-which %)
                                 13 (record-event "search-youtube-event" @youtube-q identity)
                                 nil)
                 :name "search_query"}]
        [:input {:type "submit" :value "Youtube"
                 :on-click #(record-event "search-youtube-event" @youtube-q identity)}]]
       ;;
       [:h6 "pcm ip: " @pcm-ip ", "
        [:a {:id "download-api-token" :href (str "data:text/plain," @api-token) :download "api-token.txt" :target "_blank"} "token"]]
       [:input {:type "text"
                :value @pcm-ip-txt
                :on-change #(reset! pcm-ip-txt (-> % .-target .-value))
                :on-key-down #(case (.-which %)
                                13 (do (reset! pcm-ip @pcm-ip-txt)
                                       (js/alert (str "更新pcm播放地址为" @pcm-ip)))
                                nil)}]
       [:p]
       [select-source-page]
       (if (or (= @active-source "SEMANTIC_SEARCH") (= @active-source "REVERSE_ENGINEERING"))
         [select-project-page])
       [:p]
       ])))

(defn navbar []
  (let [collapsed? (r/atom true)]
    (fn []
      [:nav.navbar.navbar-dark.bg-primary
       [:button.navbar-toggler.hidden-sm-up
        {:on-click #(swap! collapsed? not)} "☰"]
       [:div.collapse.navbar-toggleable-xs
        (when-not @collapsed? {:class "in"})
        [:a.navbar-brand {:href "#/"} "jimw-clj"]
        [:ul.nav.navbar-nav
         [nav-link "#/" "Home" :home collapsed?]
         [nav-link "#/show" "Show" :show collapsed?]
         [nav-link "#/viz" "Viz" :viz collapsed?]
         [nav-link "#/about" "About" :about collapsed?]
         [nav-link "#/" "New" :create-blog collapsed?]
         [nav-link "#/logout" "Logout" :logout collapsed?]]]])))

(defn about-page []
  [:div.container.about-margin
   [:div.row
    [:div.col-sm-4
     [:img.steve-chan-img {:src "/img/steve-chan.jpeg"}]]
    [:div.col-sm-6
     [:h1 "About Steve Chan"]
     [:p "My name is Steve Chan and I'm a Clojure/R/ELisp/Ruby hacker from BeiJing, China."]
     [:p "I love Wing Chun. In the Source I trust !"]
     [:h4 "features"]
     [:li "用机器学习来机器学习"]
     [:li "用GraphViz树来做决策树训练工具"]
     [:li "用标签云来做贝叶斯训练工具"]
     [:li "整合手机APP及微信浏览器数据流"]
     [:li "整合Chrome插件数据流"]
     [:li "整合Emacs数据流"]
     [:li "整合输入法数据流"]
     [:li "整合咏春训练数据流"]]]])

(defn logout-page []
  (let [username (r/atom "")
        password (r/atom "")]
    [:div {:class "main-login main-center"}
     [:form {:class "form-horizontal", :method "post", :action "#"}
      [:div {:class "form-group"}
       [:label {:for "name", :class "cols-sm-2 control-label"} "Your Name"]
       [:div {:class "cols-sm-10"}
        [:div {:class "input-group"}
         [:span {:class "input-group-addon"}
          [:i {:class "fa fa-user fa", :aria-hidden "true"}]]
         [:input {:type "text", :class "form-control", :name "name", :id "name", :placeholder "Enter your Name"
                  :on-change #(reset! username (-> % .-target .-value))}]]]]
      [:div {:class "form-group"}
       [:label {:for "confirm", :class "cols-sm-2 control-label"} "Confirm Password"]
       [:div {:class "cols-sm-10"}
        [:div {:class "input-group"}
         [:span {:class "input-group-addon"}
          [:i {:class "fa fa-lock fa-lg", :aria-hidden "true"}]]
         [:input {:type "password", :class "form-control", :name "confirm", :id "confirm", :placeholder "Confirm your Password"
                  :on-change #(reset! password (-> % .-target .-value))}]]]]
      [:div {:class "form-group"}
       [:button
        {:type "button", :class "btn btn-primary btn-lg btn-block login-button"
         :on-click
         (fn []
           (login
            @username @password
            (fn [data]
              (if (:token data)
                (do
                  (js/alert "login success!")
                  (reset! api-token (:token data))
                  (set! (.. js/window -location -href) (api-root ""))
                  (go (async/<! (async/timeout 2000))
                      (.click (. js/document (getElementById "download-api-token")))))
                (js/alert "username or password is error!")))))} "Login"]]]]))

(defn blog-name-save [id name]
  (do
    (swap! blog-list assoc-in [id :name] name)
    (update-blog id name nil #(prn %))))

(defn blog-content-save [id content]
  (do
    (swap! blog-list assoc-in [id :content] content)
    (update-blog id nil content #(prn %))))

(defn get-digraph
  [blog op-fn]
  (go (let [{:keys [status body]}
            (<!
             (http/get (api-root (str "/todos-" blog ".gv"))
                       {:with-credentials? false
                        :headers {"jimw-clj-token" @api-token}
                        :query-params {}}))]
        (if (= status 200)
          (op-fn body)
          (js/alert "Unauthorized !")))))

(defn tree-todo-generate [blog]
  (go (let [response
            (<!
             (http/post (api-root "/tree-todo-generate")
                        {:with-credentials? false
                         :headers {"jimw-clj-token" @api-token}
                         :query-params {:blog blog}}))])))

(defn tree-todo-generate-new
  [blog op-fn]
  (go (let [{:keys [status body]}
            (<!
             (http/post (api-root "/tree-todo-generate-new")
                        {:with-credentials? false
                         :headers {"jimw-clj-token" @api-token}
                         :query-params {:blog blog}}))]
        (if (= status 200)
          (op-fn (:data body))
          (js/alert "Unauthorized !")))))

(defn qrcode-generate
  [blog op-fn]
  (go (let [{:keys [status body]}
            (<!
             (http/post (api-root "/qrcode-generate")
                        {:with-credentials? false
                         :headers {"jimw-clj-token" @api-token}
                         :query-params {:blog blog}}))]
        (if (= status 200)
          (op-fn body)
          (js/alert "Unauthorized !")))))

(defn search-sqldots
  [q op-fn]
  (go (let [{:keys [status body]}
            (<!
             (http/get (api-root "/search-sqldots")
                       {:with-credentials? false
                        :headers {"jimw-clj-token" @api-token}
                        :query-params {:q q}}))]
        (if (= status 200)
          (op-fn (:data body))
          (js/alert "Unauthorized !")))))

(defn get-blog-wctags
  [id op-fn scaling show-count]
  (go (let [{:keys [status body]}
            (<!
             (http/get (api-root "/get-blog-wctags")
                       {:with-credentials? false
                        :headers {"jimw-clj-token" @api-token}
                        :query-params {:id id}}))]
        (if (= status 200)
          (op-fn (vec (map (fn [item] (vector (name (first item)) (* (last item) scaling)))
                           (take show-count (:data body)))))
          (js/alert "Unauthorized !")))))

(defn md-render [id name content stags]
  (let [editing (r/atom false)]
    [:div.container {:id (str "current-blog-id-" id)}
     [:div.row>div.col-sm-12
      [edit/blog-name-item {:id id :name name :save-fn blog-name-save}]
      [:p (str "搜索标签: " (clojure.string/join " | " stags))]
      [edit-md/blog-content-item {:id id :name content :save-fn blog-content-save}]
      [todos/todo-app blog-list id search-wolframalpha-en focus-bdsug-blog-id]
      [:div
       ;; 打开本地的Viz临时使用局部树
       [:button.btn.tree-btn
        {:on-click
         #(do (js/alert "Update...")
              (tree-todo-generate id))} "Generate"]
       [:a.btn.margin-download
        {:href (str "/todos-" id ".gv")
         :download (str "past_" id "_navs.zip")} "Download"]
       [:button.btn.margin-download
        {:on-click #(let [graph (.querySelector js/document (str "#gv-output-" id))
                          svg (.querySelector graph "svg")]
                      (do
                        (if svg (.removeChild graph svg) ())
                        (get-digraph id
                                     (fn [digraph-str]
                                       (.appendChild
                                        graph
                                        (viz-string digraph-str))))))} "Viz"]
       ;; 
       ;; 生产环境测试viz.js已ok
       [:button.btn.margin-download
        {:on-click #(let [graph (.querySelector js/document (str "#gv-output-" id))
                          svg (.querySelector graph "svg")]
                      (do
                        (if svg (.removeChild graph svg) ())
                        (tree-todo-generate-new
                         id
                         (fn [digraph-str]
                           (.appendChild
                            graph
                            (viz-string digraph-str))))))} "NewViz"]
       [:button.btn.margin-download
        {:on-click #(let [elem (.getElementById js/document (str "wordcloud-" id))]
                      (set! (.-display (.-style elem)) "block")
                      (get-blog-wctags
                       id
                       (fn [wctags]
                         (window.WordCloud
                          elem
                          (clj->js
                           {:list wctags}))) 5 30))} "WordCloud"]
       [:button.btn.margin-download
        {:on-click #(qrcode-generate
                     id
                     (fn [data]
                       (let [img-ele (.createElement js/document "img")
                             qrcode-div (.querySelector js/document (str "#qrcode-" id))]
                         (set! (.-src img-ele ) (str "/qrcode/" (:file data)))
                         (.appendChild qrcode-div img-ele)
                         )))
         } "QRCode"]
       [:button.btn.margin-download
        {:on-click #(do
                      (something/copyToClipboard (str "{\"todo-root-id\":" (get-todo-root-id id) ",\"blog-id\":" id "}"))
                      (js/alert "已复制二维码信息"))}
        "CpQRurl"]
       ]
      [:br]
      [:div.gvoutput {:id (str "gv-output-" id)}]
      [:canvas.wcanvas {:id (str "wordcloud-" id)}]
      [:div {:id (str "qrcode-" id) :style {:width "20%"}}]
      [:hr]]]))

#_(defn home-page []
  [:div [:h2 "Welcome to clipboard-test"]
   [:div {:id "copy-this"} "Testing"]
   [clipboard-button "Copy" "#copy-this"]
   [:div [:a {:href "/about"} "go to about page"]]])

(defn home-page []
  [:div.container.app-margin
   (if (seq @api-token)
     (for [blog @blog-list]
       [:div
        (md-render
         (:id (last blog))
         (:name (last blog))
         (:content (last blog))
         (:stags (last blog)))])
     [:h3.please-login "please login"])])

(defn word-cloud-did-mount [this]
  (get-blog-wctags
   25125
   (fn [wctags]
     (window.WordCloud
      (r/dom-node this)
      (clj->js { :list wctags })
      #(do
         (js/alert %)))) 50 30))

(defn word-cloud-create-class []
  (r/create-class {:reagent-render
                   (fn []
                     [:div
                      {:style
                       {:height    "2500px"
                        :margin    "0 auto"}}])
                   :component-did-mount
                   (fn [this]
                     (word-cloud-did-mount this))}))

(defn update-wordcloud-component []
  (set! (.-innerHTML (. js/document (getElementById "wordcloud"))) "")
  (r/render-component [word-cloud-create-class]
                      (. js/document (getElementById "wordcloud"))))

(defonce scaling (r/atom 30))
(defonce show-count (r/atom 50))

(defn show-page []
  [:div.container.app-margin
   [:div.row
    [:div.col-sm-2
     [:h6 "文章"]
     [:input {:type "number"}]]
    [:div.col-sm-5
     [:h6 "放大倍数 " @scaling]
     [:input {:type "range" :min 5 :max 50
              :style {:width "100%"}
              :on-change (fn [e] (reset! scaling (.. e -target -value)))}]
     [:h6 "最大显示词数量 " @show-count]
     [:input {:type "range" :min 10 :max 100
              :style {:width "100%"}
              ;;:show-count @show-count
              :on-change (fn [e] (reset! show-count (.. e -target -value)))}]]
    [:div.col-sm-2
     [:button.btn.btn-primary "Generate"]]]
   [:h1 
    {:on-click #(do
                  (js/alert (str "====" @scaling "====" @show-count))
                  (update-wordcloud-component))
     }
    "."
    ]
   ]
  )

(defonce search-viz-str (atom ""))

(defn search-mapen 
  [q op-fn]
  (go (let [{:keys [status body]}
            (<!
             (http/get (api-root "/search-mapen")
                       {:with-credentials? false
                        :headers {"jimw-clj-token" @api-token}
                        :query-params {:q q}}))]
        (if (= status 200)
          (op-fn (:data body))
          (js/alert "Unauthorized !")))))

(defonce mapen-show (atom ""))

(defn viz-page []
  (let [viz-fn #(let [graph (.querySelector js/document "#gv-output-sql")
                      svg (.querySelector graph "svg")]
                  (do
                    (if svg (.removeChild graph svg) ())
                    (search-sqldots
                     %
                     (fn [digraph-str]
                       (.appendChild
                        graph
                        (viz-string digraph-str))))))]
    [:div.viz-container
     #_[:div.viz-search-logo
        [:h2 "Viz"]]
     [:div#adv-search.input-group.search-margin
      [:input {:type "text", :class "form-control", :placeholder "Search"
               :on-change #(do
                             (reset! search-viz-str (-> % .-target .-value))
                             (search-mapen @search-viz-str (fn [data] (reset! search-viz-en data))))
               :on-key-down #(case (.-which %)
                               13 (viz-fn @search-viz-str)
                               nil)}]
      [:ul
       (for [item @search-viz-en]
         [:li (str (last item))])]
      [:div {:class "input-group-btn"}
       [:div {:class "btn-group", :role "group"}
        [:div {:class "dropdown dropdown-lg"}]
        #_[:button {:type "button", :class "btn btn-primary"
                    :on-click #(viz-fn @search-viz-str)}
           [:span {:class "glyphicon glyphicon-search", :aria-hidden "true"}]]]]]
     [:br]
     [:div.gvoutput {:id "gv-output-sql"}]]))

;; 新增路由区域, 配合navbar使用
(def pages
  {:home #'home-page
   :about #'about-page
   :logout #'logout-page
   :show #'show-page
   :viz #'viz-page})

(defn page []
  [(pages (session/get :page))])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :page :home))

(secretary/defroute "/about" []
  (session/put! :page :about))

(secretary/defroute "/logout" []
  (session/put! :page :logout))

(secretary/defroute "/show" []
  (session/put! :page :show))

(secretary/defroute "/viz" []
  (session/put! :page :viz))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     HistoryEventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app

(defn mount-components []
  (r/render [#'navbar] (.getElementById js/document "navbar"))
  (r/render [#'searchbar] (.getElementById js/document "searchbar"))
  (r/render [#'page] (.getElementById js/document "app"))
  (r/render [views/main-view] (.getElementById js/document "msg")))

(defn init! []
  ;;(load-interceptors!)
  (hook-browser-navigation!)
  (re-frame/dispatch-sync [:db/initialize])
  (re-frame/dispatch-sync [:sente/connect])
  (mount-components))


#_(for [item (get-in @blog-list [37581 :todos])]
  (do
    (let [{:keys [content id] :as todo} (last item)]
      (prn content)
      )
    )
  )


#_(swap! @blog-list update-in [37581 :todos]                            
       ;;@(get-in @blog-list [37581 :todos])
       ;;(sorted-map)
       (fn [x] (sorted-map))
       )

;; (swap! @blog-list update-in [37581 :todos 228 :content] (fn [x] "0000" ))

;; (something/hello) ;; => "Hey there from example.something JavaScript"
;; (something/getSelectionEndPosition) ;;  => #js {:x 246.125, :y 458}
;; (something/copyToClipboard "aaaaaa") <=> jimw_clj.something.copyToClipboard("aaaaaaadsadsa")

(def google-input-html
  "<form target=\"_blank\" action=\"http://www.google.com/search\" method=\"get\"><input type=\"text\" id=\"google-input\" name=\"q\"><input type=\"submit\" value=\"Google\" id=\"google-input-button\"></form>")

;; 给任意网页body后面加一个google搜索:
;; Chrome测试: b=new DOMParser().parseFromString("<form target=\"_blank\" action=\"http://www.google.com/search\" method=\"get\"><input type=\"text\" id=\"google-input\" name=\"q\"><input type=\"submit\" value=\"Google\" id=\"google-input-button\"></form>", 'text/html').body
;; document.body.appendChild(b.firstElementChild)
;; (body-append-html-stri google-input-html)
(defn body-append-html-stri [html]
  (.appendChild
   (.-body
    js/document)
   (-> (js/DOMParser.)
       (.parseFromString     
        html "text/html")
       .-body
       .-firstElementChild)))

;;(cljs.reader/read-string "(+ 1 2)")
#_(prn
 (cljs.reader/read-string
  (str
   "("
   (clojure.string/replace
    (.-textContent (last (array-seq (.-children  (. js/document (getElementById "file-file"))))))
    #"Copy lines\n|Copy permalink\n|View git blame\n|Open new issue\n" "")
   ")")
  ))

;; class
#_(prn
 (cljs.reader/read-string
  (str
   "("
   (clojure.string/replace
    (.-textContent (last (array-seq (.-children
                                     (last (array-seq
                                            (. js/document (getElementsByClassName "file"))))
                                     ))))
    #"Copy lines\n|Copy permalink\n|View git blame\n|Open new issue\n" "")
   ")")
  ))

(comment
  
  (let [selector (.getSelection js/window)
        select-stri (.toString selector)
        origin-text (.-textContent (.-baseNode selector))
        origin-nodename (.-nodeName (.-parentElement (.-baseNode selector)))
        ;; window.getSelection().baseNode.parentElement.outerHTML => "<p>研究方向为自然语言问答
        ;; TODO: 直接暴力replaceHTML替换就好了: 或者是hiccup的递归遍历树然后一次变化结束(支持极其复杂的结构,并且不会重复替换出错)
        origin-html (.-outerHTML (.-parentElement (.-baseNode selector)))
        ]
    ;;select-stri
    ;;(.-nodeName selector)
    
    ;;(.-parentElement (.-baseNode selector)))
    
    )
  
  (defn hello [name]
    [:p (str "Hello " name "!")])
  (hello "Klipse") ;;=> [:p "Hello Klipse!"]
  [hello "Klipse"]
  ;;=> [#object[jimw_clj$core$hello "function jimw_clj$core$hello(name){ return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"p","p",151049309),[cljs.core.str.cljs$core$IFn$_invoke$arity$1("Hello "),cljs.core.str.cljs$core$IFn$_invoke$arity$1(name),cljs.core.str.cljs$core$IFn$_invoke$arity$1("!")].join('')], null); }"] "Klipse"]
  )

;; 无效
#_(when (.getElementById js/document "google-input")
    (prn (.-search js/location))
    )

;; (-> (last (get-viz-select-node-text)) .-parentElement)
(defn get-viz-select-node-text []
  (let [cnt (let [bp-ele (-> (.getSelection js/window) .-baseNode .-parentElement)]
              ((fn [n]
                 (loop [cnt n]
                   (if (re-matches #"node(\d+)" (.-id cnt))
                     cnt
                     (recur (.-parentElement cnt))))) bp-ele))]
    (list (clojure.string/replace 
           (.-textContent
            (first
             (array-seq
              (.-children
               cnt )))) "\n" "")
          cnt)))

;;(recur-match "adsdasdas 123321 dasd 213dasdas" (last (get-viz-select-node-text)))
;; TODO: 根据树的文本去找列表
(defn recur-match [re-text selector]
  (let [bp-ele (-> selector .-parentElement)]
    ((fn [n]
       (loop [cnt n]
         (if (re-matches re-text (clojure.string/replace (.-textContent cnt) "\n" ""))
           cnt
           (recur (.-parentElement cnt))))) bp-ele)))

;; ;;选中任何的地方可以获取todos列表 (get-todos-li-elements)
(defn get-todos-li-elements []
  (array-seq
   (.-children
    (first
     (array-seq
      (.getElementsByClassName
       (. js/document
          (getElementById
           (get-selector-current-blog-id (.getSelection js/window))))
       "todo-list-class"))))))

;; 打印所有的todos的文本信息
#_(map
   #(clojure.string/replace
     (.-textContent %)
     #"copy(\d+)◔" "")
   (get-todos-li-elements))

(defn ctrlkey-todo-node-select-edit-old []
  (.click
   (first
    (array-seq
     (.getElementsByClassName
      (first
       (filter
        #(=
          (clojure.string/replace
           (.-textContent %)
           #"copy(\d+)◔" "")
          (first (get-viz-select-node-text)))
        (get-todos-li-elements)))
      "todo-front-size")))))

(defn ctrlkey-todo-node-select-edit []
  (let [node-name (first (get-viz-select-node-text))
        _ (.info js/console "node-name: " node-name)
        todos-list (get-todos-li-elements)
        _ (.info js/console "todos-list: " todos-list)
        filter-res (filter
                    #(=
                      (clojure.string/replace
                       (.-textContent %)
                       #"copy(\d+)◔" "")
                      node-name)
                    todos-list)
        _ (.info js/console "filter-res: " filter-res)]
    (.click
     (first
      (array-seq
       (.getElementsByClassName
        (first
         filter-res)
        "todo-front-size"))))))

;; 获取选中锚点的信息
;; (get-selector-stri-and-anchor-stri) ;; => ["caRn" "ppcaRnd"]
(defn get-selector-stri-and-anchor-stri-0
  []
  (let [selector (.getSelection js/window)
        select-stri (.toString selector)]
    [select-stri
     (.-textContent (.-anchorNode selector))
     (.-anchorOffset selector)]))

(defn get-selector-stri-and-anchor-stri
  []
  (let [selector (.getSelection js/window)
        select-stri (.toString selector)]
    [select-stri
     (set! (.-innerText
            (.-parentElement
             (.-anchorNode selector))) select-stri)
     (.-anchorOffset selector)]))

;; 双黐手的求微分的思想: 先不要管html的结构的问题,而暴力替换(易), 用λ求近似解, 快速失败 => 过早的完美主义是万恶之源！！!

(defn set-color []
  (let [tr (.getRangeAt (.getSelection js/window) 0)
        span (.createElement js/document "span")]
    (do
      (set! (-> span .-style .-cssText) "color:#ff0000")
      (.surroundContents tr span))))

;; (something/highlight "" "red" (.getRangeAt (.getSelection js/window) 0))



