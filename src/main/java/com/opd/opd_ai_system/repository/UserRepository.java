package com.opd.opd_ai_system.repository;

import com.opd.opd_ai_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer>{

    Optional<User> findByEmailAndRole(
            String email,
            String role
    );
    Optional<User> findByEmail(String email);

}
