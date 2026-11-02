package exceptions;

public class DBexceptions extends Exception { // fejl fra databaselaget

    public DBexceptions(String message) {
        super(message);
    }

    public DBexceptions(String message, Throwable cause) { // Besked plus årsag
        super(message, cause);
    }
}