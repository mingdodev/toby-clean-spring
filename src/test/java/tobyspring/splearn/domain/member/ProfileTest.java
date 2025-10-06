package tobyspring.splearn.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ProfileTest {
    @Test
    void profile() {
        new Profile("hello");
        new Profile("123hello");
        new Profile("323434");
        new Profile("");
    }

    @Test
    void profileFail() {
        Assertions.assertThatThrownBy(() -> new Profile("1234512345123456"))
                .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> new Profile("123hello@#"))
                .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> new Profile("프로필주소"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void url() {
        var profile = new Profile("hello");

        assertThat(profile.url()).isEqualTo("@hello");
    }
}