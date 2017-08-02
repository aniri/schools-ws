# schools-ws

Demo webservice using schools data from Romania. Uses the datasets from http://data.gov.ro/dataset/coordonate-geografice-unitati-de-invatamant-geocodare and http://data.gov.ro/dataset/reteaua-scolara-a-unitatilor-de-invatamant-2016-2017. The data was merged into one smaller table containing only: siiir code, school name, school city, school county and coordonates.

Uses:
* [Jersey](https://jersey.github.io/) - framework for developing RESTful Web Services in Java that provides support for JAX-RS APIs
* [Jetty](http://www.eclipse.org/jetty/) web server
* [maven](https://maven.apache.org/)

To build and deploy on Jetty server use:
```
mvn clean package jetty:run
```


Usage:

* http://localhost:8080/schools/ - view info
* http://localhost:8080/schools/licee?judet=AR two letter county code or B for Bucharest -> returns the schools from county in json format 
* http://localhost:8080/schools/licee?lat=CENTER_LATITUDE_VALUE&lng=CENTER_LONGITUDE_VALUE&radius=VALUE_IN_METRES -> returns the schools from the selected area in json format
* has very basic authorisation method: the previous urls return only the first 6 entries. Use the key parameter (demo api-key value is 123456) to get all results. Example http://localhost:8080/schools/licee?judet=AR&key=123456
