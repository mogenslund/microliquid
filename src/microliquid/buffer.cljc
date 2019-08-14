(ns microliquid.buffer
  (:require [microliquid.slider :as s]))

(defn buffer
  [name]
  {::name name
   ::slider (s/slider "")
   ::highlighter nil
   ::keymap {}})

(defn- doto-slider
  [buffer fun & args]
  (update buffer ::slider #(apply fun (list* % args))))

(defn apply-to-slider
  [buffer fun]
  (update buffer ::slider fun))

(defn get-slider
  [buffer]
  (buffer ::slider))

(defn set-keymap
  [buffer keymap]
  (assoc buffer ::keymap keymap))

(defn get-keymap
  [buffer]
  (buffer ::keymap))

(defn get-name
  [buffer]
  (buffer ::name))

