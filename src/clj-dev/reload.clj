(ns clj-dev.reload
  "Utilities for using the ns-tracker library from a repl to auto-reload changed namespaces.

  ns-tracker is normally used like so:

  (def tracker
  (ns-tracker [\"src\" \"test\"]))

  (doseq [ns-sym (tracker)]
    (require ns-sym :reload))

  With this NS, you can also:

  (def track-thread (track \"src\" \"test\"))

  to automatically reload all namespaces defined under src and test whenever they
  change."
  (:require [clojure.tools.logging :as log]
            [ns-tracker.core :as tracker]))


(defn- check-namespace-changes [track]
  (try
    (doseq [ns-sym (track)]
      (println "Reloading namespace: " ns-sym)
      (require ns-sym :reload))
    (catch Throwable e (.printStackTrace e)))
  (Thread/sleep 500))


(defn track
  "Track .clj files under the specified source code root folders relative to the project
  root."
  [& source-roots]
  (let [track (tracker/ns-tracker source-roots)]
    (doto
        (Thread.
         #(while true
            (check-namespace-changes track)))
      (.setDaemon true)
      (.start))))
