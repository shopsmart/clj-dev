(ns clj-dev.breakpoint
  "Breakpoints and a debug repl for Clojure.

  Synopsis:

  (breakpoint)        - Open a debug repl.
  (breakpoint form)   - Open a debug repl.  If (quit-dr) exits the repl, form is evaluated
                        and becomes the value of (breakpoint form).  If (quit-dr quit-form)
                        exits the repl, quit-form is evaluated and becomes the value of
                        (breakpoint form) instead.
  (quit-dr)           - Quit the debug repl.
  (quit-dr quit-form) - Quit the debug repl.  Evaluate quit-form and return its value as the
                        value of the (breakpoint...) expression.

  Examples:

  user=> (let [c 1
               d 2]
           (defn a [b c]
             (breakpoint)
             d))

  user=> (a \"foo\" \"bar\")

  dr => c
  \"bar\"

  dr => d
  2

  dr => *locals*
  {fn__20 #<user$eval__19 user$eval__19@955cd5>
          c \"bar\"
          d 2
          fn__22 #<user$eval__19$a__21 user$eval__19$a__21@59fb21>
          b \"foo\"}

  user=> (let [a 10] (breakpoint (* a a)))

  dr-1-1006 => (quit-dr)
  100

  user=> (let [a 10] (breakpoint (* a a)))

  dr-1-1007 => (quit-dr 99)
  99"

  [:require clojure.main])


(defmacro local-bindings
  "Produces a map of the names of local bindings to their values."
  []
  (let [symbols (keys &env)]
    (zipmap (map (fn [sym] `(quote ~sym)) symbols) symbols)))


(declare ^:dynamic *locals*)


(defn view-locals []
  *locals*)


(defn eval-with-locals
  "Evals a form with given locals. The locals should be a map of symbols to
  values."
  [locals form]
  (binding [*locals* locals]
    (eval
     `(let ~(vec (mapcat #(list % `(*locals* '~%)) (keys locals)))
        ~form))))


(defn dr-read
  [request-prompt request-exit]
  (let [input (clojure.main/repl-read request-prompt request-exit)]
    (if (= input '())
      request-exit
      input)))


(def ^:dynamic level 0)
(def counter (atom 1000))
(defn inc-counter []
  (swap! counter inc))


(def element (atom nil))


(def quit-dr-exception
     (proxy [Exception java.util.Enumeration] []
       (nextElement [] @element)))


(defn quit-dr [ & form]
  (reset! element (first form))
  (throw quit-dr-exception))


(def ^:dynamic  exit-dr-exception
     (Throwable. "Exiting back to main repl from breakpoint"))


(defn exit-dr []
  (throw exit-dr-exception))


(defn caught [exc]
  (cond
    (= (.getCause exc) quit-dr-exception) (throw quit-dr-exception)
    (= (.getCause exc) exit-dr-exception) (throw exit-dr-exception)
    :else (clojure.main/repl-caught exc)))


(defmacro breakpoint
  "Starts a REPL with the local bindings available."
  ([]
     `(breakpoint nil))
  ([form]
     `(let [counter# (inc-counter)
            eval-fn# (partial eval-with-locals (local-bindings))]
        (try
         (binding [level (inc level)]
           (clojure.main/repl
            :prompt #(print (str "dr-" level "-" counter# " => "))
            :eval eval-fn#
            :read dr-read
            :caught caught))
         (catch Exception e#
           (cond
             (= e# quit-dr-exception)
             (if-let [new-form# (.nextElement quit-dr-exception)]
               (eval-fn# new-form#)
               (eval-fn# ~form))
             (= e# exit-dr-exception)
             (when (> level -1)
               (throw exit-dr-exception))
             :else (throw e#)))))))
