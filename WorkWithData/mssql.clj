(ns mssql)

(use 'datatable)

(System.Reflection.Assembly/LoadWithPartialName "System.Data")

(import '(System.Data.SqlClient SqlConnection SqlDataAdapter))
(import '(System.Data DataTable))

(comment
  Categories 
Customers 
Employees 
Order Details 
Orders 
Products 
Shippers 
Suppliers 

  (def categories (fetch-all-data "categories"))
  (def customers (fetch-all-data "customers"))
  (def employees (fetch-all-data "employees"))
  (def order-details (fetch-all-data "[Order Details]"))
  (def orders (fetch-all-data "orders"))
  (def products (fetch-all-data "products"))
  (def shippers (fetch-all-data "shippers"))
  (def suppliers (fetch-all-data "suppliers"))
  )

(defn fetch-all-data
  "Fetch data from the table"
  [table]
  (let [conString "Data Source=.\\SQLEXPRESS;AttachDbFilename='C:\\SQL Server 2000 Sample Databases\\NORTHWND.MDF';Integrated Security=True;Connect Timeout=30;User Instance=True"
        dt (DataTable.)
        sql (str "select * from " table)        
        ]
    (with-open [con (SqlConnection. conString)
                da (SqlDataAdapter. sql con)]    
      (.Open con)
      (.Fill da dt)
      (datatable-rows dt)
  )))

(defn table-definition
  "Fetch data from the table"
  [table]
  (let [conString "Data Source=.\\SQLEXPRESS;AttachDbFilename='C:\\SQL Server 2000 Sample Databases\\NORTHWND.MDF';Integrated Security=True;Connect Timeout=30;User Instance=True"
        dt (DataTable.)
        sql (str "select * from " table " where 1 = 0")        
        ]
    (with-open [con (SqlConnection. conString)
                da (SqlDataAdapter. sql con)]    
      (.Open con)
      (.Fill da dt)
      (datatable-definition dt)
  )))
