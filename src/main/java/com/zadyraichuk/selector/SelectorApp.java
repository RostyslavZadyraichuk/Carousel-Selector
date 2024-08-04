package com.zadyraichuk.selector;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import com.zadyraichuk.general.ResourceLoader;
import com.zadyraichuk.general.PropertiesFile;
import com.zadyraichuk.selector.controller.SelectorUIController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SelectorApp extends Application {

    public static final String USER_PATH;
    public static final PropertiesFile PROPERTIES;

    private SelectorUIController controller;

    static {
        USER_PATH = System.getProperty("user.home") + "/.my_utils/selector/";
        File userPath = new File(USER_PATH);
        if (!userPath.exists()) {
            userPath.mkdirs();
        }

        PROPERTIES = loadAppProperties();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Thread.setDefaultUncaughtExceptionHandler(this::catchException);

        controller = setUpPrimaryStage(primaryStage);
        controller.init();
    }

    @Override
    public void stop() throws InterruptedException, IOException {
        if (controller != null) {
            controller.shutDown();
        }
    }

    // private void catchException(Thread t, Throwable e) {
    // System.out.println(e.getMessage());
    //// e.printStackTrace();
    // }

    private static PropertiesFile loadAppProperties() {
        File propertiesFile = new File(USER_PATH + "app.properties");
        return new PropertiesFile(propertiesFile);
    }

    private SelectorUIController setUpPrimaryStage(Stage primaryStage) throws IOException {
        File uiFxmlFile = ResourceLoader.loadResource(
                "/selector/ui/xml/selector.fxml",
                USER_PATH + "xml/",
                "selector",
                "fxml");
        Objects.requireNonNull(uiFxmlFile);

        FXMLLoader loader = new FXMLLoader(uiFxmlFile.toURI().toURL());
        Parent root = loader.load();
        primaryStage.setTitle("Wheel Selector");
        primaryStage.setScene(new Scene(root, 550, 650));
        primaryStage.setResizable(false);
        primaryStage.show();

        return loader.getController();
    }
}
