# 💬 ***CHAT PROJECT***
### 🔊 프로젝트 소개
STOMP 기반의 일대일 채팅을 구현한 프로젝트 입니다.
##
### 🎨 Dependencies
- *Spring Web*
- *Spring WebSocket*
- *Mustache*
- *Lombok*

##
### 💻 개발환경
- **Version** : Java 17
- **IDE** : IntelliJ
- **Framework** : SpringBoot 3.4.3

##
### ❓STOMP란 ?
> STOMP(Simple or Streaming Text Oriented Messaging Protocol)  
>   
> 텍스트 기반 메시징 프로토콜로, 웹소켓 같은 전송 계층 위에서 동작하면서 메시지 송수신을 표준화해주는 역할을 한다.
> 

#### 1️⃣ 웹소켓만 사용한다면 ?
클라이언트와 서버가 그저 문자열만 주고받는 형태, 이 메세지가 채팅인지, 알림인지, 어디로 전달해야하는지,
같은 것을 직접 규칙을 정해 해석해야한다.
  
#### 2️⃣ STOMP를 사용한다면 ?
메세지에 헤더와 명령 구조가 생겨서, `SEND`, `SUBSCRIBE`, `UNSUBSCRIBE`, `CONNECT`, `DISCONNECT` 
같은 공통된 명령을 정의해준다. 즉 HTTP가 웹에서 요청/응답을 규칙으로 만든 것처럼
STOMP는 메시징에서의 규칙을 만들어준다.

#### 3️⃣ STOMP 구조
```
COMMAND
header1:value1
header2:value2
...

body^@
```

- COMMAND (명령어)  
첫 줄에 들어가며, 클라이언트 ↔ 서버가 어떤 동작을 하는지 정의
`예: CONNECT, SEND, SUBSCRIBE, MESSAGE, DISCONNECT`
  

- Header (헤더)  
키-값 쌍으로 여러 줄이 입력   
`예: destination:/topic/chatroom/1`


- Body (본문)  
실제 메시지 데이터. JSON, 문자열 등 자유롭게 들어감
  

- Frame 종료 (\0 널 문자)  
마지막에 \0이 붙어서 프레임 끝을 알림

#### 4️⃣ STOMP 명령어
- SEND  
특정 destination(예: /topic/chatroom/1)으로 메시지 발송

- SUBSCRIBE  
어떤 destination을 구독해서 메시지를 수신

- MESSAGE  
서버가 클라이언트로 전달하는 메시지

- CONNECT / DISCONNECT  
연결 관리

##
### 🐞 트러블 슈팅
<details>
    <summary>1. 프론트엔드 - 백엔드 CORS 설정</summary><br>

```bash
# 에러 문구
java.lang.IllegalArgumentException: When allowCredentials is true, 
allowedOrigins cannot contain the special value "*" since that cannot be set on the "Access-Control-Allow-Origin" response header. 
To allow credentials to a set of origins, list them explicitly or consider using "allowedOriginPatterns" instead.
```
###
#### 📝 개요
`allowCredentials`를 true로 설정하면 `allowedOrigins`의 값을 `*` 설정할 수 없다는 에러 메세지 였다. 
하지만 `allowCredentials`를 설정하지 않았고 디폴트 값도 false 라는 것 때문에 한동안 구글링과 AI 도움을 받았다.
  
#### ❗원인
```java
// config/handler/WebSocketStompBrokerConfig
@Override
 public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry
            .addEndpoint("/ws-stomp")
            .setAllowedOrigins("*") // 에러 발생의 원인 '*'
            .withSockJS();
}
```
에러 발생의 원인은 `registerStompEndpoints` 메서드의 `setAllowedOrigins`의 `*`가 문제였다.
스프링부트 2.4 버전부터는 `allowCredentials`이 true 일때 `AllowedOrigins`이 `*` 면 스프링 부트에서 보안상 자동으로 에러를 띄운다고 한다.
  
 `allowCredentials`이 true 시 `AllowedOrigins`이 `*` 면 사용자 인증정보가 헤더에 포함되면서 모든 도메인에서 요청을 허용한다는 뜻이다.
이는 결국 악의적인 공격자가 자신만의 도메인과 JS 혹은 HTML을 만들어 사용자를 유도해 요청을 보낼 수 있다는 것이다. 인증정보는 사용자의 쿠키에 저장될테니 
보안상 엄청난 허점이 되기에 스프링 부트에서 처음부터 막는 것이다.


#### ✔️ 해결
```java
// 방법 1
registry
        .addEndpoint("/ws-stomp")
        .setAllowedOrigins("http://localhost:3000") // 프론트 URL
        .withSockJS();

// 방법 2
registry
        .addEndpoint("/ws-stomp")
        .setAllowedOriginPatterns("*") // 패턴 기반 설정
        .withSockJS();
```
해결방법은 방법 1 처럼 프론트 요청 URL을 직접 작성해주거나 방법 2 처럼 패턴 기반 메서드에 와일드카드를 넣어주면 HTTP 표준은 준수하기에
오류가 발생하지 않는다.
</details>

##
### 🏷 출처
- [소켓 연결 및 구성](https://adjh54.tistory.com/573#5.%20ChatWebSocketHandler%20%ED%81%B4%EB%9E%98%EC%8A%A4%20%EA%B5%AC%EC%84%B1-1-11)