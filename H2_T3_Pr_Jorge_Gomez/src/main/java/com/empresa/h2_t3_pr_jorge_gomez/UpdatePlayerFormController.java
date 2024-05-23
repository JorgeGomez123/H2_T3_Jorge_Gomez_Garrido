package com.empresa.h2_t3_pr_jorge_gomez;

import com.mongodb.client.MongoCollection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.bson.Document;

public class UpdatePlayerFormController {

    @FXML
    private Label errorLabel;

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

    private Document playerDocument;
    private MongoCollection<Document> collection;

    public void setPlayerDocument(Document playerDocument) {
        this.playerDocument = playerDocument;
        // Cargar los detalles del jugador en los campos de texto
        if (playerDocument != null) {
            dorsalField.setText(playerDocument.getInteger("dorsal").toString());
            nameField.setText(playerDocument.getString("nombre"));
            positionField.setText(playerDocument.getString("posicion"));
            edadField.setText(playerDocument.getInteger("edad").toString());
            nacionalidadField.setText(playerDocument.getString("nacionalidad"));
            valorField.setText(playerDocument.getString("valor"));
            partidosField.setText(playerDocument.getInteger("partidos").toString());
            golesField.setText(playerDocument.getInteger("goles").toString());
            asistenciasField.setText(playerDocument.getInteger("asistencias").toString());
            minutosField.setText(playerDocument.getInteger("minutos").toString());
        }
    }

    @FXML
    protected void updatePlayer() {
        try {
            if (playerDocument != null) {
                playerDocument.put("dorsal", Integer.parseInt(dorsalField.getText()));
                playerDocument.put("nombre", nameField.getText());
                playerDocument.put("posicion", positionField.getText());
                playerDocument.put("edad", Integer.parseInt(edadField.getText()));
                playerDocument.put("nacionalidad", nacionalidadField.getText());
                playerDocument.put("valor", valorField.getText());
                playerDocument.put("partidos", Integer.parseInt(partidosField.getText()));
                playerDocument.put("goles", Integer.parseInt(golesField.getText()));
                playerDocument.put("asistencias", Integer.parseInt(asistenciasField.getText()));
                playerDocument.put("minutos", Integer.parseInt(minutosField.getText()));
                collection.replaceOne(new Document("_id", playerDocument.getObjectId("_id")), playerDocument);
                errorLabel.setText("Jugador actualizado correctamente.");


                errorLabel.getScene().getWindow().hide();
            }
        } catch (Exception e) {
            errorLabel.setText("Error al actualizar el jugador: " + e.getMessage());
        }
    }

    public void setCollection(MongoCollection<Document> collection) {
        this.collection = collection;
    }
    @FXML
    protected void closeForm() {
        nameField.getScene().getWindow().hide();
    }
}
