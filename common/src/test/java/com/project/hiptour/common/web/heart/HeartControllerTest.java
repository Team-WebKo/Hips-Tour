package com.project.hiptour.common.web.heart;

import com.project.hiptour.common.security.OauthProviderService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional  // 각 테스트 후 롤백
@ActiveProfiles("local")
class HeartControllerTest {

}