package services;

import entities.User;
import exceptions.DBexceptions;
import exceptions.DBexceptions;
import persistence.ConnectionPool;
import persistence.UserMapper;
import org.mindrot.jbcrypt.BCrypt;

public class UserService { // Regler for login og oprettelse

    public static User login(String email, String password, ConnectionPool pool) throws DBexceptions { // log ind (US-4)
        if (email == null || password == null) {
            throw new IllegalArgumentException("Forkert email eller kodeord");
        }
        email = email.trim().toLowerCase();
        String hash = UserMapper.getPasswordHash(email, pool);
        if (hash == null || !BCrypt.checkpw(password, hash)) {
            throw new IllegalArgumentException("Forkert email eller kodeord");
        }
        return UserMapper.getByEmail(email, pool);
    }

    public static User register(String email, String password, String repeat, ConnectionPool pool) throws DBexceptions { //  konto (US-2)
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Skriv en gyldig email");
        }
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Kodeordet skal være mindst 4 tegn");
        }
        if (!password.equals(repeat)) {
            throw new IllegalArgumentException("Kodeordene er ikke ens");
        }
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        UserMapper.createUser(email.trim().toLowerCase(), hash, pool);
        return UserMapper.getByEmail(email.trim().toLowerCase(), pool);
    }

    public static User refresh(User user, ConnectionPool pool) throws DBexceptions { // hent ny saldo
        return UserMapper.getById(user.getUserId(), pool);
    }
}
