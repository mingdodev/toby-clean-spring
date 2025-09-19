package tobyspring.splearn.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.Objects;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

@MappedSuperclass
@ToString
public abstract class AbstractEntity {
    @Id
    @Getter(onMethod_ = {@Nullable}) // Null을 반환할 수도 있음을 명시
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        // 동등성 비교 시 hibernateProxy(Lazy Loading을 위해 만든 대리 객체) 상태라면 정확한 비교 불가
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() // 실제 엔티티 클래스 획득
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) {
            return false;
        }
        AbstractEntity that = (AbstractEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
                .getPersistentClass()
                .hashCode() : getClass().hashCode();
    }
}
