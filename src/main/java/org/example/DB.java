package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.lang.Nullable;
import org.bson.Document;

/**
 * Enum of {@link java.lang.reflect.Method Methods} that execute a Database command
 */
public enum DB{
    /**
     * Execute Get Method
     */
    Get{
        /**
         * @param obj {@link String}
         * @return returns <a style="color : #00ccff">null</a>  if success! <br>
         * returns the <a style="color: #ff0000">error message</a> if failed!
         */
        @Override
        public @Nullable String execute(String obj){
            return null;
        }
    },
    /**
     * Execute Post Method
     */
    Post{
        /**
         * @param obj {@link String}
         * @return returns <a style="color : #00ccff">null</a>  if success! <br>
         * returns the <a style="color: #ff0000">error message</a> if failed!
         */
        @Override
        public String execute(String obj){
            try{
                Document document = new Document("dado", obj).append("timestamp", System.currentTimeMillis());
                collection.insertOne(document);
                return "Completed";
            }catch (Exception e){
                return "Fail: "+e;
            }
        }
    },
    /**
     * Execute Request Method
     */
    Request{
        /**
         * @param obj {@link String}
         * @return returns <a style="color : #00ccff">null</a>  if success! <br>
         * returns the <a style="color: #ff0000">error message</a> if failed!
         */
        @Override
        public @Nullable String execute(String obj){
            return null;
        }
    },
    /**
     * Execute Trigger Method
     */
    Trigger{
        /**
         * @param obj {@link String}
         * @return returns <a style="color : #00ccff">null</a>  if success! <br>
         * returns the <a style="color: #ff0000">error message</a> if failed!
         */
        @Override
        public @Nullable String execute(String obj){
            return null;
        }
    },

    Start{
        @Override
        public String execute(String obj) {
            try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                this.mongoClient = mongoClient;
                this.database = this.mongoClient.getDatabase("projeto");
                collection = this.database.getCollection("sensor");
                return "Completed";
            } catch (Exception e) {
                return "Fail: "+e;
            }
        }
    };

    /**
     *
     * @param obj {@link String}
     * @return the requested object
     */
    public String execute(@Nullable String obj){
        return obj;
    };

    /**
     * @param obj {@link String}
     * @return {@link Boolean#TRUE true} if one of the {@link DB} items have the same name of the inserted {@link String}
     */
    public static boolean exists(String obj){
        for(DB db : DB.values()){
            if (obj.equals(db.toString())){
                return true;
            }
        }
        return false;
    };
    MongoCollection<Document> collection;
    MongoDatabase database;
    MongoClient mongoClient;
}
