package tobyspring.splearn.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class MemberTest {
    @Test
    void 회원을_생성하면_회원_상태는_가입_대기() {
        var member = new Member("user@example.com", "user", "secret");

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void 생성자에서_Null이_들어오면_예외가_발생() {
        assertThatThrownBy(() -> new Member(null, "user", "secret"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 회원_활성화() {
        var member = new Member("user@example.com", "user", "secret");

        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVATE);
    }

    @Test
    void 회원_활성화_실패() {
        var member = new Member("user@example.com", "user", "secret");

        member.activate();

        assertThatThrownBy(member::activate)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 회원_탈퇴() {
        var member = new Member("user@example.com", "user", "secret");

        member.activate();
        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    void 회원_탈퇴_실패() {
        var member = new Member("user@example.com", "user", "secret");

        member.activate();
        member.deactivate();

        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);
    }
}