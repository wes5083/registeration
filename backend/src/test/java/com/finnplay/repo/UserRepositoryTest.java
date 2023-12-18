package com.finnplay.repo;

import com.finnplay.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setEmail("wes.zhang@163.com");
        user.setFirstName("wes");
        user.setLastName("zhang");
        user.setPassword("123456");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("wes.zhang@163.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getFirstName()).isEqualTo("wes");
        assertThat(foundUser.get().getLastName()).isEqualTo("zhang");
    }

}
