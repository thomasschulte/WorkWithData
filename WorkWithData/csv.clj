(ns csv)

(require '[clojure.string :as s])

(def file-path "C:\\Users\\thomas\\Downloads\\Bezirke_einzeldarstellung.csv")

(defn convert-one-line [line kws]
  (let [values (s/split line #";")]
    (apply assoc {} (interleave  kws values))))

(defn csv-to-map [file-name]
  (let [content (slurp file-name)
        raw-lines (s/split-lines content)
        key-line (first raw-lines)
        csv-keywords (map #(keyword (s/replace % " " "_")) (s/split key-line #";"))
        data-lines (rest raw-lines)]
    (map #(convert-one-line % csv-keywords) data-lines)))

(defn to-numeric [key xs]
  (map #(assoc % key (double (s/replace (% key) "." ""))) xs))

(def raw-ls (csv-to-map file-path))
(def ls (->> raw-ls
          (to-numeric :Plan_2011)
          (to-numeric :Plan_2012)
          (to-numeric :Plan_2013)
          (to-numeric :Plan_2014)
          (to-numeric :Plan_2015)
          (to-numeric :Ergebnis_2010)))

(filter #(= (:Bezirk %) "1") ls)

(defn get-bezirke [xs]
   (->> 
     (map #(select-keys % [:Bezirk :Bezeichnung]) xs)               
     distinct))

(defn get-teilplanzeilen [xs] 
  (->>                
    (map :Teilplanzeile ls)               
    distinct))

(defn calc-gesammt-summen [xs]
  {:Gesammt2010 (reduce + (map :Ergebnis_2010 xs))
   :Gesammt2011 (reduce + (map :Plan_2011 xs))
   :Gesammt2012 (reduce + (map :Plan_2011 xs))
   :Gesammt2013 (reduce + (map :Plan_2011 xs))
   :Gesammt2014 (reduce + (map :Plan_2011 xs))
   :Gesammt2015 (reduce + (map :Plan_2011 xs))})

(def bezirk1 (filter #(= (:Bezirk %) "1") ls))
(def Zeile-27 (filter #(= (:Teilplanzeile %) "27 - Interne Leistungsbeziehung (Aufwand / (-) Ertrag)") ls))

(defn summe-pro-bezirk [xs]
  (for [bezirk (get-bezirke xs)]
    (let [nr (:Bezirk bezirk)
          ls (filter #(= nr (:Bezirk %)) xs)
          ges (calc-gesammt-summen ls)]
      (assoc ges :Bezirk nr :Bezeichnung (:Bezeichnung bezirk))
      )))

(defn summe-pro-teilplanzeile [xs]
  (for [zeile (get-teilplanzeilen xs)]
    (let [ls (filter #(= zeile (:Teilplanzeile  %)) xs)
          ges (calc-gesammt-summen ls)]
      (assoc ges :Teilplanzeile zeile))))






















