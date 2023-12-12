package me.braydon.feather;

import com.mongodb.ConnectionString;
import me.braydon.feather.databases.mongodb.MongoDB;

/**
 * @author Braydon
 */
public final class Playground {
    public static void main(String[] args) {
        MongoDB mongoDB = new MongoDB(); // Create the database instance
        mongoDB.connect(new ConnectionString("mongodb://root:p4$$w0rd@localhost:27017/database")); // Connect to the MongoDB server
        
        // Get the ping to the database synchronously (blocking)
        long ping = mongoDB.sync().getPing();
        System.out.println("ping = " + ping);
        
        // ...or get the ping to the database asynchronously (another thread)
        mongoDB.async().getPing().thenAccept(asyncPing -> {
            System.out.println("asyncPing = " + asyncPing);
        });
        
        // Close the connection after our app is done
        mongoDB.close();
    }
}