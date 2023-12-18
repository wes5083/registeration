package com.finnplay.repo;

import com.finnplay.entity.Token;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findByToken_WhenTokenExists_ReturnsToken() {
        Token token = new Token();
        token.setToken("test-token");
        token.setEmail("test@example.com");
        token.setLastSeeAt(LocalDateTime.now());
        entityManager.persistAndFlush(token);

        Optional<Token> foundToken = tokenRepository.findByToken("test-token");

        assertThat(foundToken).isPresent();
        assertThat(foundToken.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void findByToken_WhenTokenDoesNotExist_ReturnsEmptyOptional() {
        Optional<Token> foundToken = tokenRepository.findByToken("non-existent-token");
        assertThat(foundToken).isEmpty();
    }

}
