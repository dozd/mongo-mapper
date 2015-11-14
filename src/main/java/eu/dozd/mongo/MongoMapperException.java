package eu.dozd.mongo;

/**
 * Only exception used in library.
 */
public class MongoMapperException extends RuntimeException {

    public MongoMapperException(String s) {
        super(s);
    }

    public MongoMapperException(String s, Throwable throwable) {
        super(s, throwable);
    }

}
