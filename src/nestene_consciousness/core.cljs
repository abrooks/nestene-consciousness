(ns nestene-consciousness.core
  (:require [cljs.nodejs :as node]
            [clojure.walk :as walk]
            [cljs.core.async :refer [chan close!]]
            [goog.string :as gstring]
            [goog.string.format :as gformat])
  (:require-macros
   [cljs.core.async.macros :as m :refer [go]]))

(node/enable-util-print!)

(def nest (node/require "unofficial-nest-api/index.js"))
(def fs (node/require "fs"))

(defn env [k]
      (-> (.-env node/process) js->clj (get k)))

(defn timeout [ms]
  (let [c (chan)]
    (js/setTimeout (fn [] (close! c)) ms)
    c))

(defn now-str []
  (let [d (new js/Date)]
    (gstring/format "%04d-%02d-%02d"
            (.getFullYear d)
            (inc (.getMonth d))
            (.getDate d))))

(defn -main []
  (.login nest
          (env "NEST_USERNAME")
          (env "NEST_PASSWORD")
          (fn [err data]
            (if err
              (println "Failure" err)
              (do
                (js/console.log "nest.login:" data)
                (.fetchStatus nest (fn [status] (let [fname (str (now-str) ".edn")
                                                      devs (-> (js->clj status) (get "device") keys)
                                                      subscription (js/JSON.stringify
                                                                    (clj->js
                                                                     {:objects (for [dev devs]
                                                                                 {:object_key (str "energy_latest." dev)})}))]
                                                  (.post nest (clj->js {:path "/v5/subscribe"
                                                                        :body subscription
                                                                        :headers {"X-nl-subscribe-timeout" 10}
                                                                        :done #(let [data (walk/keywordize-keys (js->clj %1))]
                                                                                 (.writeFile fs fname (pr-str data) (fn [& e] (prn "wrote:" e)))
                                                                                 (prn ">>> nest.post:" (-> data
                                                                                                           :objects
                                                                                                           first
                                                                                                           :value
                                                                                                           :days
                                                                                                           last)))}))))))))))

(set! *main-cli-fn* -main)
