package com.zadyraichuk.apps;

import com.zadyraichuk.PropertiesFile;
import com.zadyraichuk.selector.RandomSelector;
import com.zadyraichuk.selector.RationalRandomSelector;
import com.zadyraichuk.selector.VariantsList;
import com.zadyraichuk.selector.controller.SelectorController;
import com.zadyraichuk.selector.controller.SelectorLogic;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class SelectorApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(this::catchException);

        SelectorController controller = setUpPrimaryStage(primaryStage);
        PropertiesFile properties = loadAppProperties();
        loadVariantCollections(properties);
        controller.init(properties);
    }

    private SelectorController setUpPrimaryStage(Stage primaryStage) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(Objects.requireNonNull(getClass().getResource("../../../selector/ui/xml/selector.fxml")));
        Parent root = loader.load();
        primaryStage.setTitle("Wheel Selector");
        primaryStage.setScene(new Scene(root, 550, 650));
        primaryStage.setResizable(false);
        primaryStage.show();

        return loader.getController();
    }

    private PropertiesFile loadAppProperties() throws IOException {
        File propertiesFile = new File(Objects.requireNonNull(getClass().getResource("../../../selector/app.properties")).getPath());
        return new PropertiesFile(propertiesFile);
    }

    private void loadVariantCollections(PropertiesFile properties) throws IOException {
        SelectorLogic appLogic = SelectorLogic.getInstance();
        File variantsDir = new File(Objects.requireNonNull(getClass().getResource("../../../selector/variants/")).getPath());
        appLogic.readVariantsFromDirectory(variantsDir);

        String selectedVariantsName = properties.getProperty("last.used.variants");
        VariantsList selectedVariants = appLogic.getVariantsList(selectedVariantsName);

        SelectorLogic.SelectorType type = SelectorLogic.SelectorType
                .valueOf(properties.getProperty("selector.type"));
        RandomSelector selector = new RandomSelector(selectedVariants);
        if (type == SelectorLogic.SelectorType.RATIONAL) {
            selector = new RationalRandomSelector(selectedVariants);
        }
        appLogic.setSelector(selector);
    }

    private void catchException(Thread t, Throwable e) {
        System.out.println(e.getMessage());
//        e.printStackTrace();
    }

}
