package com.empresa.h2_t3_pr_jorge_gomez;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;

public class LoginController {

    @FXML
    private Label errorLabel;

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passwordField;

    private final String correctUser = "administrador";
    private final String correctPassword = "Abc123456";
    private MongoClient mongoClient;
    private MongoDatabase database;

    @FXML
    protected void handleLogin() {
        String user = userField.getText();
        String password = passwordField.getText();

        if (user.equals(correctUser) && password.equals(correctPassword)) {
            try {
                // Conectar a la base de datos MongoDB en MongoDB Atlas
                String uri = "mongodb+srv://" + correctUser + ":" + correctPassword + "@jorgegg.utgag9g.mongodb.net/?retryWrites=true&w=majority&appName=JorgeGG";
                mongoClient = MongoClients.create(uri);
                database = mongoClient.getDatabase("SevillaFC");

                errorLabel.setText("Inicio de sesión exitoso.");

                // Cerrar la ventana de inicio de sesión
                Stage stage = (Stage) errorLabel.getScene().getWindow();
                stage.close();

                // Abrir la ventana principal
                openMainWindow();
            } catch (Exception e) {
                errorLabel.setText("Error al conectar a la base de datos: " + e.getMessage());
            }
        } else {
            errorLabel.setText("Usuario o contraseña incorrectos. Inténtalo de nuevo.");
        }
    }

    private void openMainWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Stage mainStage = new Stage();
            Scene scene = new Scene(fxmlLoader.load(), 880, 600);

            // Pasar la conexión de la base de datos al controlador principal
            HelloController controller = fxmlLoader.getController();
            controller.setDatabase(database);

            mainStage.setTitle("Gestión de Jugadores");
            mainStage.setScene(scene);
            mainStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
