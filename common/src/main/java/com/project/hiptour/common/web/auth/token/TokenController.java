package com.project.hiptour.common.web.auth.token;

import com.project.hiptour.common.usercase.token.TokenRequestResult;
import com.project.hiptour.common.usercase.token.TokenUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@Slf4j
@AllArgsConstructor
public class TokenController {

    private final TokenUseCase tokenUseCase;
    /**
     * @apiNote refresh token 유효성 검사를 수행하고, 성공하면 새로운 access token을 발급한다
     * **/

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TokenResponse> verifyAndReturnAccessToken(@RequestBody TokenRequest request){

        log.debug("refresh token request");

        String requestRefreshToken = request.getRefreshToken();
        TokenRequestResult result = tokenUseCase.validateRefreshToken(requestRefreshToken);

        if(result.isSuccess()){
            return ResponseEntity.ok(new TokenResponse(true,"success", result.getAccessToken()));
        }else{
            return ResponseEntity.badRequest().body(new TokenResponse(false, result.getMessage(), ""));
        }
    }

}
