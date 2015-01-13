(defproject nestene-consciousness "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2657"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]]

  :node-dependencies [[source-map-support "0.2.8"]
                      [unofficial-nest-api "0.1.4"]]

  :plugins [[lein-cljsbuild "1.0.4"]
            [lein-npm "0.4.0"]]

  :source-paths ["src"]

  :cljsbuild {
    :builds [{:id "nestene-consciousness"
              :source-paths ["src"]
              :compiler {
                :output-to "out/nestene_consciousness.js"
                :output-dir "out"
                :target :nodejs
                :optimizations :none
                :source-map true}}]})
