package fr.ferfoui.softcobalt.client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class ApplicationController {

    @FXML
    private Label folderLabel;

    @FXML
    private void chooseDirectory() {
        Stage primaryStage = (Stage) folderLabel.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Sélectionner un dossier");
        File selectedDirectory = directoryChooser.showDialog(primaryStage);

        if (selectedDirectory != null) {
            folderLabel.setText("Dossier sélectionné: " + selectedDirectory.getAbsolutePath());
        } else {
            folderLabel.setText("Aucun dossier sélectionné");
        }
    }
}
