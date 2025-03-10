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
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

@RestController
public class Controller {

    private final MongoClient mongoClient;
    private final S3Client s3Client;

    public Controller(MongoClient mongoClient,
                      S3Client s3Client) {
        this.mongoClient = mongoClient;
        this.s3Client = s3Client;
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

    @GetMapping("/test-sdk")
    public String testSdk() {
        ListBucketsResponse listBucketsResponse = s3Client.listBuckets();
        return listBucketsResponse.toString();
    }

}
