package Restful;

import javafx.beans.property.SimpleStringProperty;

public class User {

    private final SimpleStringProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty email;
    private final SimpleStringProperty gender;
    private final SimpleStringProperty status;

    public User(String id, String name, String email, String gender, String status) {
        this.id = new SimpleStringProperty(String.valueOf(id));
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.gender = new SimpleStringProperty(gender);
        this.status = new SimpleStringProperty(status);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getGender() {
        return gender.get();
    }

    public SimpleStringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
