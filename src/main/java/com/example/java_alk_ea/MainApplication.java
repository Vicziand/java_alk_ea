package com.example.java_alk_ea;

import Oanda.Config;
import com.oanda.v20.Context;
import com.oanda.v20.ContextBuilder;
import com.oanda.v20.account.AccountSummary;
import com.oanda.v20.instrument.Candlestick;
import com.oanda.v20.instrument.InstrumentCandlesRequest;
import com.oanda.v20.instrument.InstrumentCandlesResponse;
import com.oanda.v20.primitives.InstrumentName;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.pricing.ClientPrice;
import com.oanda.v20.pricing.PricingGetRequest;
import com.oanda.v20.pricing.PricingGetResponse;
import com.oanda.v20.primitives.DateTime;


import java.io.IOException;

import static com.oanda.v20.instrument.CandlestickGranularity.H1;

public class MainApplication extends Application {
    public static SessionFactory factory;
    static Context ctx;
    static AccountID accountId;
    @Override
    public void start(Stage stage) throws IOException {
        //  FXML fájl betöltése
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
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

/*
        Context ctx = new ContextBuilder(Config.URL).setToken(Config.TOKEN).setApplication("PricePolling").build();
        AccountID accountId = Config.ACCOUNTID;
        List<String> instruments = new ArrayList<>( Arrays.asList("EUR_USD", "USD_JPY", "GBP_USD", "USD_CHF"));
*/

/* Historikus Árak
        Context ctx = new ContextBuilder(Config.URL).setToken(Config.TOKEN).setApplication("HistorikusAdatok").build();
        try {
            InstrumentCandlesRequest request = new InstrumentCandlesRequest(new InstrumentName("EUR_USD"));
            request.setGranularity(H1);
            request.setCount(10L);	// 10 adat	L: long adattípus
            InstrumentCandlesResponse resp = ctx.instrument.candles(request);
            for(Candlestick candle: resp.getCandles())
                System.out.println(candle);
            for(Candlestick candle: resp.getCandles())
                System.out.println(candle.getTime()+"\t"+candle.getMid().getC());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("HIBA: " + e.getMessage());
        }
*/

        launch(args);
        }

    }
