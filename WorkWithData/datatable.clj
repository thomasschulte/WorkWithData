(ns datatable)

(System.Reflection.Assembly/LoadWithPartialName "System.Data")
(System.Reflection.Assembly/LoadWithPartialName "Microsoft.VisualBasic")

(import 'System.Data.DataTable)
(import 'Microsoft.VisualBasic.DateAndTime)

(defn create-datatable
  "Create a data table from definitions
   definitions is a map with key type definition like {:id Int32 :name String}"
  [definitions]
  (let [dt (DataTable.)]
       (doseq [entry (seq definitions)]     
         (doto (.. dt Columns)         
           (.Add (name (first entry)) (second entry))))
    dt))

(defn set-row-value 
  ( [r k v]   
    "Put in the datarow a value at key position"     
    (.set_Item r k v))
  ( [r m]
    "Put the map entries in the row"    
    (doseq [entry (seq m)]    
      (.set_Item r (name (first entry)) (second entry)))))

(defn add-new-row [dt m]
  "adds a new row to the datatable
   m is a map with key value definitions {:id 1, :name ""Thomas""} "
  (let [r (.NewRow dt)]
    (set-row-value r m)
    (.. dt Rows (Add r))))

(defn fill-datatable [dt col]
  "Fills the data table with the entries of the collection col"
  "col [{:id 1, :name ""Thoamas""} {:id 2 :name ""Andrea""}]"
  (doseq [entry col]
    (add-new-row dt entry)))

(defn datatable-definition 
  "returns the data table definition
   as key value pair map"
  [dt]
  (let [cols (.. dt Columns)
        defs (apply assoc {} (interleave (map #(keyword (.. % ColumnName)) cols) (map #(.. % DataType) cols)))]
    defs))

(defn get-row-value [r k]    
  "Get the row value at k position" 
  (let [value  (.get_Item r k)]
    (if (= value DBNull/Value) nil value)))

  ; .get_Item r k))

(defn get-row [r keywords]
  "get all kewords in the row back as map"
  (let [row (apply assoc {} (interleave (map #(identity %) keywords) (map #(get-row-value r (name %)) keywords)))]
    row))

(defn datatable-rows
  "returns all rows of the datatable as vector of maps"
  [dt]
  (let [defs (datatable-definition dt)
        keywords (keys defs)
        rows (.. dt Rows)]
    (apply vector (map #(get-row % keywords) rows))))
