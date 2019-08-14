(ns microliquid.slider)

(defn slider
  "Creates a new slider with the given text"
  [text]
  {::before '()
   ::after (map str text)})

(defn get-char
  [sl]
  (first (sl ::after)))

(defn get-point
  [sl]
  (count (sl ::before)))

(defn beginning
  "Moves cursor to the beginning"
  [sl]
  (assoc sl
   ::before ()
   ::after (concat (reverse (sl ::before)) (sl ::after))))

(defn get-content
  "Returns content as string"
  [sl]
  (apply str (-> sl beginning ::after)))

(defn right
  "Moves cursor one to the right"
  [sl]
  (let [c (first (sl ::after))]
    (if c
      (assoc sl ::before (conj (sl ::before) c)
                ::after (rest (sl ::after)))
      sl)))

(defn left
  "Moves cursor one to the left"
  [sl]
  (let [c (first (sl ::before))]
    (if c
      (assoc sl ::before (rest (sl ::before))
                ::after (conj (sl ::after) c))
      sl)))

(defn insert
  "Insert text into slider"
  [sl text]
  (assoc sl ::before (concat (map str (reverse text)) (sl ::before))))

(defn delete
  [sl]
  (update sl ::before rest))

(defn end?
  "Checks if cursor at the end"
  [sl]
  (empty? (sl ::after)))