(ns Class1)

(require '[clojure.string :as s])

(def file-path "C:\\Users\\tschulte\\Downloads\\Bezirke_einzeldarstellung.csv")

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
(def bezirke (->> 
               (map #(select-keys % [:Bezirk :Bezeichnung]) ls)
               distinct
               ))

















