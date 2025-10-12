package tobyspring.splearn.adapter.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;
import tobyspring.splearn.domain.shared.Email;

class DummyEmailSenderTest {
    @Test
    @StdIo
    void dummyEmailSender(StdOut out) {
        DummyEmailSender dummyEmailSender = new DummyEmailSender();

        dummyEmailSender.send(new Email("user@splearn.app"), "subject", "body");

        assertThat(out.capturedLines()[0]).isEqualTo("DummyEmailSender send email: Email[address=user@splearn.app]");
    }

}