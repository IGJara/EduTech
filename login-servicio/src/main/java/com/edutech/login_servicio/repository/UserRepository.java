package com.edutech.login_servicio.repository; // PAQUETE CORRECTO: login_servicio

import com.edutech.login_servicio.model.User; // IMPORT CORRECTO: login_servicio.model.User
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { // REPOSITORIO: UserRepository (no UsuarioLoginRepository)
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
