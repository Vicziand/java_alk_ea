package Grafikus;

import javax.persistence.*;

@Entity
@Table(name = "gep")

public class GepCreate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String gyarto;
    private String tipus;
    private double kijelzo;
    private int memoria;
    private int merevlemez;
    private String videovezerlo;
    private int ar;
    private int processzorId;
    private int oprendszerId;
    private int db;

    public GepCreate( String gyarto, String tipus, double kijelzo, int memoria, int merevlemez, String videovezerlo, int ar, int processzorId, int oprendszerId, int db) {

        this.gyarto = gyarto;
        this.tipus = tipus;
        this.kijelzo = kijelzo;
        this.memoria = memoria;
        this.merevlemez = merevlemez;
        this.videovezerlo = videovezerlo;
        this.ar = ar;
        this.processzorId = processzorId;
        this.oprendszerId = oprendszerId;
        this.db = db;
    }

    public GepCreate() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGyarto() {
        return gyarto;
    }

    public void setGyarto(String gyarto) {
        this.gyarto = gyarto;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }

    public double getKijelzo() {
        return kijelzo;
    }

    public void setKijelzo(double kijelzo) {
        this.kijelzo = kijelzo;
    }

    public int getMemoria() {
        return memoria;
    }

    public void setMemoria(int memoria) {
        this.memoria = memoria;
    }

    public int getMerevlemez() {
        return merevlemez;
    }

    public void setMerevlemez(int merevlemez) {
        this.merevlemez = merevlemez;
    }

    public String getVideovezerlo() {
        return videovezerlo;
    }

    public void setVideovezerlo(String videovezerlo) {
        this.videovezerlo = videovezerlo;
    }

    public int getAr() {
        return ar;
    }

    public void setAr(int ar) {
        this.ar = ar;
    }

    public int getProcesszorId() {
        return processzorId;
    }

    public void setProcesszorId(int processzorId) {
        this.processzorId = processzorId;
    }

    public int getOprendszerId() {
        return oprendszerId;
    }

    public void setOprendszerId(int oprendszerId) {
        this.oprendszerId = oprendszerId;
    }

    public int getDb() {
        return db;
    }

    public void setDb(int db) {
        this.db = db;
    }
}
