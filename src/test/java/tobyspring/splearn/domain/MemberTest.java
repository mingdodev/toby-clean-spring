package tobyspring.splearn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MemberTest {
    @Test
    void 회원을_생성하면_회원_상태는_가입_대기() {
        var member = new Member("user@example.com", "user", "secret");

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }
}