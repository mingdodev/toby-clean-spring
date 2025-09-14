package tobyspring.splearn;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

class SplearnApplicationTest {
    @Test
    void run() {
        try(MockedStatic<SpringApplication> mock = Mockito.mockStatic(SpringApplication .class)) {
            SplearnApplication.main(new String[0]);

            mock.verify(() -> SpringApplication.run(SplearnApplication.class, new String[0]));
        }
    }


}