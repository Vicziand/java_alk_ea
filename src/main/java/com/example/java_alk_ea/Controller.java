package com.example.java_alk_ea;

import Grafikus.Gep;
import Grafikus.GepCreate;
import Grafikus.Oprendszer;
import Grafikus.Processzor;
import javafx.application.Platform;
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

    @FXML private Label lb1,lb2;
    @FXML private GridPane gp1;
    @FXML private TextField tfGyarto, tfTipus, tfKijelzo,tfMemoria,tfMerevlemez,tfVideovezerlo,tfAr,tfProcesszorgyarto,tfProcesszortipus,tfOprendszernev,tfDb;
    @FXML private GridPane gpUpdate;
    @FXML private ComboBox cb1;
    @FXML private Button btUpdate,btCreate,btDelete;


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
        tfGyarto.clear();
        tfTipus.clear();
        tfKijelzo.clear();
        tfMemoria.clear();
        tfMerevlemez.clear();
        tfVideovezerlo.clear();
        tfAr.clear();
        tfProcesszorgyarto.clear();
        tfProcesszortipus.clear();
        tfOprendszernev.clear();
        tfDb.clear();
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
        tv1.setVisible(false);
        tv1.setManaged(false);
        btCreate.setVisible(true);
        btCreate.setManaged(true);
        btUpdate.setVisible(false);

    }

// Create metódus segítségével adjuk hozzá az adatokat az adatbázishoz
    void Create(){
        try (Session session = factory.openSession()) {
            Transaction t = session.getTransaction();
            try {
                if (!t.isActive()) {
                    t = session.beginTransaction();
                }

                Query<Processzor> processzorQuery = session.createQuery("FROM Processzor WHERE Gyarto = :gyarto AND Tipus = :tipus", Processzor.class);
                processzorQuery.setParameter("gyarto", tfProcesszorgyarto.getText());
                processzorQuery.setParameter("tipus", tfProcesszortipus.getText());
                Processzor proc = processzorQuery.uniqueResult();

                if (proc == null) {
                    proc = new Processzor(tfProcesszorgyarto.getText(), tfProcesszortipus.getText());
                    session.save(proc);
                }

                Query<Oprendszer> oprendszerQuery = session.createQuery("FROM Oprendszer WHERE Nev = :nev", Oprendszer.class);
                oprendszerQuery.setParameter("nev", tfOprendszernev.getText());
                Oprendszer opr = oprendszerQuery.uniqueResult();

                if (opr == null) {
                    opr = new Oprendszer(tfOprendszernev.getText());
                    session.save(opr);
                }

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
        ElemekTörlése();
        Create();
        lb1.setVisible(true);
        lb1.setManaged(true);
        lb1.setText("Adatok beírva az adatbázisba");
    }


    @FXML protected void menuUpdateClick() {
        gpUpdate.setVisible(true);
        gpUpdate.setManaged(true);
        ElemekTörlése();
        tv1.setVisible(false);
        tv1.setManaged(false);
        gp1.setVisible(false);
        gp1.setManaged(false);
        btCreate.setVisible(false);
        initializeCbWithIds();
    }


    public void initializeCbWithIds() {
        btUpdate.setVisible(false);
        btUpdate.setManaged(false);
        Transaction t = null;
        if (factory != null) {
            try (Session session = factory.openSession()) {
                t = session.beginTransaction();
                ElemekTörlése();
                List<Integer> ids = session.createQuery("SELECT id FROM Gep", Integer.class).list();
                ObservableList<Integer> idList = FXCollections.observableArrayList(ids);
                cb1.setItems(idList);
                t.commit();
            } catch (HibernateException e) {
                if (t != null && t.isActive()) {
                    t.rollback();
                }
                e.printStackTrace();
            }
        }
    }

    public void handleComboBoxSelection() {
        gp1.setVisible(true);
        gp1.setManaged(true);
        btUpdate.setVisible(true);
        btUpdate.setManaged(true);
        btCreate.setVisible(false);
        btCreate.setManaged(false);
        gpUpdate.setVisible(false);
        gpUpdate.setManaged(false);
        Integer selectedId = (Integer) cb1.getValue();
        if (selectedId != null) {
            try (Session session = factory.openSession()) {

                Gep gep = session.get(Gep.class, selectedId);
                tfGyarto.setText(gep.getGyarto());
                tfTipus.setText(gep.getTipus());
                tfKijelzo.setText(String.valueOf(gep.getKijelzo()));
                tfMemoria.setText(String.valueOf(gep.getMemoria()));
                tfMerevlemez.setText(String.valueOf(gep.getMerevlemez()));
                tfVideovezerlo.setText(gep.getVideovezerlo());
                tfAr.setText(String.valueOf(gep.getAr()));
                tfProcesszorgyarto.setText(gep.getProcesszor().getGyarto());
                tfProcesszortipus.setText(gep.getProcesszor().getTipus());
                tfOprendszernev.setText(gep.getOprendszer().getNev());
                tfDb.setText(String.valueOf(gep.getDb()));

            } catch (HibernateException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML protected void btUpdateClick() {
        Integer selectedId = (Integer) cb1.getValue();
        if (selectedId != null) {
            try (Session session = factory.openSession()) {
                Transaction t = session.beginTransaction();

                Query<Processzor> processzorQuery = session.createQuery("FROM Processzor WHERE Gyarto = :gyarto AND Tipus = :tipus", Processzor.class);
                processzorQuery.setParameter("gyarto", tfProcesszorgyarto.getText());
                processzorQuery.setParameter("tipus", tfProcesszortipus.getText());
                Processzor processzor = processzorQuery.uniqueResult();

                Query<Oprendszer> oprendszerQuery = session.createQuery("FROM Oprendszer WHERE Nev = :nev", Oprendszer.class);
                oprendszerQuery.setParameter("nev", tfOprendszernev.getText());
                Oprendszer oprendszer = oprendszerQuery.uniqueResult();

                String gyarto = tfGyarto.getText();
                String tipus = tfTipus.getText();
                double kijelzo = Double.parseDouble(tfKijelzo.getText());
                int memoria = Integer.parseInt(tfMemoria.getText());
                int merevlemez = Integer.parseInt(tfMerevlemez.getText());
                String videovezerlo = tfVideovezerlo.getText();
                int ar = Integer.parseInt(tfAr.getText());
                int processzorId = processzor.getId();
                int oprendszerid = oprendszer.getId();
                int db = Integer.parseInt(tfDb.getText());

                    Gep gep = session.get(Gep.class, selectedId);

                    gep.setGyarto(gyarto);
                    gep.setTipus(tipus);
                    gep.setKijelzo(kijelzo);
                    gep.setMemoria(memoria);
                    gep.setMerevlemez(merevlemez);
                    gep.setVideovezerlo(videovezerlo);
                    gep.setAr(ar);
                    gep.setProcesszorid(processzorId);
                    gep.setOprendszerid(oprendszerid);
                    gep.setDb(db);

                session.update(gep);
                t.commit();
                btUpdate.setVisible(false);
                btUpdate.setManaged(false);
                gp1.setVisible(false);
                gp1.setManaged(false);
                lb2.setVisible(true);
                lb2.setManaged(true);
                lb1.setVisible(true);
                lb1.setManaged(true);
                lb1.setText("Adatok módosítva az adatbázisban");

            } catch (HibernateException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML protected void menuDeleteClick() {
        gpUpdate.setVisible(true);
        gpUpdate.setManaged(true);
        tv1.setVisible(false);
        tv1.setManaged(false);
        initializeCbWithIds();


    }

    @FXML protected void btDeleteClick() {
        Integer selectedId = (Integer) cb1.getValue();
        if (selectedId != null) {
            try (Session session = factory.openSession()) {
                Transaction t = session.beginTransaction();

                Gep gep = session.get(Gep.class, selectedId);
                session.delete(gep);

                t.commit();

                lb2.setVisible(true);
                lb2.setManaged(true);
                lb2.setText("Rekord törölve az adatbázisból");
            } catch (HibernateException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML protected void menuCreateClickRest(){}

}