package com.example.java_alk_ea;

import DataMining.CrossValidation;
import DataMining.MachineLearn;
import Restful.RestClient;
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
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.RandomForest;
import weka.core.Utils;
import weka.classifiers.trees.J48;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;

import java.io.IOException;

public class MainApplication extends Application {
    public static SessionFactory factory;
    @Override
    public void start(Stage stage) throws IOException {
        //  FXML fájl betöltése
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();

        // JavaFX alkalmazás inicializálása
        Scene scene = new Scene(root);
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setScene(scene);
        stage.show();

        Controller controller = loader.getController();
        controller.initializeSessionFactory(factory);
        //controller.menuReadClick();

    }

    public static void main(String[] args) {

        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();
        Metadata metadata = new MetadataSources(standardRegistry).getMetadataBuilder().build();
        factory = metadata.getSessionFactoryBuilder().build();

        RestClient restClient = new RestClient();
       // try {
            //restClient.GET(null);
            // Emailnek egyedinek kell lenni!!!
            //restClient.POST("Horváth János", "male", "email21@data.hu","active");
            //String ID="3399";
            //restClient.GET(ID);
            //restClient.PUT(ID,"Horváth János2", "male", "email3@data.hu","active");
            //restClient.GET(ID);
            //restClient.DELETE(ID);
            //restClient.GET(ID);
            //restClient.GET(null);
       // } catch (IOException e) {
        //    e.printStackTrace();
        //}

       String file = "src/main/java/DataMining/ionosphere.arff";
        int classIndex=34;	// 34. oszlopot kell előre jelezni
        new MachineLearn(file, classIndex);
        new CrossValidation(file, classIndex, new J48());
        new CrossValidation(file, classIndex, new SMO());
        new CrossValidation(file, classIndex, new NaiveBayes());
        IBk classifier = new IBk();
// 10 legközelebbi szomszéd:
        try {
            classifier.setOptions(Utils.splitOptions("-K 10"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        new CrossValidation(file, classIndex, classifier);
        new CrossValidation(file, classIndex, new RandomForest());


        launch(args);
        }

    }
