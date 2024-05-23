package com.empresa.h2_t3_pr_jorge_gomez;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;

public class DatabaseConnectionController {

    @FXML
    private Label errorLabel;

    @FXML
    private TextField userField;
    @FXML
    private TextField passwordField;

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    @FXML
    protected void connectToDatabase() {
        String user = "administrador";
        String password = "Abc123456";
        String clusterAddress = "jorgegg.utgag9g.mongodb.net";

        try {
            // Conectar a la base de datos MongoDB en MongoDB Atlas
            String uri = "mongodb+srv://" + user + ":" + password + "@" + clusterAddress + "/?retryWrites=true&w=majority&appName=JorgeGG";
            mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase("SevillaFC");
            collection = database.getCollection("Jugadores");

            errorLabel.setText("Conexión exitosa a la base de datos.");

            Stage stage = (Stage) errorLabel.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            errorLabel.setText("Error al conectar a la base de datos: " + e.getMessage());
        }

    }


    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }
}