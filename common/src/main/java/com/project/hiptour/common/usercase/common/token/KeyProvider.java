package com.project.hiptour.common.usercase.common.token;

import java.security.Key;

/**
 * @apiNote Token을 생성하는데 필요한 키를 제공하는 객체의 인터페이스로, 상황에 따라 다양한 KeyProvider 구현체를 사용할 수 있도록 하기 위한 인터페이스
 * **/
public interface KeyProvider {
    /**
     * @return JWT 토큰을 생성하는데 필요한 key를 반환한다.
     * **/
    byte[] getKey();
}
