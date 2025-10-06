package tobyspring.splearn.application.member.provided;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.member.DuplicateEmailException;
import tobyspring.splearn.domain.member.DuplicateProfileException;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberFixture;
import tobyspring.splearn.domain.member.MemberInfoUpdateRequest;
import tobyspring.splearn.domain.member.MemberRegisterRequest;
import tobyspring.splearn.domain.member.MemberStatus;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {

    private Member registerMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();
        return member;
    }

    private Member registerMember(String email) {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest(email));
        entityManager.flush();
        entityManager.clear();
        return member;
    }

    @Test
    void register() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        System.out.println(member);

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmailFail() {
        // 이건 도메인 모델에서 체크할 수 없음. 외부 의존성(DB)가 필요하기 때문.
        memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    void activate() {
        Member member = registerMember();

        member = memberRegister.activate(member.getId());

        entityManager.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVATE);
    }

    @Test
    void deactivate() {
        Member member = registerMember();

        member = memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.deactivate(member.getId());

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
    }

    @Test
    void updateInfo() {
        Member member = registerMember();

        member = memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "hello23", "자기소개"));

        assertThat(member.getDetail().getProfile().address()).isEqualTo("hello23");
    }

    @Test
    void updateDuplicateProfileFail() {
        Member member = registerMember();
        memberRegister.activate(member.getId());
        member = memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "hello23", "자기소개"));

        Member member2 = registerMember("newemail@splearn.com");
        memberRegister.activate(member2.getId());
        entityManager.flush();
        entityManager.clear();

        // member2는 다른 멤버와 같은 프로필 주소를 사용할 수 없다
        assertThatThrownBy(() -> {
            memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("James", "hello23", "자기소개2"));
        }).isInstanceOf(DuplicateProfileException.class);

        // 다른 프로필 주소로는 변경 가능
        memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("James", "james123", "자기소개2"));

        // 기존 프로필 주소를 입력하는 것도 가능
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "hello23", "자기소개"));

        // 프로필 주소를 공백으로 입력하는 것도 가능
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "", "자기소개"));
    }

    @Test
    void memberRegisterRequestFail() {
        checkRequestValidation(new MemberRegisterRequest("tody@splearn.app", "Toby", "secret"));
        checkRequestValidation(new MemberRegisterRequest("tody@splearn.app", "HelloHelloHelloHelloHelloHello", "longsecretsecret"));
        checkRequestValidation(new MemberRegisterRequest("tody", "Toby", "secret"));
    }

    private void checkRequestValidation(MemberRegisterRequest invalidRequest) {
        assertThatThrownBy(() -> memberRegister.register(invalidRequest))
            .isInstanceOf(ConstraintViolationException.class);
    }
}
