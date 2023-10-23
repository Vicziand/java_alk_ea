package Grafikus;
import javax.persistence.*;

    @Entity
    @Table(name = "processzor")

    public class Processzor {
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        public int Id;
        @Column(name = "gyarto")
        public String Gyarto;
        @Column(name = "tipus")
        public String Tipus;


        public Processzor( String gyarto, String tipus) {
            Gyarto = gyarto;
            Tipus = tipus;
        }

        public Processzor() {

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

        public void setId(int id) {
            Id = id;
        }
        public void setGyarto(String gyarto) {
            Gyarto = gyarto;
        }
        public void setTipus(String tipus) {
            Tipus = tipus;
        }

}
