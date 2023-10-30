module com.example.java_alk_ea {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires java.naming;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires org.json;
    opens Grafikus;
    exports Grafikus;
    opens com.example.java_alk_ea to javafx.fxml;
    exports com.example.java_alk_ea;
}