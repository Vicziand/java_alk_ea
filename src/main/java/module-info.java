module com.example.java_alk_ea {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires java.naming;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires org.json;
    requires weka.stable;


    opens com.oanda.v20.pricing to com.google.gson;
    opens com.oanda.v20.pricing_common to com.google.gson;
    opens com.oanda.v20.primitives to com.google.gson;
    opens com.oanda.v20.order to com.google.gson;
    opens com.oanda.v20.account to com.google.gson;
    opens com.oanda.v20.transaction to com.google.gson;
    opens com.oanda.v20.instrument to com.google.gson;
    opens com.oanda.v20.trade to com.google.gson;
    opens com.oanda.v20 to com.google.gson;

    opens Grafikus;
    exports Grafikus;
    opens com.example.java_alk_ea to javafx.fxml;
    exports com.example.java_alk_ea;

    requires com.google.gson;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
}