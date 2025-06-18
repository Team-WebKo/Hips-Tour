# Session Tracker

## 목적

1. 사용자의 서비스 이용 패턴을 추적하고, 이를 관리한다.
2. 이를 통해, 사용자의 서비스 이용 패턴을 추적한다.
3. 이를 통해, 서비스의 KPI를 측정하고, 이를 근거로 의사결정을 수행한다.


## 구현 방식

1. 기본적으로, Spring Interceptor를 이용하며, 이를 통해, 사용자의 접근 URL 및 세션 지속 시간 등을 추적한다.
```
  USER --> WAS --> Interceptor --> Handler  --> Interceptor  --> was --> User
```