package services; // Forretningslogik

import entities.User; // Bruger
import exceptions.DBexceptions;
import exceptions.DBexceptions; // Egen fejl
import persistence.ConnectionPool; // Pool
import persistence.UserMapper; // SQL for brugere
import org.mindrot.jbcrypt.BCrypt; // Hashing

public class UserService { // Regler for login og oprettelse

    public static User login(String email, String password, ConnectionPool pool) throws DBexceptions { // log ind (US-4)
        String hash = UserMapper.getPasswordHash(email, pool); // hent gemt hash
        if (hash == null || !BCrypt.checkpw(password, hash)) { // findes ikke eller forkert kode
            throw new IllegalArgumentException("Forkert email eller kodeord"); // Samme besked begge steder
        }
        return UserMapper.getByEmail(email, pool); // Giv brugeren
    }

    public static User register(String email, String password, String repeat, ConnectionPool pool) throws DBexceptions { //  konto (US-2)
        if (email == null || !email.contains("@")) { //  email check
            throw new IllegalArgumentException("Skriv en gyldig email");
        }
        if (password == null || password.length() < 4) { // For kort kode
            throw new IllegalArgumentException("Kodeordet skal være mindst 4 tegn");
        }
        if (!password.equals(repeat)) { // Ikke ens
            throw new IllegalArgumentException("Kodeordene er ikke ens");
        }
        String hash = BCrypt.hashpw(password, BCrypt.gensalt()); //  hash
        UserMapper.createUser(email.trim().toLowerCase(), hash, pool); // Gem i databasen
        return UserMapper.getByEmail(email.trim().toLowerCase(), pool); // Hent den nye bruger
    }

    public static User refresh(User user, ConnectionPool pool) throws DBexceptions { // hent ny saldo
        return UserMapper.getById(user.getUserId(), pool); // fra databasen
    }
}
