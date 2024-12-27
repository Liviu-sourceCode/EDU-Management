package com.unitbv.tst.springdata.config;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

    @Value("${app_user}")
    private String username;

    @Value("${app_password}")
    private String password;

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // Validare proprietati obligatorii
        Objects.requireNonNull(username, "Environment variable app_user must be set!");
        Objects.requireNonNull(password, "Environment variable app_password must be set!");

        // Creare utilizator cu parola criptata
        UserDetails user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles("USER")
                .build();

        // Returnare utilizator in memorie
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // Dezactivare CSRF pentru API-uri RESTful
                .authorizeHttpRequests(authz -> authz.anyRequest().authenticated()) // Toate cererile necesita autentificare
                .httpBasic(withDefaults()); // Autentificare HTTP Basic
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }

    /**
     * Encoder personalizat utilizand Argon2.
     */
    public static class Argon2PasswordEncoder implements PasswordEncoder {

        private static final int SALT_LENGTH = 16;
        private static final int HASH_LENGTH = 32;
        private static final int MEMORY = 65536; // 64 MB
        private static final int ITERATIONS = 2;
        private static final int PARALLELISM = 1;

        @Override
        public String encode(CharSequence rawPassword) {
            byte[] salt = new byte[SALT_LENGTH];
            new SecureRandom().nextBytes(salt);

            byte[] hash = generateHash(rawPassword.toString(), salt);

            return Base64.getEncoder().encodeToString(salt) + "$" + Base64.getEncoder().encodeToString(hash);
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            if (!encodedPassword.contains("$")) {
                throw new IllegalArgumentException("Encoded password format is invalid. Expected format: salt$hash");
            }

            String[] parts = encodedPassword.split("\\$");
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] storedHash = Base64.getDecoder().decode(parts[1]);

            byte[] computedHash = generateHash(rawPassword.toString(), salt);

            return java.util.Arrays.equals(storedHash, computedHash);
        }

        private byte[] generateHash(String password, byte[] salt) {
            Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                    .withSalt(salt)
                    .withParallelism(PARALLELISM)
                    .withMemoryAsKB(MEMORY)
                    .withIterations(ITERATIONS);

            Argon2BytesGenerator generator = new Argon2BytesGenerator();
            generator.init(builder.build());

            byte[] hash = new byte[HASH_LENGTH];
            generator.generateBytes(password.toCharArray(), hash, 0, HASH_LENGTH);

            return hash;
        }
    }
}
