package tobyspring.splearn.adapter.webapi;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tobyspring.splearn.adapter.webapi.dto.MemberRegisterResponse;
import tobyspring.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberRegisterRequest;

@RestController
@RequiredArgsConstructor
public class MemberApi {
    private final MemberRegister memberRegister;

    @PostMapping("/api/members")
    public MemberRegisterResponse register(@RequestBody @Valid MemberRegisterRequest request) {
        // 보통 인라인으로 하긴 하는데 어댑터에서 다른 작업을 하거나 로그를 남길 수도 있으니 개방
        // 움 그리고 리턴타입이 엔티티/애그리거트인 걸 일단 눈으로 확인해보자
        Member member = memberRegister.register(request);

        return MemberRegisterResponse.of(member);
    }
}
