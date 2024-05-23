package com.empresa.h2_t3_pr_jorge_gomez;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.Document;
import java.io.*;
import java.nio.file.*;
import java.util.List;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javafx.print.Paper;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.transform.Scale;

import com.lowagie.text.DocumentException;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.swing.filechooser.FileSystemView;
import java.io.IOException;
import java.util.Optional;

public class HelloController {

    @FXML
    private Label errorLabel;

    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField positionField;

    @FXML
    private TableView<Document> tableView;
    @FXML
    private TableColumn<Document, String> idColumn;
    @FXML
    private TableColumn<Document, String> nameColumn;
    @FXML
    private TableColumn<Document, String> positionColumn;
    @FXML
    private TableColumn<Document, String> dorsalColumn;
    @FXML
    private TableColumn<Document, String> edadColumn;
    @FXML
    private TableColumn<Document, String> nacionalidadColumn;
    @FXML
    private TableColumn<Document, String> valorColumn;
    @FXML
    private TableColumn<Document, String> partidosColumn;
    @FXML
    private TableColumn<Document, String> golesColumn;
    @FXML
    private TableColumn<Document, String> asistenciasColumn;
    @FXML
    private TableColumn<Document, String> minutosColumn;

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public void setDatabase(MongoDatabase database) {
        this.database = database;
        this.collection = database.getCollection("Jugadores");
    }

    @FXML
    protected void showDatabaseConnectionDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("database-connection-dialog.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            DatabaseConnectionController controller = fxmlLoader.getController();
            mongoClient = controller.getMongoClient();
            database = controller.getDatabase();
            collection = controller.getCollection();

            if (mongoClient != null) {
                errorLabel.setText("Conexión exitosa a la base de datos.");
            }
        } catch (IOException e) {
            errorLabel.setText("Error al mostrar el diálogo de conexión: " + e.getMessage());
        }
    }

    @FXML
    protected void openPlayerForm() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("player-form.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            PlayerFormController controller = fxmlLoader.getController();
            controller.setCollection(collection);

            stage.showAndWait();
            // Después de cerrar el formulario del jugador, recargar los registros
            readRecords();
        } catch (IOException e) {
            errorLabel.setText("Error al abrir el formulario de jugador: " + e.getMessage());
        }
    }

    @FXML
    protected void readRecords() {
        try {
            // Limpiar la tabla
            tableView.getItems().clear();

            // Volver a leer los registros de la base de datos y cargarlos en la tabla
            ObservableList<Document> documents = FXCollections.observableArrayList();
            for (Document doc : collection.find()) {
                documents.add(doc);
            }
            tableView.setItems(documents);
            errorLabel.setText("Lectura de registros completa.");
        } catch (Exception e) {
            errorLabel.setText("Error al leer los registros: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getObjectId("_id").toString()));
        nameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getString("nombre")));
        positionColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getString("posicion")));
        dorsalColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getInteger("dorsal").toString()));
        edadColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getInteger("edad").toString()));
        nacionalidadColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getString("nacionalidad")));
        valorColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getString("valor")));
        partidosColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getInteger("partidos").toString()));
        golesColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getInteger("goles").toString()));
        asistenciasColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getInteger("asistencias").toString()));
        minutosColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getInteger("minutos").toString()));
    }

    @FXML
    protected void updateRecord() {
        try {
            // Obtener el documento del jugador seleccionado en la tabla
            Document selectedPlayer = tableView.getSelectionModel().getSelectedItem();

            if (selectedPlayer != null) {
                // Cargar el formulario de actualización del jugador
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("update-player-form.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);

                UpdatePlayerFormController controller = fxmlLoader.getController();
                controller.setPlayerDocument(selectedPlayer);
                controller.setCollection(collection);

                stage.showAndWait();
                // Después de cerrar el formulario de actualización, recargar los registros
                readRecords();
            } else {
                errorLabel.setText("Por favor, selecciona un jugador para actualizar.");
            }
        } catch (IOException e) {
            errorLabel.setText("Error al abrir el formulario de actualización: " + e.getMessage());
        }
    }

    @FXML
    protected void deleteRecord() {
        try {
            // Obtener el documento del jugador seleccionado en la tabla
            Document selectedPlayer = tableView.getSelectionModel().getSelectedItem();

            if (selectedPlayer != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmar eliminación");
                alert.setHeaderText("¿Estás seguro de que deseas eliminar este registro?");
                alert.setContentText("Esta acción no se puede deshacer.");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    collection.deleteOne(Filters.eq("_id", selectedPlayer.getObjectId("_id")));
                    errorLabel.setText("Registro eliminado exitosamente.");
                    readRecords(); // Actualizar la tabla después de la eliminación
                } else {
                    errorLabel.setText("Eliminación cancelada.");
                }
            } else {
                errorLabel.setText("Por favor, selecciona un jugador para eliminar.");
            }
        } catch (Exception e) {
            errorLabel.setText("Error al eliminar el registro: " + e.getMessage());
        }
    }
    @FXML
    protected void generatePDF() {
        try {
            File file = new File(FileSystemView.getFileSystemView().getHomeDirectory(), "jugadores.pdf");
            OutputStream outputStream = new FileOutputStream(file);

            String htmlContent = generateHTMLTable();
            convertHTMLToPDF(htmlContent, outputStream);

            outputStream.close();

            errorLabel.setText("PDF generado exitosamente. Guardado en: " + file.getAbsolutePath());
        } catch (IOException | DocumentException e) {
            errorLabel.setText("Error al generar el PDF: " + e.getMessage());
        }
    }

    private String generateHTMLTable() {
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><body><table>");

        // Agrega las filas y columnas de la tabla como HTML
        ObservableList<Document> items = tableView.getItems();
        for (Document item : items) {
            htmlContent.append("<tr>");
            htmlContent.append("<td>").append(item.getObjectId("_id").toString()).append("</td>");
            htmlContent.append("<td>").append(item.getString("nombre")).append("</td>");
            htmlContent.append("<td>").append(item.getString("posicion")).append("</td>");
            htmlContent.append("<td>").append(item.getInteger("dorsal").toString()).append("</td>");
            htmlContent.append("<td>").append(item.getInteger("edad").toString()).append("</td>");
            htmlContent.append("<td>").append(item.getString("nacionalidad")).append("</td>");
            htmlContent.append("<td>").append(item.getString("valor")).append("</td>");
            htmlContent.append("<td>").append(item.getInteger("partidos").toString()).append("</td>");
            htmlContent.append("<td>").append(item.getInteger("goles").toString()).append("</td>");
            htmlContent.append("<td>").append(item.getInteger("asistencias").toString()).append("</td>");
            htmlContent.append("<td>").append(item.getInteger("minutos").toString()).append("</td>");
            htmlContent.append("</tr>");
        }

        htmlContent.append("</table></body></html>");
        return htmlContent.toString();
    }

    private void convertHTMLToPDF(String htmlContent, OutputStream outputStream) throws IOException, DocumentException {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);
    }
}

