(defproject mal "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :injections   [(require 'nu)]
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [cljdev "0.6.0-SNAPSHOT"]
                 [org.clojure/algo.monads "0.1.6"]]
  :profiles {:step0_repl {:main mal.step0-repl
                          :uberjar-name "step0_repl.jar"}
             :step1_read_print {:main mal.step1-read-print
                                :uberjar-name "step1_read_print.jar"}
             :step2_eval {:main mal.step2-eval
                          :uberjar-name "step2_eval.jar"}
             :step3_env {:main mal.step3-env
                         :uberjar-name "step3_env.jar"}})
