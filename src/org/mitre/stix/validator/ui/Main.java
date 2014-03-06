package org.mitre.stix.validator.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("validator-main-window.fxml"));
        primaryStage.setTitle("STIX Validator");
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add("org/mitre/stix/validator/ui/validator-main-window.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
