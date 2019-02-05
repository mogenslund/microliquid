(ns microliquid.renderer
  (:require [microliquid.editor :as editor]
            [microliquid.slider :refer :all]
            [microliquid.buffer :as buffer]
            [clojure.string :as str]))

(defn point-to-row-col
  "Takes a slider and returns the row and column
  of the cursor when printed."
  [sl]
  (let [p (get-point sl)]
    (loop [r 0 c 0 s (beginning sl)]
      (cond (or (>= (get-point s) p) (end? s)) {:row r :column c}
            (= (get-char s) "\n") (recur (+ r 1) 0 (right s))
            true (recur r (+ c 1) (right s))))))

(defn render-screen
  "Renders screen from the editor state"
  []
  (let [sl (buffer/get-slider (editor/current-buffer))]
    {:lines (str/split-lines (get-content sl))
     :cursor (point-to-row-col sl)}))
