package exceptions;

public class DBexceptions extends Exception { // Fejl fra databaselaget

    public DBexceptions(String message) { // Kun besked
        super(message); // Sender videre
    }

    public DBexceptions(String message, Throwable cause) { // Besked plus årsag
        super(message, cause); // Sender videre
    }
}