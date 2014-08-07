
package ch.hsr.i.yaetm.logic;

public class History {
    
    private String user, datum, type, query, undoquery;
    private int id;
    
    public History(int id, String date, String user, String type, String query, String undoquery) {
        this.user = user;
        this.type = type;
        this.datum = date;
        this.query = query;
        this.undoquery = undoquery;
        this.id = id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public void setdatum(String datum) {
        this.datum = datum;
    }
    
    public String getDatum() {
        return datum;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    
    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
    public void setUndoquery(String undoquery) {
        this.undoquery = undoquery;
    }
    
    public String getUndoquery() {
        return undoquery;
    }

    public String getUser() {
        return user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }

    
    
    
    public String toString( ){
    	return id + " " + type + ", " + datum + " " + user + " " + query;
    }
    
    public String getHistory() {
    	return id + " " + type + ", " + datum + " " + user + " " + query;
    }
    
   
}
