package Grafikus;

import javax.persistence.*;

@Entity
@Table(name = "gep")

public class Gep {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int Id;
    @Column(name = "gyarto")
    public String Gyarto;
    @Column(name = "tipus")
    public String Tipus;
    @Column(name = "kijelzo")
    public double Kijelzo;
    @Column(name = "memoria")
    public int Memoria;
    @Column(name = "merevlemez")
    public int Merevlemez;
    @Column(name = "videovezerlo")
    public String Videovezerlo;
    @Column(name = "ar")
    public int Ar;
    @Column(name = "processzorid")
    public int Processzorid;
    @Column(name = "oprendszerid")
    public int Oprendszerid;
    @Column(name = "db")
    public int Db;
    @OneToOne
    @JoinColumn(name = "processzorid", insertable = false, updatable = false)
    public Processzor processzor;

    @OneToOne
    @JoinColumn(name = "oprendszerid", insertable = false, updatable = false)
    public Oprendszer oprendszer;


    public Gep(String gyarto, String tipus, double kijelzo, int memoria, int merevlemez, String videovezerlo, int ar,Processzor processzor,Oprendszer oprrendszer,int db) {
        Gyarto = gyarto;
        Tipus = tipus;
        Kijelzo = kijelzo;
        Memoria = memoria;
        Merevlemez = merevlemez;
        Videovezerlo = videovezerlo;
        Ar = ar;
        Db = db;
        this.processzor = processzor;
        this.oprendszer = oprendszer;
    }

    public Gep(){

    }

    public int getId() {
        return Id;
    }
    public String getGyarto() {
        return Gyarto;
    }
    public String getTipus() {
        return Tipus;
    }
    public double getKijelzo() {
        return Kijelzo;
    }
    public int getMemoria() {
        return Memoria;
    }
    public int getMerevlemez() {
        return Merevlemez;
    }
    public String getVideovezerlo() {
        return Videovezerlo;
    }
    public int getAr() {
        return Ar;
    }
    public int getProcesszorid() {
        return Processzorid;
    }
    public int getOprendszerid() {
        return Oprendszerid;
    }
    public int getDb() {
        return Db;
    }
    public Processzor getProcesszor() {
        return processzor;
    }
    public Oprendszer getOprendszer() {
        return oprendszer;
    }

    public void setId(int id) {
        Id = id;
    }
    public void setGyarto(String gyarto) {
        Gyarto = gyarto;
    }
    public void setTipus(String tipus) {
        Tipus = tipus;
    }
    public void setKijelzo(double kijelzo) {
        Kijelzo = kijelzo;
    }
    public void setMemoria(int memoria) {
        Memoria = memoria;
    }
    public void setMerevlemez(int merevlemez) {
        Merevlemez = merevlemez;
    }
    public void setVideovezerlo(String videovezerlo) {
        Videovezerlo = videovezerlo;
    }
    public void setAr(int ar) {
        Ar = ar;
    }
    public void setProcesszorid(int processzorid) {
        Processzorid = processzorid;
    }
    public void setOprendszerid(int oprendszerid) {
        Oprendszerid = oprendszerid;
    }
    public void setDb(int db) {
        Db = db;
    }
    public void setProcesszor(Processzor processzor) {
        this.processzor = processzor;
    }
    public void setOprendszer(Oprendszer oprendszer) {
        this.oprendszer = oprendszer;
    }
}
