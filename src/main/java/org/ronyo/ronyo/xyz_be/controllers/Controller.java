package org.ronyo.ronyo.xyz_be.controllers;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private final MongoClient mongoClient;

    public Controller(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @GetMapping
    public String index() {
        return "Hello World!";
    }

    @PostMapping("/db/cv-view")
    public void indexView() {
        MongoDatabase test = mongoClient.getDatabase("Test");
        try {
            Bson command = new BsonDocument("ping", new BsonInt64(1));
            Document commandResults = test.runCommand(command);
        } catch (MongoException mongoException) {
            System.out.println("Uh oh");
        }
    }

}
