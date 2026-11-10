package services;

import entities.User;
import exceptions.DBexceptions;
import persistence.UserMapper;
import org.mindrot.jbcrypt.BCrypt;

public class UserService { // regler for login og oprettelse

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User login(String email, String password) throws DBexceptions { // log ind (US-4)
        if (email == null || password == null) {
            throw new IllegalArgumentException("Forkert email eller kodeord");
        }
        email = email.trim().toLowerCase();
        String hash = userMapper.getPasswordHash(email);
        if (hash == null || !BCrypt.checkpw(password, hash)) {
            throw new IllegalArgumentException("Forkert email eller kodeord");
        }
        return userMapper.getByEmail(email);
    }

    public User register(String email, String password, String repeat) throws DBexceptions { //  konto (US-2)
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
        userMapper.createUser(email.trim().toLowerCase(), hash);
        return userMapper.getByEmail(email.trim().toLowerCase());
    }

    public User refresh(User user) throws DBexceptions { // hent ny saldo
        return userMapper.getById(user.getUserId());
    }
}
