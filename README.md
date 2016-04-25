## Backend for AirCheck-NG server

### Function
This program gets the user submission data for reports and processes the geo-location for later mapping on the webpage. The program merges the nearby reports within a constant utilizes the disjoint-set data structure. 

### Implementation
This program is written in Java, using a MySQL API for reading and writing to and from the database. 
The main algorithm used is disjoint set. It can be found in ``DisjointSet.java`` and https://en.wikipedia.org/wiki/Disjoint-set_data_structure.
