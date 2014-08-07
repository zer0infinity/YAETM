
package ch.hsr.i.yaetm.logic;

public class Reservation {
    
    private String name, datum, reservator;
    private int anzahl, id, fk_ersatzteil;
    
    public Reservation(String name, int anzahl, String datum, String reservator, int id, int fk_ersatzteil) {
        this.name = name;
        this.fk_ersatzteil = fk_ersatzteil;
        this.datum = datum;
        this.anzahl = anzahl;
        this.reservator = reservator;
        this.id = id;
    }
    
    public void setReservator(String reservator) {
        this.reservator = reservator;
    }
    
    public String getReservator() {
        return reservator;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setfk_ersatzteil(Integer fk_ersatzteil) {
        this.fk_ersatzteil = fk_ersatzteil;
    }
    
    public String getName() {
        return name;
    }
    
    public int getfk_ersatzteil() {
        return fk_ersatzteil;
    }
    
    public void setDatum(String datum) {
        this.datum = datum;
    }
    
    public String getDatum() {
        return datum;
    }
    
    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }
    
    public int getAnzahl() {
        return anzahl;
    }
    
    public void setID(int id) {
        this.id = id;
    }
    
    public int getID() {
        return id;
    }
   
    @Override
    public String toString( ){
    	return id + " " + name + ", " + datum + " " + anzahl + " " + reservator;
    }
    
    public String getReservation() {
        return name + ", " + datum + " " + anzahl + " " + reservator;
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other instanceof Reservation) {
            Reservation ersatzteil = (Reservation) other;
            return ((name == null ? ersatzteil.name == null : name.equalsIgnoreCase(ersatzteil.name)) &&
                    (datum == null ? ersatzteil.datum == null : datum.equalsIgnoreCase(ersatzteil.datum)) && 
                    (anzahl == 0 ? ersatzteil.anzahl == 0 : anzahl == ersatzteil.anzahl)) &&
                    (reservator == null ? ersatzteil.reservator == null : reservator.equalsIgnoreCase(ersatzteil.reservator)) &&
                    (id == 0 ? ersatzteil.id == 0 : id == ersatzteil.id) &&
                    (fk_ersatzteil == 0 ? ersatzteil.fk_ersatzteil == 0 : fk_ersatzteil == ersatzteil.fk_ersatzteil);
        }
        return false;
    }
}
