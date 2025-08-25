package tobyspring.splearn.domain;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberTest {
    Member member;
    PasswordEncoder passwordEncoder;

    // 요것은 테스트 각각의 상황을 구성하기에 어려움을 주어요
    // 아래의 null 테스트와 같은
    @BeforeEach
    void setUp() {
        this.passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
        member = Member.create("user@example.com", "user", "secret", passwordEncoder);
    }

    @Test
    void 회원을_생성하면_회원_상태는_가입_대기() {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void 생성자에서_Null이_들어오면_예외가_발생() {
        assertThatThrownBy(() -> Member.create(null, "user", "secret", null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 회원_활성화() {
        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVATE);
    }

    @Test
    void 회원_활성화_실패() {
        member.activate();

        assertThatThrownBy(member::activate)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 회원_탈퇴() {
        member.activate();
        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    void 회원_탈퇴_실패() {
        member.activate();
        member.deactivate();

        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);
    }
    
    @Test
    void 비밀번호_검증() {
        assertThat(member.verifyPassword("secret", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("hello", passwordEncoder)).isFalse();
    }

    @Test
    void 닉네임_변경() {
        assertThat(member.getNickname()).isEqualTo("user");

        member.changeNickname("changedUser");

        assertThat(member.getNickname()).isEqualTo("changedUser");
    }

    @Test
    void 비밀번호_변경() {
        member.changePassword("verysecret", passwordEncoder);

        assertThat(member.verifyPassword("verysecret", passwordEncoder)).isTrue();
    }
}