(ns test-csv
  (:use clojure.test
	clojure-csv.core))

(deftest basic-functionality
  (is (= [["a" "b" "c"]] (parse-csv "a,b,c")))
  (is (= [["" ""]] (parse-csv ",")))
  (is (= [[""]] (parse-csv ""))))

(deftest quoting
  (is (= [["Before", "\"","After"]] (parse-csv "Before,\"\"\"\",After")))
  (is (= [["Before", "", "After"]] (parse-csv "Before,\"\",After")))
  (is (= [["", "start&end", ""]] (parse-csv "\"\",\"start&end\",\"\"")))
  (is (= [[",", "\"", ",,", ",,,"]]
	 (parse-csv "\",\",\"\"\"\",\",,\",\",,,\"")))
  (is (= [["quoted", "\",\"", "comma"]]
	 (parse-csv "quoted,\"\"\",\"\"\",comma"))))

(deftest newlines
  (is (= [["test1","test2"] ["test3","test4"]]
	 (parse-csv "test1,test2\ntest3,test4")))
  (is (= [["test1","test2"] ["test3","test4"]]
	 (parse-csv "test1,test2\r\ntest3,test4")))
  (is (= [["embedded","line\nbreak"]] (parse-csv "embedded,\"line\nbreak\"")))
  (is (= [["embedded", "line\r\nbreak"]]
	 (parse-csv "embedded,\"line\r\nbreak\""))))

(deftest writing
  (is (= "test1,test2\n" (write-csv [["test1" "test2"]])))
  (is (= "test1,test2\ntest3,test4\n"
	 (write-csv [["test1" "test2"] ["test3" "test4"]])))
  (is (= "quoted:,\"carriage\nreturn\"\n"
	 (write-csv [["quoted:" "carriage\nreturn"]])))
  (is (= "quoted:,\"embedded,comma\"\n"
	 (write-csv [["quoted:" "embedded,comma"]])))
  (is (= "quoted:,\"escaped\"\"quotes\"\"\"\n"
	 (write-csv [["quoted:" "escaped\"quotes\""]]))))

(deftest nonstring-inputs
  (is (= [["First", "Second"]] (parse-csv (seq "First,Second"))))
  (is (= [["First", "Second"]] (parse-csv (.toCharArray "First,Second")))))