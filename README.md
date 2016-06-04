# mongo-mapper

Mapping POJO for MongoDB has not been easier. Thanks to the new codecs feature in [MongoDB Java 3.0 driver](https://www.mongodb.com/blog/post/introducing-30-java-driver).
Simply mark your entities with annotation, create `EntityCodec` and that's it! Then use standard methods for storing and accessing data from MongoDB.

## Why us it?
- Simple and easy to use.
- Use standard (MongoDB) way for object manipulation.
- Works for synchronous as well as asynchronous version of MongoDB Java Driver.
- It's fast and small - only 13kB dependency.
 

## Installation

Mongo mapper is on Maven Central. Add following into your `pom.xml`.

##### Maven

```
<dependency>
    <groupId>eu.dozd</groupId>
    <artifactId>mongo-mapper</artifactId>
    <version>1.0.2</version>
</dependency>
```

##### Gradle

```
compile 'eu.dozd:mongo-mapper:1.0.2'
```

## Usage
1. Annotate your entities with `Entity`. Make sure every entity has exactly one String annotated with `Id`. All properties must have
correct getter and setter methods according Java Bean specification.

    ```java
    import eu.dozd.mongo.annotation.Entity;
    import eu.dozd.mongo.annotation.Id;
    
    @Entity
    public class Person {
        @Id
        String id;
        String name;
        int age;
    
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
    }
    ```

2. Create mapper `CodecProvider` by calling `MongoMapper.getProviders`.
    ```java
    CodecRegistry codecRegistry = CodecRegistries.fromProviders(MongoMapper.getProviders());
    ```

    - Usage for standard driver:
    
        ```java
            MongoClientOptions settings = MongoClientOptions.builder().codecRegistry(codecRegistry).build();
        
            MongoClient client = new MongoClient(new ServerAddress("localhost", 27017), settings);
        ```
    
    - Usage for asynchronous driver:
    
        ```java
            ClusterSettings clusterSettings = ClusterSettings.builder().hosts(Arrays.asList(new ServerAddress("localhost", 27017))).build();
            MongoClientSettings settings = MongoClientSettings.builder().codecRegistry(codecRegistry)
                                                .clusterSettings(clusterSettings).build();
            
            MongoClient client = MongoClients.create(settings);
        ```
        
3. Access and store data like normal POJO.

    ```java
        MongoCollection<Person> collection = db.getCollection("persons", Person.class);
    
        Person person = new Person();
        person.setName("Foo Bar");
    
        // Store person normally.
        collection.insertOne(person);
    
        // Access data.
        Person person2 = collection.find.first()
    ```

## Features
- Entity reference - make sure all entities classes are annotated with `Entity`.
- Embedded entities - entities annotated with `Embedded` does not need to have an ID. 
- Feel free to create issue or pull request if you missing some functionality.

## Other codecs
- You can create other Codecs for you special classes.
- I added [`BigDecimalCodec`](tree/master/src/main/java/eu/dozd/mongo/codecs/bigdecimal/BigDecimalCodec) and [`BigDecimalCodecProvider`](tree/master/src/main/java/eu/dozd/mongo/codecs/bigdecimal/BigDecimalCodecProvider) as an example.

## Licence
Copyright 2016 Zdenek Dolezal

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

## See also
- [MongoDB Java driver documentation](http://mongodb.github.io/mongo-java-driver/3.1/)
- [Codec and CodecRegistry](http://mongodb.github.io/mongo-java-driver/3.1/bson/codecs/)
