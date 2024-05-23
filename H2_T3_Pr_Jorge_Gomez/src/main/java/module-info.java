module com.empresa.h2_t3_pr_jorge_gomez {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;
    requires com.github.librepdf.openpdf;
    requires flying.saucer.pdf;
    requires java.desktop;

    opens com.empresa.h2_t3_pr_jorge_gomez to javafx.fxml;
    exports com.empresa.h2_t3_pr_jorge_gomez;
}
