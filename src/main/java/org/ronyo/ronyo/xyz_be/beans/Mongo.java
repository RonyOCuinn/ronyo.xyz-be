package org.ronyo.ronyo.xyz_be.beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.util.List;

@Configuration
public class Mongo {

    @Bean
    public MongoClient mongoClient() {
        String secretName = "rds!cluster-15db7328-6df4-4c8d-946f-e83879f3330d";
        Region region = Region.of("eu-west-1");
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder().secretId(secretName).build();
        GetSecretValueResponse getSecretValueResponse;

        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
        } catch ( Exception e ) {
            System.out.println("uh oh");
            throw e;
        }
        ObjectMapper objectMapper = new ObjectMapper();


        String secretValue = getSecretValueResponse.secretString();
        String password = "";
        try {
            JsonNode jsonNode = objectMapper.readTree(secretValue);
            if (jsonNode.has("password")) {
                password = jsonNode.get("password").asText();
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String host = "toBeImplemented";
        String username = "toBeImplemented";
        String uri = String.format("mongodb://%s:%s@%s:27017/?tls=true&tlsCAFile=global-bundle.pem&retryWrites=false", username, password, host);
        ServerApi serverApi= ServerApi.builder().version(ServerApiVersion.V1).build();

        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(new ConnectionString(uri)).serverApi(serverApi).build();

        return MongoClients.create(settings);
    }

}
