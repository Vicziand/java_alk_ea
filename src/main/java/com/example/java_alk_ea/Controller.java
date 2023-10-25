package com.example.java_alk_ea;

import Grafikus.Gep;
import Grafikus.GepCreate;
import Grafikus.Oprendszer;
import Grafikus.Processzor;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.lang.module.Configuration;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML private Label lb1;
    @FXML private GridPane gp1;
    @FXML private TextField tfGyarto, tfTipus, tfKijelzo,tfMemoria,tfMerevlemez,tfVideovezerlo,tfAr,tfProcesszorgyarto,tfProcesszortipus,tfOprendszernev,tfDb;
    @FXML private GridPane gpUpdate;
    @FXML private ComboBox cb1;

    @FXML private TableView tv1;
    @FXML private TableColumn<Gep, String> IDCol;
    @FXML private TableColumn<Gep, String> gyartoCol;
    @FXML private TableColumn<Gep, String> tipusCol;
    @FXML private TableColumn<Gep, String> kijelzoCol;
    @FXML private TableColumn<Gep, String> memoriaCol;
    @FXML private TableColumn<Gep, String> merevlemezCol;
    @FXML private TableColumn<Gep, String> videovezerloCol;
    @FXML private TableColumn<Gep, String> arCol;
    @FXML private TableColumn<Gep, String> processzorgyartoCol;
    @FXML private TableColumn<Gep, String> processzortipusCol;
    @FXML private TableColumn<Gep, String> oprendszernevCol;

    @FXML private TableColumn<Gep, String> dbCol;

    SessionFactory factory;

// SessionFactory inicializálása az adatbázis műveletekhez
    public void initializeSessionFactory(SessionFactory factory) {
        this.factory = factory;
    }

//A ElemekTörlése metódus  a TableView (tv1) táblázatban  előző elemeit törli.
    private void ElemekTörlése() {
        tv1.getItems().clear();
    }


    //A menuReadClick metódus megjeleníti az adatokat a TableView-ben, és újra előkészíti a TableView oszlopait a friss adatok megjelenítésére.
    @FXML protected void menuReadClick() {
        try (Session session = factory.openSession()) {
            Transaction t = session.beginTransaction();
        ElemekTörlése();
        tv1.setVisible(true);
        tv1.setManaged(true);
        tv1.getColumns().removeAll(tv1.getColumns());
        IDCol = new TableColumn("Id");
        gyartoCol = new TableColumn("Gyártó");
        tipusCol = new TableColumn("Típus");
        kijelzoCol = new TableColumn("Kijelző");
        memoriaCol = new TableColumn("Memória");
        merevlemezCol = new TableColumn("Merevlemez");
        videovezerloCol = new TableColumn("Videóvezérlő");
        arCol = new TableColumn("Ár");
        processzorgyartoCol = new TableColumn("Processzor gyártó");
        processzortipusCol = new TableColumn("Processzor típus");
        oprendszernevCol = new TableColumn("Op. rendszer név");
        dbCol = new TableColumn("Darab");

        tv1.getColumns().addAll(IDCol, gyartoCol, tipusCol,kijelzoCol,memoriaCol,merevlemezCol,videovezerloCol,arCol,processzorgyartoCol,processzortipusCol,oprendszernevCol,dbCol);
        IDCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        gyartoCol.setCellValueFactory(new PropertyValueFactory<>("Gyarto"));
        tipusCol.setCellValueFactory(new PropertyValueFactory<>("Tipus"));
        kijelzoCol.setCellValueFactory(new PropertyValueFactory<>("Kijelzo"));
        memoriaCol.setCellValueFactory(new PropertyValueFactory<>("Memoria"));
        merevlemezCol.setCellValueFactory(new PropertyValueFactory<>("Merevlemez"));
        videovezerloCol.setCellValueFactory(new PropertyValueFactory<>("Videovezerlo"));
        arCol.setCellValueFactory(new PropertyValueFactory<>("Ar"));
        processzorgyartoCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProcesszor().getGyarto()));
        processzortipusCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProcesszor().getTipus()));
        oprendszernevCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOprendszer().getNev()));
        dbCol.setCellValueFactory(new PropertyValueFactory<>("Db"));

        tv1.getItems().clear();

            List<Gep> lista = session.createQuery("FROM Gep").list();
            tv1.getItems().setAll(lista);
            t.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        gp1.setVisible(false);
        gp1.setManaged(false);
        gpUpdate.setVisible(false);
        gpUpdate.setManaged(false);

    }

//A menuCreateClick metódus megjeleníti az új adatok beviteléhez a GridPane-t
    @FXML protected void menuCreateClick() {
        ElemekTörlése();
        gp1.setVisible(true);
        gp1.setManaged(true);
        gpUpdate.setVisible(false);
        gpUpdate.setManaged(false);
    }

// Create metódus segítségével adjuk hozzá az adatokat az adatbázishoz
    void Create(){
        try (Session session = factory.openSession()) {
            Transaction t = session.getTransaction();
            try {
                if (!t.isActive()) {
                    t = session.beginTransaction();
                }

        Processzor proc = new Processzor(tfProcesszorgyarto.getText(), tfProcesszortipus.getText());
        session.save(proc);
        Oprendszer opr = new Oprendszer(tfOprendszernev.getText());
        session.save(opr);

        GepCreate gepcreate = new GepCreate(tfGyarto.getText(), tfTipus.getText(), Double.parseDouble(tfKijelzo.getText()),
                Integer.parseInt(tfMemoria.getText()), Integer.parseInt(tfMerevlemez.getText()),
                tfVideovezerlo.getText(), Integer.parseInt(tfAr.getText()), proc.getId(), opr.getId(),
                Integer.parseInt(tfDb.getText()));

        session.save(gepcreate);

        t.commit();
            } catch (Exception e) {
                if (t != null && t.isActive()) {
                    t.rollback();
                }
                e.printStackTrace();
            }
        }
    }

//bt1Click metódus a Küldés gomb megnyomása után a Create metódus meghívása
    @FXML void btCreateClick(){
        Create();
        ElemekTörlése();
        lb1.setVisible(true);
        lb1.setManaged(true);
        lb1.setText("Adatok beírva az adatbázisba");
    }


    @FXML protected void menuUpdateClick() {
        gpUpdate.setVisible(true);
        gpUpdate.setManaged(true);
        ElemekTörlése();
        initializeCbWithIds();

        tv1.setVisible(false);
        tv1.setManaged(false);
        gp1.setVisible(false);
        gp1.setManaged(false);
    }


    public void initializeCbWithIds() {
        Transaction t = null;
        if (factory != null) {
            try (Session session = factory.openSession()) {
                t = session.beginTransaction();

                List<Integer> ids = session.createQuery("SELECT id FROM Gep", Integer.class).list();
                ObservableList<Integer> idList = FXCollections.observableArrayList(ids);
                cb1.setItems(idList);

                ElemekTörlése();

                cb1.setOnAction(event -> {
                    Object selectedValue = cb1.getValue();
                    if (selectedValue != null && selectedValue instanceof Integer) {
                        Integer selectedId = (Integer) selectedValue;

                        Gep gep = session.get(Gep.class, selectedId);
                        if (gep != null) {
                            Processzor proc = gep.getProcesszor();
                            tfGyarto.setText(gep.getGyarto());
                            tfTipus.setText(gep.getTipus());
                            tfKijelzo.setText(String.valueOf(gep.getKijelzo()));
                            // ...
                            tfDb.setText(String.valueOf(gep.getDb()));
                        }
                    }
                });

                t.commit();
            } catch (HibernateException e) {
                if (t != null && t.isActive()) {
                    t.rollback();
                }
                e.printStackTrace();
            }
        }
    }


    @FXML protected void btUpdateClick() {

    }

    @FXML protected void menuDeleteClick() {}

    @FXML
    protected void menuCreateClickRest() {

    }

}