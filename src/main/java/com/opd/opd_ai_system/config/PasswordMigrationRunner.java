package com.opd.opd_ai_system.config;

import com.opd.opd_ai_system.entity.User;
import com.opd.opd_ai_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ONE-TIME MIGRATION — run once, then delete this file.
 * -----------------------------------------------------------------------
 * Some doctor/nurse accounts were created before password hashing was
 * added to AdminService.saveUser(), so their "password" column still
 * holds plain text. This scans every user on startup and re-hashes any
 * password that isn't already a valid bcrypt hash.
 *
 * Bcrypt hashes always start with $2a$, $2b$, or $2y$ and are 60
 * characters long — that's how we tell "already hashed" apart from
 * "plain text" without needing to know the original value.
 *
 * SAFE TO RUN MULTIPLE TIMES: already-hashed rows are skipped, so
 * nothing breaks if the app restarts again with this class still in
 * place. Still — once you've confirmed doctor/nurse login works, delete
 * this file so it stops running on every startup.
 * -----------------------------------------------------------------------
 */
@Component
public class PasswordMigrationRunner implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) {
        List<User> users = userRepository.findAll();
        int migrated = 0;

        for (User user : users) {
            String pwd = user.getPassword();
            boolean alreadyHashed =
                    pwd != null && pwd.length() == 60 &&
                            (pwd.startsWith("$2a$") || pwd.startsWith("$2b$") || pwd.startsWith("$2y$"));

            if (!alreadyHashed) {
                user.setPassword(encoder.encode(pwd));
                userRepository.save(user);
                migrated++;
            }
        }

        if (migrated > 0) {
            System.out.println("[PasswordMigrationRunner] Hashed " + migrated + " plain-text password(s) in the users table.");
        }
    }
}