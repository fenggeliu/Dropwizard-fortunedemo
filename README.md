# Prototype Implemetation

How to start the FortuneService application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/fortune-demo-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

Architecture
---

1.	The basica design includes the dropwizard archetype framework, freemaker view template and a MongoDB persistence Volume.
2.	As soon as the application starts, the dropwizard framework will generate local storage in my core data structure holding all the current data from database. It takes responsibilities for handling 4 REST calls: `GET`, `POST`, `DELETE` indicated query and generating main view of the application. The API calls will directly update the local data while indirectly update the database in the background threads.
3.  The reason for multi-threading is to ensure O(1) time operatin in the main thread by working with local variables. Even if we can do Hash cluster in MongoDB but the O(1) time database query still has lower performance than that of Binary Tree SQL. So I believe background thread is my choice to update the database later than receiving the result locally.
4.	A MongoDB database structure is mainly for keeping persistence volumes that hold the data while server shutdown or restarts. I deployed a mongdb container exposed in kubernetes microserver to specifically work on this purpose. The external port address is `35.197.11.144:27017` and the database is called `"FortuneDB"`
5.	The freemaker template works mainly to generate the main index page for this web application. I did not use any CSS styling since this is for a demonstration of architecture only.

Core Data Structure
---

The core data structure is a `FortuneStore` Java class, stored in the `fenggetest.core` package, holding all the information as soon as the application starts. The REST API calls update the FortuneStore local data storage directly. `insert`, `delete` and `random` are all performed in O(1) time.

The data structure includes 3 private instance variables: 
```java
    private HashMap<String, String> map1;
    private HashMap<String, Integer> map2;
    private ArrayList<String> index;
 ```
map1 has fortune message as Key and unique id as Value. map2 has unique id as Key and an incrementing integer index as Value. ArrayList index has fortune messages as items. 
Get will generate random number for ArrayList `index` as index to get the fortune message then return it.
Insert will put new key value pair into HashMap, or add item into ArrayList. All will require O(1) time.
Delete item by unique id needs to find the current order index from `map2`, then use the order index number onto ArrayList `index` to find the fortune message. The message is key in `map1`, so we delete all found items in 3 variables. To keep the current order consistent, we have swap the item into the deleted index for `map2` and `index`. `map1` will not change after item removed. 

Each unit data will be held in a data structure named `Fortune` which has a random generated unique id for each input and has a String value to store fortune message.

Application Lifecyle
---

1. Once the build command is fired, the application finds the signature in `FortuneServiceApplication` class in `src.java.fenggetest package`. Due to the configuration the application will initialize the bootstrap by adding a viewbundle. A run commend will fire to locate the database in MongoDB, with the port and name information from configuration as well. A `JacksonDBCollection` class will deploy and update the collection fond by name "FortuneDb". 
2. By cursoring throught the collection we serialize each elements into the `Fortune` class with fortune message and an id. Then we create local data storage, the `FortuneStore` class from inputing the list of fortunes.
3. `FortuneResource` is the only resource class registered in the jersey environment. At `"/"` it performs a `welcome()` method to return the view for main page of the application with a random fortune message. `GET` `POST` `DELETE` on the required path with String value will directly modify local storage `FortuneStore` to get the results in O(1) time. But before return, `POST` and `DELETE` will run a background thread to update database value while not hinder the main thread process.
