# spring-security-authentication


아이디와 비밀번호를 기반으로 로그인 기능을 구현하고 <br> 
Basic 인증을 사용하여 사용자를 식별할 수 있도록 스프링 프레임워크를 사용하여 웹 앱으로 구현한다.

- ``Spring security``의 내부 구조를 분석해 직접 구현 (단, ``Filter`` 대신 ``Interceptor`` 활용)

---
# 구현 요구 사항

1. 아이디와 비밀번호 기반 로그인 인증 구현
    + 로그인 요청 시 사용자가 입력한 아이디와 패스워드를 확인하여 인증한다.
      + 로그인 성공 시 ``Session`` 을 사용하여 인증 정보를 저장한다.
    

2. Basic 인증 구현
    + 사용자 목록 조회 기능 (인증 진행 후 인가 진행) 
      - 인증 : Basic 인증을 사용하여 사용자를 식별한다.
        + 이를 위해 요청의 **Authorization 헤더**에서 Basic 인증 정보를 추출 후 decode 하여 인증을 처리한다. 
        + 인증 성공 시 ``Session``을 사용하여 인증 정보를 저장한다. 
          + (다만, 인가를 위한 인증 및 권한 정보는 ThreadLocal 에 저장하여 활용) 
      - 인가 : ``Member``로 등록되어 있는, 인증된 사용자만 가능하도록 한다.
        + 인증 ``Interceptor``가 통과되면 인가 ``Interceptor`` 진행 
        + ThreadLocal 에서 조회하여 인증 정보가 있으면 인가 


3. 인터셉터 분리
    + ``HandlerInterceptor``를 사용하여 인증 관련 로직을 ``Controller``에서 분리한다.
      + 앞서 구현한 두 인증 방식(아이디 비밀번호 로그인 방식과 Basic 인증 방식) 모두 인터셉터에서 처리되도록 구현한다.
      + 가급적이면 하나의 인터셉터는 하나의 작업만 수행하도록 설계한다.
        + 아이디/패스워드 기반 Authentication ``Interceptor``
        + Basic 인증 기반 Authentication  ``Interceptor``
        + Authorization ``Interceptor``
      


4. 인증 로직과 서비스 로직 간의 패키지 분리
    + **서비스 코드와 인증 코드를 명확히 분리**하여 관리하도록 한다.
      + 서비스 관련 코드는 ``app`` 패키지에 위치시키고, 인증 관련 코드는 ``security`` 패키지에 위치시킨다.
    + 리팩터링 과정에서 패키지 간의 양방향 참조가 발생한다면 단방향 참조로 리팩터링한다.
      + ``app`` 패키지는 ``security`` 패키지에 의존할 수 있지만, **``security`` 패키지는 ``app`` 패키지에 의존하지 않도록** 한다.
    + 인증 관련 작업은 ``security`` 패키지에서 전담하도록 설계하여, 서비스 로직이 인증 세부 사항에 의존하지 않게 만든다.
      
    ```
   패키지 간 의존성을 최소화하고, 변경에 강한 구조를 만드는 목적.
   security 패키지를 독립적이고 재사용 가능하게 설계하려면, 직접적인 의존성을 피하기 위해 인터페이스를 구현하게 한다. (DIP)
   ```
   
   
    

<br>

---    
# API 정의
### 로그인
     - /login  [POST]  아이디와 비밀번호를 확인하여 인증. (인증 후 Session에 인증 정보 저장) 

### 사용자 조회
     - /member [GET]   사용자 목록 조회. (단, 인증 성공 후 인증 정보가 있을 경우만 인가) 

