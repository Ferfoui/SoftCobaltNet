package fr.ferfoui.softcobalt.client.gui;

import fr.ferfoui.softcobalt.common.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CobaltApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(GuiResources.COBALT_APPLICATION_FXML);
        Scene scene = new Scene(root, 400, 200);

        primaryStage.setTitle(Constants.NAME);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
