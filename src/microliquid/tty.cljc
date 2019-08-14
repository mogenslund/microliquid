(ns microliquid.tty
  (:require [microliquid.editor :as editor]
            [microliquid.renderer :as renderer]
            [clojure.string :as str]))

(def esc "\033[")

; https://github.com/mogenslund/liquidjs/blob/master/src/dk/salza/liq/adapters/tty.cljs
(defn cmd
  [& args]
  #?(:clj (.waitFor (.exec (Runtime/getRuntime) (into-array args)))
     :cljs (do)))

(defn- tty-print
  [& args]
  #?(:clj (.print (System/out) (str/join "" args))
     :cljs (js/process.stdout.write (str/join "" args))))

(defn set-raw-mode
  []
  #?(:clj (do (cmd "/bin/sh" "-c" "stty -echo raw </dev/tty")
              (tty-print esc "0;37m" esc "2J")
              (tty-print esc "?7l"))  ; disable line wrap
     :cljs (let [readline (js/require "readline")]
             (.emitKeypressEvents readline process.stdin
               (js/process.stdin.setRawMode true)))))


(defn set-line-mode
  []
  (tty-print esc "0;37m" esc "2J")
  (cmd "/bin/sh" "-c" "stty -echo cooked </dev/tty")
  (cmd "/bin/sh" "-c" "stty -echo sane </dev/tty")
  (tty-print esc "0;0H" esc "s"))

(defn- raw2keyword
  "Converts the raw input to string"
  [raw]
  (cond (= raw 127) "backspace"
        (>= raw 32) (str (char raw))
        (= raw 9) "\t"
        (= raw 13) "\n"
        true (str (char raw))))

; https://stackoverflow.com/questions/48039759/how-to-distinguish-between-escape-and-escape-sequence
(defn input-handler
  "Loop inside i thread waiting for user input"
  []
  #?(:clj (future
            (let [r (java.io.BufferedReader. *in*)]
              (loop [input (.read r)]
                (when (not= input 27)
                  (editor/handle-input (raw2keyword input))
                  (let [sc (renderer/render-screen)]
                    (tty-print esc "0;37m" esc "2J")
                    (tty-print esc 0 ";" 0 "H" esc "s")
                    (doseq [line (sc :lines)]
                      (doseq [ch line]
                        (tty-print ch))
                      (tty-print "\r\n"))
                    (tty-print esc (+ ((sc :cursor) :row) 1) ";" (+ ((sc :cursor) :column) 1) "H" esc "s"))
                  (recur (.read r))))
              (set-line-mode)
              (Thread/sleep 500)
              (System/exit 0)))
     :cljs (js/process.stdin.on "keypress"
             (fn [chunk key]
               (when (= js/key.name "0") (js/process.exit))
               (editor/handle-input js/key.name)
               (let [sc (renderer/render-screen)]
                 (tty-print esc "0;37m" esc "2J")
                 (tty-print esc 0 ";" 0 "H" esc "s")
                 (doseq [line (sc :lines)]
                   (doseq [ch line]
                     (tty-print ch))
                   (tty-print "\r\n"))
                 (tty-print esc (+ ((sc :cursor) :row) 1) ";" (+ ((sc :cursor) :column) 1) "H" esc "s"))))))
               
       
    

(defn init
  []
  (set-raw-mode)
  (tty-print esc "0;37m" esc "2J")
  (tty-print esc "0;0H" esc "s")
  (input-handler))
