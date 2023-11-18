package com.example.java_alk_ea;

import DataMining.Algs;
import DataMining.CrossValidation;
import DataMining.MachineLearn;
import Grafikus.Gep;
import Grafikus.GepCreate;
import Grafikus.Oprendszer;
import Grafikus.Processzor;
import Oanda.Config;
import com.oanda.v20.Context;
import com.oanda.v20.ContextBuilder;
import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.account.AccountSummary;
import com.oanda.v20.order.MarketOrderRequest;
import com.oanda.v20.order.OrderCreateRequest;
import com.oanda.v20.order.OrderCreateResponse;
import com.oanda.v20.pricing.ClientPrice;
import com.oanda.v20.pricing.PricingGetRequest;
import com.oanda.v20.pricing.PricingGetResponse;
import com.oanda.v20.primitives.DateTime;
import com.oanda.v20.primitives.InstrumentName;
import com.oanda.v20.trade.Trade;
import com.oanda.v20.trade.TradeCloseRequest;
import com.oanda.v20.trade.TradeSpecifier;
import com.oanda.v20.transaction.TransactionID;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Utils;
import weka.core.stopwords.Null;

import javax.net.ssl.HttpsURLConnection;

public class Controller {

    @FXML
    private Label lb1, lb2,lb3,lb4,lbActPrice;
    @FXML
    private GridPane gp1,gpAlg,gpActPrice;
    @FXML
    private TextField tfGyarto, tfTipus, tfKijelzo, tfMemoria, tfMerevlemez, tfVideovezerlo, tfAr, tfProcesszorgyarto, tfProcesszortipus, tfOprendszernev, tfDb,tfRead2;
    @FXML
    private GridPane gpUpdate,gpRead2,gpPositionClose;
    @FXML
    private ComboBox cb1,cb2,cbActPrice;
    @FXML
    private Button btUpdate, btCreate, btDelete;
    @FXML
    private VBox vbDatabase,vbDataMining,vbRest1,vbOthers,vbForex;


    @FXML
    private TableView tv1;
    @FXML
    private TableColumn<Gep, String> IDCol;
    @FXML
    private TableColumn<Gep, String> gyartoCol;
    @FXML
    private TableColumn<Gep, String> tipusCol;
    @FXML
    private TableColumn<Gep, String> kijelzoCol;
    @FXML
    private TableColumn<Gep, String> memoriaCol;
    @FXML
    private TableColumn<Gep, String> merevlemezCol;
    @FXML
    private TableColumn<Gep, String> videovezerloCol;
    @FXML
    private TableColumn<Gep, String> arCol;
    @FXML
    private TableColumn<Gep, String> processzorgyartoCol;
    @FXML
    private TableColumn<Gep, String> processzortipusCol;
    @FXML
    private TableColumn<Gep, String> oprendszernevCol;

    @FXML
    private TableColumn<Gep, String> dbCol;

    @FXML
    private GridPane gr6,gr7,gr8,gr9;
    @FXML
    private  TextField tf6,tf7,tf8,tf9,tf10,tf11,tf12,tf13,tf14,tf15,tf16,tfTrade;
    @FXML
    private TextArea ta1,ta2,ta3,ta4;

    @FXML
    private Button startButton, stopButton;

    @FXML
    private Label label1, label2;

    @FXML
    private TableView<AccountSummary> tvForex;
    @FXML
    private TableView<Trade> tvOpenedTrade;
    @FXML
    private TableColumn<Trade, String> IdCol3,InstrumentCol3,OpenedTimeCol3,CurrentUnitsCol3,PriceCol3,UnrealizedPLCol3;
    @FXML
    private TableColumn<AccountSummary, String> AliasCol2,IDCol2,CurrencyCol2,BalanceCol2,CreatedByCol2,CreatedTimeCol2,GuarantedCol2,PlCol2;
    @FXML
    private TextArea taActPrice;

    static String token = "1db5e41713588809f524d82fc1713cb66e45c47dcb63e42b35e85c48f54202bb";
    HttpsURLConnection httpsURLConnection;
    SessionFactory factory;
    static Context ctx;
    static AccountID accountId;

    private volatile boolean stopThreads = false;

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

    @FXML
    protected void Read() {
        tv1.setVisible(true);
        tv1.setManaged(true);
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

        tv1.getColumns().addAll(IDCol, gyartoCol, tipusCol, kijelzoCol, memoriaCol, merevlemezCol, videovezerloCol, arCol, processzorgyartoCol, processzortipusCol, oprendszernevCol, dbCol);
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

    }


    //A menuReadClick metódus megjeleníti az adatokat a TableView-ben, és újra előkészíti a TableView oszlopait a friss adatok megjelenítésére.
    @FXML
    protected void menuReadClick() {

        try (Session session = factory.openSession()) {
            Transaction t = session.beginTransaction();
            ElemekTörlése();

            Read();


            List<Gep> lista = session.createQuery("FROM Gep").list();
            tv1.getItems().setAll(lista);
            t.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }


        vbDataMining.setManaged(false);
        vbDataMining.setVisible(false);
        vbRest1.setVisible(false);
        vbRest1.setManaged(false);
        gp1.setVisible(false);
        gp1.setManaged(false);
        gpUpdate.setVisible(false);
        gpUpdate.setManaged(false);
        btDelete.setVisible(false);
        btUpdate.setVisible(false);
        gpRead2.setManaged(false);
        gpRead2.setVisible(false);

    }

    //A menuCreateClick metódus megjeleníti az új adatok beviteléhez a GridPane-t
    @FXML
    protected void menuCreateClick() {
        ElemekTörlése();
        vbDatabase.setVisible(true);
        vbRest1.setVisible(false);
        vbDataMining.setVisible(false);
        gp1.setVisible(true);
        gp1.setManaged(true);
        gpUpdate.setVisible(false);
        gpUpdate.setManaged(false);
        tv1.setVisible(false);
        tv1.setManaged(false);
        btCreate.setVisible(true);
        btCreate.setManaged(true);
        btUpdate.setVisible(false);
        btDelete.setVisible(false);
        gpRead2.setManaged(false);
        gpRead2.setVisible(false);

    }

    // Create metódus segítségével adjuk hozzá az adatokat az adatbázishoz
    void Create() {
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
    @FXML
    void btCreateClick() {
        ElemekTörlése();
        Create();
        lb1.setVisible(true);
        lb1.setManaged(true);
        lb1.setText("Adatok beírva az adatbázisba");
    }


    @FXML
    protected void menuUpdateClick() {
        vbDatabase.setVisible(true);
        vbRest1.setVisible(false);
        vbDataMining.setVisible(false);
        gpUpdate.setVisible(true);
        gpUpdate.setManaged(true);
        ElemekTörlése();
        tv1.setVisible(false);
        tv1.setManaged(false);
        gp1.setVisible(false);
        gp1.setManaged(false);
        btCreate.setVisible(false);
        initializeCbWithIds();
        btUpdate.setVisible(true);
        btUpdate.setManaged(true);
        btDelete.setVisible(false);
        btDelete.setManaged(false);
        gpRead2.setManaged(false);
        gpRead2.setVisible(false);
    }


    public void initializeCbWithIds() {
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


    @FXML
    protected void btUpdateClick() {
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


    @FXML
    protected void menuDeleteClick() {
        gpUpdate.setVisible(true);
        gpUpdate.setManaged(true);
        btUpdate.setVisible(false);
        gp1.setVisible(false);
        gp1.setManaged(false);
        tv1.setVisible(false);
        tv1.setManaged(false);
        initializeCbWithIds();
        btDelete.setVisible(true);
        btDelete.setManaged(true);
        gpRead2.setManaged(false);
        gpRead2.setVisible(false);

    }

    @FXML
    protected void btDeleteClick() {
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

    @FXML protected void menuRead2Click(){
        vbRest1.setVisible(false);
        vbRest1.setManaged(false);
        vbDataMining.setVisible(false);
        vbDatabase.setManaged(false);
        gpUpdate.setVisible(false);
        gpUpdate.setManaged(false);
        btDelete.setManaged(false);
        btDelete.setVisible(false);
        btUpdate.setVisible(false);
        btUpdate.setManaged(false);
        gp1.setVisible(false);
        gp1.setManaged(false);
        tv1.setVisible(false);
        tv1.setManaged(false);
        gpRead2.setVisible(true);
        gpRead2.setManaged(true);


        try (Session session = factory.openSession()) {
            Transaction t = session.beginTransaction();
            ElemekTörlése();

            Read(); // TableView inicializálása

            String gyartoValue = tfRead2.getText();
            if (!gyartoValue.isEmpty()) {
                Query<Gep> query = session.createQuery("FROM Gep WHERE Gyarto = :gyarto", Gep.class);
                query.setParameter("gyarto", gyartoValue);

                List<Gep> lista = query.list();
                tv1.getItems().setAll(lista);
                System.out.println(gyartoValue);
            }
            else{
                System.out.println("Üres");
            }
            t.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    // 2. feladat Rest

    protected void clearControlUIData(TextField... tfList) {
        for(TextField tf : tfList) tf.setText("");
    }
    protected void segéd1(){
        httpsURLConnection.setRequestProperty("Content-Type", "application/json");
        httpsURLConnection.setRequestProperty("Authorization", "Bearer " + token);
        httpsURLConnection.setUseCaches(false);
        httpsURLConnection.setDoOutput(true);
    }
    protected void segéd2(String params) throws IOException {
        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(httpsURLConnection.getOutputStream(), "UTF-8"));
        wr.write(params);
        wr.close();
        httpsURLConnection.connect();
    }
    protected String segéd3(int code) throws IOException {
        int statusCode = httpsURLConnection.getResponseCode();
        System.out.println("statusCode: "+statusCode);
        if (statusCode == code) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
            StringBuffer jsonResponseData = new StringBuffer();
            String readLine = null;
            while ((readLine = in.readLine()) != null)
                jsonResponseData.append(readLine);
            in.close();
            httpsURLConnection.disconnect();
            return jsonResponseData.toString();
        } else {
            httpsURLConnection.disconnect();
            return "Hiba!!!";
        }
    }
    @FXML
    protected void rest1MenuCreateClick() {
        vbRest1.setManaged(true);
        vbRest1.setVisible(true);
        ElemekTörlése();
        clearControlUIData(tf7, tf8, tf9, tf10);
        ta2.setText("");
        gr7.setVisible(true);
        gr7.setManaged(true);
        gr6.setVisible(false);
        gr6.setManaged(false);
        gr8.setVisible(false);
        gr8.setManaged(false);
        gr9.setVisible(false);
        gr9.setManaged(false);
        vbDatabase.setManaged(false);
        vbDatabase.setVisible(false);
        vbDataMining.setVisible(false);
        vbDataMining.setManaged(false);
    }
    @FXML
    protected void btnRest1MenuCreateClick() throws IOException {
        ta2.setText("");
        URL postUrl = new URL("https://gorest.co.in/public/v1/users");
        httpsURLConnection = (HttpsURLConnection) postUrl.openConnection();
        httpsURLConnection.setRequestMethod("POST");
        segéd1();
        String name = tf7.getText();
        String gender = tf8.getText();
        String email = tf9.getText();
        String status = tf10.getText();
        String params = "{\"name\":\""+name+"\", \"gender\":\""+gender+"\", \"email\":\""+email+"\", \"status\":\""+status+"\"}";
        segéd2(params);
        String response = segéd3(HttpsURLConnection.HTTP_CREATED);
        if(!response.equals("Hiba!!!")) {
            ta2.setText(response);
        } else {
            ta2.setText("Az új user létrehozása sajnos nem sikerült.");
        }
    }
    @FXML
    protected void rest1MenuReadClick() {
        vbRest1.setManaged(true);
        vbRest1.setVisible(true);
        ElemekTörlése();
        clearControlUIData(tf6);
        ta1.setText("");
        gr6.setVisible(true);
        gr6.setManaged(true);
        gr7.setVisible(false);
        gr7.setManaged(false);
        gr8.setVisible(false);
        gr8.setManaged(false);
        gr9.setVisible(false);
        gr9.setManaged(false);
        vbDatabase.setManaged(false);
        vbDatabase.setVisible(false);
        vbDataMining.setVisible(false);
        vbDataMining.setManaged(false);
    }
    @FXML
    protected void btnRest1MenuReadClick() throws IOException {
        ta1.setText("");
        String url = "https://gorest.co.in/public/v1/users";
        String ID = tf6.getText();
        if(ID != null)
            url = url + "/" + ID;
        URL usersUrl = new URL(url);
        httpsURLConnection = (HttpsURLConnection) usersUrl.openConnection();
        httpsURLConnection.setRequestMethod("GET");
        if(ID != null)
            httpsURLConnection.setRequestProperty("Authorization", "Bearer " + token);
        String response = segéd3(HttpsURLConnection.HTTP_OK);
        if(!response.equals("Hiba!!!")) {
            ta1.setText(response);
        } else {
            ta1.setText("Nincs user ilyen ID-val az adatbázisban.");
        }
    }
    @FXML
    protected void rest1MenuUpdateClick() {
        ElemekTörlése();
        clearControlUIData(tf11, tf12, tf13, tf14, tf15);
        vbRest1.setManaged(true);
        vbRest1.setVisible(true);
        ta3.setText("");
        gr7.setVisible(false);
        gr7.setManaged(false);
        gr6.setVisible(false);
        gr6.setManaged(false);
        gr9.setVisible(false);
        gr9.setManaged(false);
        gr8.setVisible(true);
        gr8.setManaged(true);
        vbDatabase.setManaged(false);
        vbDatabase.setVisible(false);
        vbDataMining.setVisible(false);
        vbDataMining.setManaged(false);
    }
    @FXML
    protected void btnRest1MenuUpdateClick() throws IOException {
        ta3.setText("");
        String ID = tf11.getText();
        String name = tf12.getText();
        String gender = tf13.getText();
        String email = tf14.getText();
        String status = tf15.getText();
        String url = "https://gorest.co.in/public/v1/users"+"/"+ID;
        URL postUrl = new URL(url);
        httpsURLConnection = (HttpsURLConnection) postUrl.openConnection();
        httpsURLConnection.setRequestMethod("PUT");
        segéd1();
        String params = "{\"name\":\""+name+"\", \"gender\":\""+gender+"\", \"email\":\""+email+"\", \"status\":\""+status+"\"}";
        segéd2(params);
        String response = segéd3(HttpsURLConnection.HTTP_OK);
        if(!response.equals("Hiba!!!")) {
            ta3.setText(response);
        } else {
            ta3.setText("A user módosítása sajnos nem sikerült.");
        }
    }
    @FXML
    protected void rest1MenuDeleteClick() {
        ElemekTörlése();
        vbRest1.setManaged(true);
        vbRest1.setVisible(true);
        clearControlUIData(tf16);
        ta4.setText("");
        gr9.setVisible(true);
        gr9.setManaged(true);
        gr7.setVisible(false);
        gr7.setManaged(false);
        gr6.setVisible(false);
        gr6.setManaged(false);
        gr8.setVisible(false);
        gr8.setManaged(false);
        vbDatabase.setManaged(false);
        vbDatabase.setVisible(false);
        vbDataMining.setVisible(false);
        vbDataMining.setManaged(false);
    }
    @FXML
    protected void btnRest1MenuDeleteClick() throws IOException {
        ta4.setText("");
        String ID = tf16.getText();
        String url = "https://gorest.co.in/public/v1/users"+"/"+ID;
        URL postUrl = new URL(url);
        httpsURLConnection = (HttpsURLConnection) postUrl.openConnection();
        httpsURLConnection.setRequestMethod("DELETE");
        segéd1();
        String response = segéd3(HttpsURLConnection.HTTP_NO_CONTENT);
        if(!response.equals("Hiba!!!")) {
            ta4.setText("Sikeresen törölte a user-t!");
        } else {
            ta4.setText("A user törlése sajnos nem sikerült.");
        }
    }

    // 4. Feladat Adatbányászat
    @FXML
    protected void menuDecisionTree() {
        vbOthers.setVisible(false);
        vbOthers.setManaged(false);
        vbDatabase.setVisible(false);
        vbRest1.setVisible(false);
        vbDataMining.setVisible(true);
        gpAlg.setVisible(false);
        gpAlg.setManaged(false);
        String file = "src/main/java/DataMining/ionosphere.arff";
        int classIndex=34;	// 34. oszlopot kell előre jelezni
        new MachineLearn(file, classIndex);

        showAlert("Az adatok sikeresen kiírva a Döntési fa.txt fájlba!");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sikeres kiíratás");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void menuAlgorithms() throws FileNotFoundException {
        vbOthers.setVisible(false);
        vbOthers.setManaged(false);
        vbDatabase.setVisible(false);
        vbDatabase.setManaged(false);
        vbRest1.setVisible(false);
        vbRest1.setManaged(false);
        vbDataMining.setVisible(true);
        vbDataMining.setManaged(true);
        gpAlg.setVisible(false);
        gpAlg.setManaged(false);
        PrintWriter kiir;
        List<String> algorithms = new ArrayList<>();
        List<Integer> correct = new ArrayList<>();
        try {
            kiir = new PrintWriter("Gépi tanulás.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String file = "src/main/java/DataMining/ionosphere.arff";
        int classIndex = 34;    // 34. oszlopot kell előre jelezni
        new MachineLearn(file, classIndex);
        CrossValidation j48 = new CrossValidation(file, classIndex, new J48(), kiir);
        algorithms.add(j48.getAlgorithm());
        correct.add(j48.getCorrect());

        CrossValidation sm0 = new CrossValidation(file, classIndex, new SMO(),kiir);
        algorithms.add(sm0.getAlgorithm());
        correct.add(sm0.getCorrect());

        CrossValidation naiveBayes = new CrossValidation(file, classIndex, new NaiveBayes(),kiir);
        algorithms.add(naiveBayes.getAlgorithm());
        correct.add(naiveBayes.getCorrect());

        IBk classifier = new IBk();
// 10 legközelebbi szomszéd:
        try {
            classifier.setOptions(Utils.splitOptions("-K 10"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        CrossValidation ibk = new CrossValidation(file, classIndex, classifier,kiir);
        algorithms.add(ibk.getAlgorithm());
        correct.add(ibk.getCorrect());

        CrossValidation randomForest = new CrossValidation(file, classIndex, new RandomForest(),kiir);
        algorithms.add(randomForest.getAlgorithm());
        correct.add(randomForest.getCorrect());

        kiir.close();

        int maxCorrect = 0;
        for (int i = 0; i < algorithms.size(); i++){
            if(correct.get(maxCorrect) < correct.get(i)){
                maxCorrect = i;
            }
        }

        showAlert("A legjobb Correctly Classified Instances eredményű algoritmus: "+algorithms.get(maxCorrect));

    }

    @FXML
    private void menuAlgorithms2(){
        vbOthers.setVisible(false);
        vbOthers.setManaged(false);
        vbDatabase.setVisible(false);
        vbDatabase.setManaged(false);
        vbRest1.setVisible(false);
        vbRest1.setManaged(false);
        vbDataMining.setVisible(true);
        vbDataMining.setManaged(true);
        gpAlg.setVisible(true);
        gpAlg.setManaged(true);
        String file = "src/main/java/DataMining/ionosphere.arff";
        int classIndex = 34;    // 34. oszlopot kell előre jelezni
        new MachineLearn(file, classIndex);

        cb2.getItems().clear();
        List<Algs> algorithms = new ArrayList<>();
        Algs j48 = new Algs(file, classIndex, new J48());
        algorithms.add(j48);
        Algs sm0 = new Algs(file, classIndex, new SMO());
        algorithms.add(sm0);
        Algs naiveBayes = new Algs(file, classIndex, new NaiveBayes());
        algorithms.add(naiveBayes);
        Algs ibk = new Algs(file, classIndex, new IBk());
        algorithms.add(ibk);
        Algs randomForest = new Algs(file, classIndex, new RandomForest());
        algorithms.add(randomForest);

        cb2.getItems().addAll(algorithms);
    }

    @FXML
    private void handleComboBoxSelection2(){
        Algs selectedAlgs = (Algs) cb2.getValue();

        if (selectedAlgs != null) {
            String message = String.format("TP: %s TN: %s FP: %s FN: %s \nCorrectly Classified Instances: %s \nIncorrectly Classified Instances: %s",
                    selectedAlgs.getTp(), selectedAlgs.getTn(), selectedAlgs.getFp(), selectedAlgs.getFn(), selectedAlgs.getCorrect(),selectedAlgs.getIncorrect());

            showAlert(message);
        }
    }


    // 5. feladat Párhuzamos, Stream(mégnincsmeg)
    @FXML
    private void menuParallel() {

        vbDataMining.setVisible(false);
        vbDataMining.setManaged(false);
        vbRest1.setVisible(false);
        vbRest1.setManaged(false);
        vbDatabase.setManaged(false);
        vbDatabase.setVisible(false);
        startButton.setVisible(true);
        startButton.setManaged(true);
        stopButton.setVisible(true);
        stopButton.setManaged(true);

        label1.setVisible(true);
        label1.setManaged(true);

        label2.setVisible(true);
        label2.setManaged(true);

    }

    @FXML
    public void startTasks() {
        Thread thread1 = new Thread(() -> {
            int count = 1;
            while (!stopThreads) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String text = "Label 1: " + count++;
                Platform.runLater(() -> label1.setText(text));
            }
        });

        Thread thread2 = new Thread(() -> {
            int count = 1;
            while (!stopThreads) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String text = "Label 2: " + count++;
                Platform.runLater(() -> label2.setText(text));
            }
        });

        thread1.setDaemon(true);
        thread2.setDaemon(true);

        thread1.start();
        thread2.start();
    }

    public void stopTasks() {
        stopThreads = true;
    }

//6. Forex menü
// Jelszó oanda.com: W+iDn5AWRtw6
// Account ID: 101-004-27439358-001
// Token: e585b16c2da277387afe97a6bca5c8b7-6dad5b819772ba63b38919783c15fffe

    public void visibleForex(){
        vbDataMining.setVisible(false);
        vbDataMining.setManaged(false);
        vbRest1.setVisible(false);
        vbRest1.setManaged(false);
        vbDatabase.setManaged(false);
        vbDatabase.setVisible(false);
        vbOthers.setManaged(false);
        vbOthers.setVisible(false);
    }

    @FXML
    public void menuAccountInfo(){
        visibleForex();
        gpActPrice.setManaged(false);
        gpActPrice.setVisible(false);
        tvForex.setVisible(true);
        tvForex.setManaged(true);
        tvOpenedTrade.setManaged(false);
        tvOpenedTrade.setVisible(false);
        gpPositionClose.setManaged(false);
        gpPositionClose.setVisible(false);
        Context ctx = new Context("https://api-fxpractice.oanda.com", Config.TOKEN);
        AccountSummary summary;
        try {
            summary = ctx.account.summary(new AccountID(Config.ACCOUNTID)).getAccount();
            System.out.println(summary);

            IDCol2 = new TableColumn<>("Id");
            AliasCol2 = new TableColumn<>("Alias");
            CurrencyCol2 = new TableColumn<>("Currency");
            BalanceCol2 = new TableColumn<>("Balance");
            CreatedByCol2 = new TableColumn<>("Created by");
            CreatedTimeCol2 = new TableColumn<>("Created time");
            GuarantedCol2 = new TableColumn<>("Guaranred SLO Mode");

            IDCol2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId().toString()));
            AliasCol2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlias()));
            CurrencyCol2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCurrency().toString()));
            BalanceCol2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBalance().toString()));
            CreatedByCol2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreatedByUserID().toString()));
            CreatedTimeCol2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreatedTime().toString()));
            GuarantedCol2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGuaranteedStopLossOrderMode().toString()));

            tvForex.getColumns().addAll(IDCol2,AliasCol2,CurrencyCol2,BalanceCol2,CreatedByCol2,CreatedTimeCol2,GuarantedCol2);


            ObservableList<AccountSummary> data = FXCollections.observableArrayList(summary);
            tvForex.setItems(data);


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("HIBA: " + e.getMessage());
        }

    }

    @FXML
    public void menuCurrentPrice() {
        visibleForex();
        gpActPrice.setManaged(true);
        gpActPrice.setVisible(true);
        tvForex.setVisible(false);
        tvForex.setManaged(false);
        tvOpenedTrade.setManaged(false);
        tvOpenedTrade.setVisible(false);
        gpPositionClose.setManaged(false);
        gpPositionClose.setVisible(false);
        Context ctx = new ContextBuilder(Config.URL).setToken(Config.TOKEN).setApplication("PricePolling").build();
        AccountID accountId = Config.ACCOUNTID;

        List<String> instruments = new ArrayList<>(Arrays.asList("EUR_USD", "USD_JPY", "GBP_USD", "USD_CHF"));
        ObservableList<String> instrumentsList = FXCollections.observableArrayList(instruments);
        cbActPrice.setItems(instrumentsList);

        cbActPrice.setOnAction(event -> handlePriceSelection(ctx, accountId));
    }


    private void handlePriceSelection(Context ctx, AccountID accountId) {
        String selectedInstrument = (String) cbActPrice.getValue();
        if (selectedInstrument != null) {
            // A háttérfolyamat definiálása
            Task<Void> backgroundTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    PricingGetRequest request = new PricingGetRequest(accountId, Collections.singletonList(selectedInstrument));
                    DateTime since = null;
                    while (!isCancelled()) {
                        if (since != null) {
                            //System.out.println("Polling since " + since);
                            DateTime finalSince = since;
                            Platform.runLater(() -> lbActPrice.setText("Polling since  " + finalSince));
                            request.setSince(since);
                        }
                        PricingGetResponse resp = ctx.pricing.get(request);
                        for (ClientPrice price : resp.getPrices()) {
                            if (price.getInstrument().equals(selectedInstrument)) {
                               // System.out.println(price);
                                cbActPrice.setVisible(false);
                                cbActPrice.setVisible(false);
                                lb4.setVisible(false);
                                lb4.setManaged(false);
                                Platform.runLater(() -> taActPrice.setText(price.toString()));
                            }
                        }
                        since = resp.getTime();
                        Thread.sleep(1000);
                    }
                    return null;
                }
            };

            // A háttérfolyamat indítása egy új szálon
            Thread backgroundThread = new Thread(backgroundTask);
            backgroundThread.setDaemon(true);
            backgroundThread.start();
        }
    }

    @FXML
    public void menuHistoricalPrice(){
        visibleForex();
    }

    @FXML
    public void menuPositionOpen() {
        visibleForex();
        gpActPrice.setVisible(true);
        gpActPrice.setManaged(true);
        tvForex.setVisible(false);
        tvForex.setManaged(false);
        taActPrice.setVisible(false);
        taActPrice.setManaged(false);
        tvOpenedTrade.setManaged(false);
        tvOpenedTrade.setVisible(false);
        gpPositionClose.setManaged(false);
        gpPositionClose.setVisible(false);
        ctx = new ContextBuilder(Config.URL).setToken(Config.TOKEN).setApplication("StepByStepOrder").build();
        accountId = Config.ACCOUNTID;
        List<String> instruments = new ArrayList<>(Arrays.asList("EUR_USD", "USD_JPY", "GBP_USD", "USD_CHF", "NZD_USD"));
        ObservableList<String> instrumentsList = FXCollections.observableArrayList(instruments);
        cbActPrice.setItems(instrumentsList);

        // ComboBox kiválasztásának figyelése
        cbActPrice.setOnAction(event -> {
            String selectedInstrument = (String) cbActPrice.getValue();
            if (selectedInstrument != null) {
                Nyitás(selectedInstrument);
                System.out.println("Kész");
            }
        });
    }

    static void Nyitás(String instrument){
        System.out.println("Place a Market Order");

        try {
            OrderCreateRequest request = new OrderCreateRequest(accountId);
            MarketOrderRequest marketorderrequest = new MarketOrderRequest();
            marketorderrequest.setInstrument(instrument);
// Ha pozitív, akkor LONG, ha negatív, akkor SHORT:
            marketorderrequest.setUnits(-10);
            request.setOrder(marketorderrequest);
            OrderCreateResponse response = ctx.order.create(request);
            System.out.println("tradeId: "+response.getOrderFillTransaction().getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void menuPositionClose() {
        visibleForex();
        ctx = new ContextBuilder(Config.URL).setToken(Config.TOKEN).setApplication("StepByStepOrder").build();
        accountId = Config.ACCOUNTID;
        gpPositionClose.setVisible(true);
        gpPositionClose.setManaged(true);
        tvForex.setVisible(false);
        tvForex.setManaged(false);
        taActPrice.setVisible(false);
        taActPrice.setManaged(false);
        gpActPrice.setVisible(false);
        gpActPrice.setManaged(false);
        tvOpenedTrade.setManaged(false);
        tvOpenedTrade.setVisible(false);


    }

    static void Zárás(int selectedId) {
        System.out.println("Close a Trade");
        if (ctx == null) {
            System.err.println("Context is not initialized. Cannot close trade.");
            return;
        }
        // Ide a zárni kívánt tradeId-t kell beírni:
        TransactionID tradeId = new TransactionID(Integer.toString(selectedId));
        try {
            ctx.trade.close(new TradeCloseRequest(accountId, new TradeSpecifier(tradeId.toString())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void btTradeClick() {
        try {
            int selectedId = Integer.parseInt(tfTrade.getText());
            System.out.println(selectedId);
            if (false) Nyitás("EUR_USD");
            if (true) Zárás(selectedId);
        } catch (NumberFormatException e) {
            System.err.println("Invalid trade ID format. Please enter a valid integer.");
        }
    }
    @FXML
    public void  menuOpenPositions() throws ExecuteException, RequestException {
        visibleForex();
        tvForex.setVisible(false);
        tvForex.setManaged(false);
        taActPrice.setVisible(false);
        taActPrice.setManaged(false);
        gpActPrice.setVisible(false);
        gpActPrice.setManaged(false);
        gpPositionClose.setManaged(false);
        gpPositionClose.setManaged(false);
        tvOpenedTrade.setManaged(true);
        tvOpenedTrade.setVisible(true);
        if(true) NyitotttradekKiír();

    }

    public void NyitotttradekKiír() throws ExecuteException, RequestException {
        ctx = new ContextBuilder(Config.URL).setToken(Config.TOKEN).setApplication("StepByStepOrder").build();
        accountId = Config.ACCOUNTID;
        System.out.println("Nyitott tradek:");
        List<Trade> trades = ctx.trade.listOpen(accountId).getTrades();
        for(Trade trade: trades) {
            System.out.println(trade);
        }
        for(Trade trade: trades) {
            System.out.println(trade.getId() + "\t" + trade.getInstrument() + "\t" + trade.getOpenTime() + "\t" + trade.getCurrentUnits() + "\t" + trade.getPrice() + "\t" + trade.getUnrealizedPL());
        }
        tvOpenedTrade.getColumns().clear();

        IdCol3 = new TableColumn<>("Id");
        InstrumentCol3 = new TableColumn<>("Instrument");
        OpenedTimeCol3 = new TableColumn<>("Open Time");
        CurrentUnitsCol3 = new TableColumn<>("Current Units");
        PriceCol3 = new TableColumn<>("Price");
        UnrealizedPLCol3 = new TableColumn<>("Unrealized PL");

        IdCol3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId().toString()));
        InstrumentCol3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInstrument().toString()));
        OpenedTimeCol3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOpenTime().toString()));
        CurrentUnitsCol3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCurrentUnits().toString()));
        PriceCol3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrice().toString()));
        UnrealizedPLCol3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUnrealizedPL().toString()));

        tvOpenedTrade.getColumns().addAll(IdCol3,InstrumentCol3,OpenedTimeCol3,CurrentUnitsCol3,PriceCol3,UnrealizedPLCol3);

        ObservableList<Trade> tradeList = FXCollections.observableArrayList(trades);
        tvOpenedTrade.setItems(tradeList);

    }




    }