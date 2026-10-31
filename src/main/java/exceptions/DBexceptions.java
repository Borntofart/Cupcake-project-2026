package exceptions;

public class DBexceptions extends Exception { // Fejl fra databaselaget

    public DBexceptions(String message) { // Kun besked
        super(message);
    }

    public DBexceptions(String message, Throwable cause) { // Besked plus årsag
        super(message, cause);
    }
}