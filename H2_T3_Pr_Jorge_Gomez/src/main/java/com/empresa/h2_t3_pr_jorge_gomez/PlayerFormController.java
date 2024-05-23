package com.empresa.h2_t3_pr_jorge_gomez;

import com.mongodb.client.MongoCollection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.bson.Document;

public class PlayerFormController {

    @FXML
    private TextField dorsalField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField positionField;
    @FXML
    private TextField edadField;
    @FXML
    private TextField nacionalidadField;
    @FXML
    private TextField valorField;
    @FXML
    private TextField partidosField;
    @FXML
    private TextField golesField;
    @FXML
    private TextField asistenciasField;
    @FXML
    private TextField minutosField;
    @FXML
    private Label errorLabel;

    private MongoCollection<Document> collection;

    public void setCollection(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    @FXML
    protected void savePlayer() {
        try {
            // Verifica que todos los campos estén llenos antes de guardar el registro
            if (nameField.getText().isEmpty() || positionField.getText().isEmpty() ||
                    dorsalField.getText().isEmpty() || edadField.getText().isEmpty() || nacionalidadField.getText().isEmpty() ||
                    valorField.getText().isEmpty() || partidosField.getText().isEmpty() ||
                    golesField.getText().isEmpty() || asistenciasField.getText().isEmpty() || minutosField.getText().isEmpty()) {
                errorLabel.setText("Por favor, completa todos los campos.");
                return;
            }

            Document doc = new Document("dorsal", Integer.parseInt(dorsalField.getText()))
                    .append("nombre", nameField.getText())
                    .append("posicion", positionField.getText())
                    .append("edad", Integer.parseInt(edadField.getText()))
                    .append("nacionalidad", nacionalidadField.getText())
                    .append("valor", valorField.getText())
                    .append("partidos", Integer.parseInt(partidosField.getText()))
                    .append("goles", Integer.parseInt(golesField.getText()))
                    .append("asistencias", Integer.parseInt(asistenciasField.getText()))
                    .append("minutos", Integer.parseInt(minutosField.getText()));
            collection.insertOne(doc);
            errorLabel.setText("Registro creado exitosamente.");
        } catch (NumberFormatException e) {
            errorLabel.setText("Error al crear el registro: Ingresa valores numéricos correctamente.");
        } catch (Exception e) {
            errorLabel.setText("Error al crear el registro: " + e.getMessage());
        }
    }

    @FXML
    protected void closeForm() {
        nameField.getScene().getWindow().hide();
    }
}
