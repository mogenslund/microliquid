(ns microliquid.renderer
  (:require [microliquid.editor :as editor]
            [microliquid.slider :as s]
            [microliquid.buffer :as buffer]
            [clojure.string :as str]))

(defn point-to-row-col
  "Takes a slider and returns the row and column
  of the cursor when printed."
  [sl]
  (let [p (s/get-point sl)]
    (loop [r 0 c 0 s (s/beginning sl)]
      (cond (or (>= (s/get-point s) p) (s/end? s)) {:row r :column c}
            (= (s/get-char s) "\n") (recur (+ r 1) 0 (s/right s))
            true (recur r (+ c 1) (s/right s))))))

(defn render-screen
  "Renders screen from the editor state"
  []
  (let [sl (buffer/get-slider (editor/current-buffer))]
    {:lines (str/split-lines (s/get-content sl))
     :cursor (point-to-row-col sl)}))
