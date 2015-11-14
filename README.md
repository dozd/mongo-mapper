# mongo-mapper

Mapping POJO for MongoDB has not been easier. Thanks to the new codecs feature in [MongoDB Java 3.0 driver](https://www.mongodb.com/blog/post/introducing-30-java-driver).

## Installation

> Currently waiting for Maven Central approval.

### Maven

```
<dependency>
    <groupId>eu.dozd</groupId>
    <artifactId>mongo-mapper</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```
compile 'eu.dozd:mongo-mapper:1.0.0'
```

## Usage
1. Mark your entities with annotation. Make sure every entity has one String property marked as ID.

```java
import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;

@Entity
public class Person {
    @Id
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
```


2. Initialize mapper codecs through `MongoMapper.getProviders`.

```java
    CodecRegistry codecRegistry = CodecRegistries.fromProviders(MongoMapper.getProviders());
    MongoClientOptions settings = MongoClientOptions.builder().codecRegistry(codecRegistry).build();

    MongoClient client = new MongoClient(new ServerAddress(host, port), settings);
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

## See also
- [MongoDB Java driver documentation](http://mongodb.github.io/mongo-java-driver/3.1/)
- [Codec and CodecRegistry](http://mongodb.github.io/mongo-java-driver/3.1/bson/codecs/)