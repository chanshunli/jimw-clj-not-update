(ns jimw-clj.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [jimw-clj.ajax :refer [load-interceptors!]]
            [ajax.core :refer [GET POST]]
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
            [myexterns.viz])
  (:import goog.History))

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

(defonce page-offset (r/atom 0))
(defonce blog-list (r/atom (sorted-map-by >)))
(def api-token (local-storage (r/atom "") :api-token))
(defonce search-key (r/atom ""))

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
                        :query-params {:q q :limit 5 :offset (* offset 5)}}))]
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
  []
  (go (let [response
            (<!
             (http/post (api-root "/create-blog")
                        {:headers {"jimw-clj-token" @api-token}
                         :json-params
                         {:name (str "给我一个lisp的支点" (js/Date.now)) :content "### 我可以撬动整个地球!"}}))]
        (let [data (:body response)]
          (swap! blog-list assoc (:id data)
                 {:id (:id data) :content (:content data) :name (:name data)
                  :todos (sorted-map-by >)})))))

(def swap-blog-list
  (fn [data]
    (->
     (map (fn [li]
            (do
              (swap! blog-list assoc (:id li)
                     {:id (:id li) :name (:name li) :content (:content li)
                      :todos (into
                              (sorted-map-by >)
                              (map (fn [x] (vector (:id x) x)) (:todos li)))})
              (:id li))) data) str prn)))

(defonce blog-list-init
  (get-blog-list "" @page-offset
                 (fn [data] (swap-blog-list data))))

(def is-end (atom true))

(set!
 js/window.onscroll
 #(if (and (is-page-end-m-pc) @is-end)
    (do
      (swap! page-offset inc)
      (reset! is-end false)
      (get-blog-list @search-key @page-offset
                     (fn [data]
                       (swap-blog-list data)
                       (reset! is-end true))))
    nil))

(defn nav-link [uri title page collapsed?]
  [:li.nav-item
   {:class (when (= page (session/get :page)) "active")}
   [:a.nav-link
    {:href uri
     :on-click #(cond (= page :create-blog) (create-default-blog)
                      (= page :logout-blog) (reset! api-token "")
                      :else
                      (reset! collapsed? true))} title]])

(defn searchbar []
  (let [search-str (r/atom "")]
    (fn []
      [:div#adv-search.input-group.search-margin
       [:input {:type "text", :class "form-control", :placeholder "Search for blogs"
                :on-change #(reset! search-str (-> % .-target .-value))}]
       [:div {:class "input-group-btn"}
        [:div {:class "btn-group", :role "group"}
         [:div {:class "dropdown dropdown-lg"}]
         [:button {:type "button", :class "btn btn-primary"
                   :on-click (fn []
                               (do
                                 (reset! blog-list (sorted-map-by >))
                                 (reset! page-offset 0)
                                 (reset! search-key @search-str)
                                 (get-blog-list
                                  @search-str @page-offset
                                  (fn [data]
                                    (swap-blog-list data)))))}
          [:span {:class "glyphicon glyphicon-search", :aria-hidden "true"}]]]]])))

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
         [nav-link "#/about" "About" :about collapsed?]
         [nav-link "#/" "NewBlog" :create-blog collapsed?]
         [nav-link "#/about" "Logout" :logout-blog collapsed?]]]])))

(defn about-page []
  (if-not (empty? @api-token)
    [:div.container
     [:div.row
      [:div.col-md-12
       [:img {:src (str js/context "/img/warning_clojure.png")}]]]]
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
                    (set! (.. js/window -location -href) (api-root "")))
                  (js/alert "username or password is error!")))))} "Login"]]]])))

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
             (http/get (api-root (str "/todos-" 21170 ".gv"))
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

(defn md-render [id name content]
  (let [editing (r/atom false)]
    [:div.container
     [:div.row>div.col-sm-12
      [edit/blog-name-item {:id id :name name :save-fn blog-name-save}]
      [edit-md/blog-content-item {:id id :name content :save-fn blog-content-save}]
      [todos/todo-app blog-list id]
      [:div
       [:button.btn.tree-btn
        {:on-click
         #(do (js/alert "Update...")
              (tree-todo-generate id))} "Generate"]
       [:a.btn.margin-download
        {:href (str "/todos-" id ".gv")
         :download (str "past_" id "_navs.zip")} "Download"]
       ;; 生产环境测试viz.js已ok
       [:button.btn.margin-download
        {:on-click #(let [graph (.querySelector js/document (str "#gv-output-" id))]
                      (get-digraph id
                                   (fn [digraph-str]
                                     (.appendChild
                                      graph
                                      (viz-string digraph-str)))))} "Viz"]]
      [:br]
      [:div.gvoutput {:id (str "gv-output-" id)}]
      [:hr]
      ;; 移除gv: (let [graph (.querySelector js/document "#gv-output-9845") svg (.querySelector graph "svg")] (if svg (.removeChild graph svg) ()))
      #_[:hr {:align "center" :width "100%" :color "#987cb9" :size "1"}]]]))

(defn home-page []
  [:div.container.app-margin
   (for [blog @blog-list]
     [:div
      (md-render
       (:id (last blog))
       (:name (last blog))
       (:content (last blog)))])])

(def pages
  {:home #'home-page
   :about #'about-page})

(defn page []
  [(pages (session/get :page))])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :page :home))

(secretary/defroute "/about" []
  (session/put! :page :about))

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
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
