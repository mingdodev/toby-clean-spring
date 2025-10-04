package tobyspring.splearn.domain.member;

// 도메인 서비스: 도메인에 관련되기 보다는 기술적인 부분
// 또한, 얼마든지 구현이 바뀔 수 있는 부분 -> 테스트 용이
public interface PasswordEncoder {
    String encode(String password);
    boolean matches(String password, String passwordHash);
}
