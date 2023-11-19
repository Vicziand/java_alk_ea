package com.example.java_alk_ea;

import com.oanda.v20.Context;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import com.oanda.v20.account.AccountID;

import java.io.IOException;

public class MainApplication extends Application {
    public static SessionFactory factory;

    @Override
    public void start(Stage stage) throws IOException {
        //  FXML fájl betöltése
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Parent root = loader.load();

        // JavaFX alkalmazás inicializálása
        Scene scene = new Scene(root);
        stage.setWidth(1000);
        stage.setHeight(600);
        stage.setScene(scene);
        stage.show();

        Controller controller = loader.getController();
        controller.initializeSessionFactory(factory);

    }


    public static void main(String[] args) {

        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();
        Metadata metadata = new MetadataSources(standardRegistry).getMetadataBuilder().build();
        factory = metadata.getSessionFactoryBuilder().build();
        

        launch(args);
        }

    }
