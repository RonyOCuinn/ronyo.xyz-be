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
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Configuration
@Slf4j
public class Mongo {

    private final SecretsManagerClient secretsManagerClient;
    private static final String SECRET_IDENTIFIER = "rds!cluster-15db7328-6df4-4c8d-946f-e83879f3330d";

    public Mongo(SecretsManagerClient secretsManagerClient) {
        this.secretsManagerClient = secretsManagerClient;
    }

    @Bean
    public MongoClient mongoClient() {
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder().secretId(SECRET_IDENTIFIER).build();
        GetSecretValueResponse getSecretValueResponse = secretsManagerClient.getSecretValue(getSecretValueRequest);

        String uri = getUri(getSecretValueResponse);
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .serverApi(serverApi)
                .build();

        return MongoClients.create(settings);
    }

    private String getUri(GetSecretValueResponse getSecretValueResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        String secretValue = getSecretValueResponse.secretString();
        String username;
        String password;

        try {
            JsonNode jsonNode = objectMapper.readTree(secretValue);
            if (jsonNode.has("password") && jsonNode.has("username")) {
                username = jsonNode.get("username").asText();
                password = jsonNode.get("password").asText();
            } else {
                throw new RuntimeException("Missing value(s) from Amazon Secret");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return String.format("mongodb://%s:%s@ronyoxyz-db.c7wmsu60w6w6.eu-west-1.docdb.amazonaws.com:27017/?tls=true&tlsCAFile=global-bundle.pem&retryWrites=false", username, password);
    }

}
