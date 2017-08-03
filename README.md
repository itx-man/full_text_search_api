# Search Server
Search server that uses Apache Lucene library for indexing documents and searching through entire document text. Spring Boot framework is used to build a REST API for querying the documents. The entire indexing & search code is written in Java. Further, a simple webapp is created to search and view the results. The search server runs on port 9000 whereas the HTTP server (created by Node JS) runs on port 8080. 

## Query Parser
The custom query parser supports various queries and the features currently supported are as follows:
* Term Query
  * Multiple terms on multiple fields
* Range Query
  * Multiple fields
  * DoublePoint, LongPoint 
* Score Boosting (Integer values)
* Specify Resultset Size
* OR operation (default) of multiple terms queries
* OR operation (default) of multiple range queries
* AND operation (default) of the term and range query result

## To be added Features 
* Prefix Query for adding auto suggestion feature to webapp 
* Score Boosting using Floating values
* Phrase Query
* Option to explicitly specify AND / OR operations
* Add position / offset information for text highlighting
