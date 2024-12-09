package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.lang.Nullable;
import org.bson.Document;

import javax.swing.*;

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
            return obj;
        }
    },

    GetT{
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
    //Insert{
        /**
         * @param obj {@link String}
         * @return returns <a style="color : #00ccff">null</a>  if success! <br>
         * returns the <a style="color: #ff0000">error message</a> if failed!
         */
//        @Override
//        public Object execute(String obj) throws Exception {
//            try{
//                Document document = new Document("dado", obj).append("timestamp", System.currentTimeMillis());
//                users.insertOne(document);
//            }catch (Exception e){
//                throw new Exception(e);
//            }
//            return obj;
//        }
    //},
    Post{
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

    PostT{
        /**
         * @param obj {@link String}
         * @return returns <a style="color : #00ccff">null</a>  if success! <br>
         * returns the <a style="color: #ff0000">error message</a> if failed!
         */
        @Override
        public @Nullable String execute(String obj){
            Timer a = new Timer(0, null);
            a.start();

            a.stop();
            return a.toString();
        }
    },
    /**
     * Execute Request Method
     */
    PostGetT{
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

    PostGet{
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

    Connect{
        @Override
        public Object execute(String obj) throws Exception {
            try (MongoClient mongoClient = MongoClients.create("mongodb+srv://leticia23205:controlegastos123@controlegastos.fpwju.mongodb.net/")) {
                database = mongoClient.getDatabase("controle_gastos");
                usuarios = database.getCollection("usuarios");
                receitas = database.getCollection("receitas");
                despesas = database.getCollection("despesas");
            } catch (Exception e) {
                throw new Exception(e);
            }
            return "";
        }
    },

    fillRandom{
        @Override
        public Object execute(String obj) throws Exception {
            try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                MongoDatabase database = mongoClient.getDatabase("projeto");
                usuarios = database.getCollection("usuarios");
            } catch (Exception e) {
                throw new Exception(e);
            }
            return "";
        }
    }
    ;

    /**
     *
     * @param obj {@link String}
     * @return the requested object
     */
    public abstract Object execute(@Nullable String obj) throws Exception;

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

    public static  MongoDatabase database;
    public static MongoCollection<Document> usuarios;
    public static MongoCollection<Document> receitas;
    public static MongoCollection<Document> despesas;
}
