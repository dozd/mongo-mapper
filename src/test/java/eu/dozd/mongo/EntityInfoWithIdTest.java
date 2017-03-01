package eu.dozd.mongo;

import eu.dozd.mongo.annotation.Id;
import eu.dozd.mongo.entity.TestEntity;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EntityInfoWithIdTest {

    private TestEntity entity;
    private EntityInfo info;

    @Before
    public void setUp() throws Exception {
        entity = new TestEntity();
        info = new EntityInfoWithId(TestEntity.class);
    }

    @Test
    public void testEntitySetId() {
        info.setId(entity, "test");
        Assert.assertEquals("test", entity.getId());
    }

    @Test
    public void testEntitySetObjectId() throws Exception {
        info.setId(entity, new ObjectId("58b724ec1e558b174ce32eb6"));
        Assert.assertEquals("58b724ec1e558b174ce32eb6", entity.getId());

    }

    @Test
    public void testEntityGetId() {
        entity.setId("ahoj");
        Assert.assertEquals("ahoj", info.getId(entity));
    }

    @Test
    public void testGetIdColumn() {
        Assert.assertEquals("id", info.getIdField());
    }

    private static class EntityWithoutId {
        private String id;
    }

    @Test(expected = MongoMapperException.class)
    public void testEntityNoIdField() throws MongoMapperException {
        EntityWithoutId e = new EntityWithoutId();
        EntityInfo i = new EntityInfoWithId(EntityWithoutId.class);
        i.setId(e, "test");
    }

    private static class EntityWithoutSetterAndGetter {
        @Id
        private String id;
    }

    @Test(expected = MongoMapperException.class)
    public void testEntityWithoutSetterAndGetter() {
        EntityWithoutSetterAndGetter e = new EntityWithoutSetterAndGetter();
        EntityInfo i = new EntityInfoWithId(EntityWithoutSetterAndGetter.class);
        i.setId(e, "1");
    }

    private static class EntityWithoutSetter {
        @Id
        private String id;

        public String getId() {
            return id;
        }
    }

    @Test(expected = MongoMapperException.class)
    public void testEntityWithoutSetter() {
        EntityWithoutSetter e = new EntityWithoutSetter();
        EntityInfo i = new EntityInfoWithId(EntityWithoutSetter.class);
        i.setId(e, "1");
    }

    private static class EntityIdOnGetter {
        private String id;

        @Id
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @Test
    public void testEntityIdOnGetter() {
        EntityIdOnGetter e = new EntityIdOnGetter();
        EntityInfo i = new EntityInfoWithId(EntityIdOnGetter.class);
        i.setId(e, "a");
        Assert.assertEquals("a", i.getId(e));
    }

    @Test
    public void testGetClass() {
        Assert.assertFalse("Fields could not contain class field.", info.getFields().contains("class"));
    }

    private static class EntityWithRef {
        @Id
        private String id;

        TestEntity testEntity;

        public TestEntity getTestEntity() {
            return testEntity;
        }

        public void setTestEntity(TestEntity testEntity) {
            this.testEntity = testEntity;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @Test
    public void testEntityWithReference() {
        EntityWithRef eRef = new EntityWithRef();
        eRef.setTestEntity(entity);
        EntityInfo i = new EntityInfo(EntityWithRef.class);
        Assert.assertTrue(i.isMappedReference("testEntity"));
        Assert.assertFalse(i.isMappedReference("id"));
    }
}