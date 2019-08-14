(ns microliquid.editor
  (:require [microliquid.buffer :as buffer]
            [microliquid.slider :as slider]
            [clojure.string :as str]))

(def editor
  "Main state of the editor"
  (atom
    {::buffers ()
     ::global-keymap {}}))

(defn bump
  ([li index]
    (if (> (count li) index)
      (let [item (nth li index)]
        (conj (remove (fn [x] (= x item)) li) item))
      li))
  ([li keyw match]
    (let [parts (group-by #(= (% keyw) match) li)]
      (concat (parts true) (parts false)))))

(defn doto-first
  [li fun & args]
  (conj (rest li) (apply fun (cons (first li) args))))

(defn- doto-buffer
  [fun & args]
  (swap! editor update ::buffers #(apply doto-first (list* % fun args)))
  nil)

(defn apply-to-slider
  [fun]
  (doto-buffer buffer/apply-to-slider fun))

(defn set-global-key
  [keyw fun]
  (swap! editor assoc-in [::global-keymap keyw] fun) nil)

(defn switch-to-buffer
  [buffername]
  (swap! editor update ::buffers bump ::buffer/name buffername))

(defn get-buffer
  [name]
  (first (filter #(= (% ::buffer/name) name) (@editor ::buffers))))

(defn new-buffer
  [name]
  (when (not (get-buffer name))
    (swap! editor update ::buffers conj (buffer/buffer name)))
  (switch-to-buffer name))

(defn current-buffer
  []
  (-> @editor ::buffers first))

(defn set-keymap
  [keymap]
  (doto-buffer buffer/set-keymap keymap))

(defn- one-arg?
  "Checks if the functions takes exactly
  one argument."
  [fun]
  false)

(defn previous-buffer
  []
  (switch-to-buffer (-> @editor ::buffers second ::buffer/name)))

(defn handle-input
  [ch]
  (let [action (or ((buffer/get-keymap (current-buffer)) ch)
                   ((@editor ::global-keymap) ch))]
    (if action
      (if (one-arg? action)
        (apply-to-slider action)
        (action))
      (apply-to-slider (fn [sl] (slider/insert sl ch))))))

