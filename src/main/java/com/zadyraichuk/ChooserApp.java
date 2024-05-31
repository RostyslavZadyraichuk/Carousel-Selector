package com.zadyraichuk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;

public class ChooserApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader =
                new FXMLLoader(Objects.requireNonNull(getClass().getResource("../../ui/xml/chooser.fxml")));
        Parent root = loader.load();
        primaryStage.setTitle("Wheel Chooser");
        primaryStage.setScene(new Scene(root, 550, 650));
        primaryStage.setResizable(false);
        Font.loadFont(getClass().getResourceAsStream("../../ui/font/icomoon.ttf"), 14);

        primaryStage.show();

        MainController controller = loader.getController();
        controller.init();
    }

}
