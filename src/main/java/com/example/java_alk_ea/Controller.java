package com.example.java_alk_ea;

import DataMining.Algs;
import DataMining.CrossValidation;
import DataMining.MachineLearn;
import Grafikus.Gep;
import Grafikus.GepCreate;
import Grafikus.Oprendszer;
import Grafikus.Processzor;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.List;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Utils;

import javax.net.ssl.HttpsURLConnection;

public class Controller {

    @FXML
    private Label lb1, lb2,lb3;
    @FXML
    private GridPane gp1,gpAlg;
    @FXML
    private TextField tfGyarto, tfTipus, tfKijelzo, tfMemoria, tfMerevlemez, tfVideovezerlo, tfAr, tfProcesszorgyarto, tfProcesszortipus, tfOprendszernev, tfDb,tfRead2;
    @FXML
    private GridPane gpUpdate,gpRead2;
    @FXML
    private ComboBox cb1,cb2;
    @FXML
    private Button btUpdate, btCreate, btDelete;
    @FXML
    private VBox vbDatabase,vbDataMining,vbRest1;


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
    private  TextField tf6,tf7,tf8,tf9,tf10,tf11,tf12,tf13,tf14,tf15,tf16;
    @FXML
    private TextArea ta1,ta2,ta3,ta4;

    static String token = "1db5e41713588809f524d82fc1713cb66e45c47dcb63e42b35e85c48f54202bb";
    HttpsURLConnection httpsURLConnection;
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
        vbDatabase.setVisible(false);
        vbRest1.setVisible(false);
        vbDataMining.setVisible(true);
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
        vbDatabase.setVisible(false);
        vbDatabase.setManaged(false);
        vbRest1.setVisible(false);
        vbRest1.setManaged(false);
        vbDataMining.setVisible(true);
        vbDataMining.setManaged(true);
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

}