package example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class DemoTest {
    @MockBean
    Runnable dummy;

    @Test
    void ok() {}
}
