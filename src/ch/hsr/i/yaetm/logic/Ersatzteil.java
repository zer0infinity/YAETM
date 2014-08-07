
package ch.hsr.i.yaetm.logic;



public class Ersatzteil {
    
    private String name, preis;
    private int anzahl, id;
    
    public Ersatzteil(String name, String preis, int anzahl, int id) {
        this.name = name;
        this.preis = preis;
        this.anzahl = anzahl;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setPreis(String preis) {
        this.preis = preis;
    }
    
    public String getPreis() {
        return preis;
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
    public String toString( ) {
    	return id + " " + name + " " + preis + " " + anzahl;
    }
    
    public String getProdukt() {
        return name + " " + preis + " " + anzahl;
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other instanceof Ersatzteil) {
            Ersatzteil ersatzteil = (Ersatzteil) other;
            return ((name == null ? ersatzteil.name == null : name.equalsIgnoreCase(ersatzteil.name)) &&
                    (preis == null ? ersatzteil.preis == null : preis.equalsIgnoreCase(ersatzteil.preis)) && 
                    (anzahl == 0 ? ersatzteil.anzahl == 0 : anzahl == ersatzteil.anzahl)) &&
                    (id == 0 ? ersatzteil.id == 0 : id == ersatzteil.id);
        }
        return false;
    }
}
