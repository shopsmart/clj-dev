(defproject com.github.shopsmart/clj-dev "0.1.0"
  :description "Tools to make development and exploration using Clojure more pleasant."
  :url "https://github.com/shopsmart/clj-dev"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins [[lein-test-bang-bang "0.2.0"] ; lein test!! - Run each test NS in a separate JRE
            [lein-ancient "0.6.10"]       ; lein ancient - Check for outdated dependencies
            [lein-auto "0.1.2"]           ; e.g.: lein auto kbit   or lein auto test
            [lein-kibit "0.1.2"]]         ; lein kibit - Linter that suggests more idiomatic forms

  :repositories [["jitpack" "https://jitpack.io"]]

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [com.github.shopsmart/clj-foundation "0.9.12"]])
