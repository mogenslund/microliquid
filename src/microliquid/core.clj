(ns microliquid.core
  (:require [microliquid.slider :refer :all]
            [microliquid.editor :as editor]
            [microliquid.tty :as tty]))

(def keymap
  {"1" left
   "2" right
   "backspace" (fn [sl] (delete sl))})

(defn create-buffer
  []
  (editor/new-buffer "buffer1")
  (editor/set-keymap keymap)
  (editor/new-buffer "buffer2")
  (editor/set-keymap keymap)
  (editor/set-global-key "0" editor/previous-buffer))

(defn -main
  [& args]
  (create-buffer)
  (tty/init))
