package tobyspring.splearn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EmailTest {
    @Test
    void 동등성_비교() {
        var email1 = new Email("user@example.com");
        var email2 = new Email("user@example.com");

        assertThat(email1).isEqualTo(email2);
    }

}