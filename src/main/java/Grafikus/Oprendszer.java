package Grafikus;

import javax.persistence.*;

@Entity
@Table(name = "oprendszer")

public class Oprendszer {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int Id;
    @Column(name = "nev")
    public String Nev;

    public Oprendszer(String nev) {
        Nev = nev;
    }

    public Oprendszer(){

    }

    public int getId() {
        return Id;
    }
    public void setId(int id) {
        Id = id;
    }
    public String getNev() {
        return Nev;
    }
    public void setNev(String nev) {
        Nev = nev;
    }
}
