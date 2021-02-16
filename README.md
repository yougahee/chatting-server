# :email: Chatting Server

</br>

## :pushpin: 사용 프레임워크 및 기술
- Spring boot 
- WebSocket
- MongoDB

</br>
</br>

## :pushpin: 기능
1. Presenter와 WebSocket으로 연결
2. WebSocket Session 관리
3. 룸서버에 해당 Presenter가 방송 중인지 확인
4. HTTP 채팅 Message Send ( API Gateway를 통해서 넘어온다. )
3. 채팅로그 남기기

</br>
</br>

## :pushpin: 간략한 Architecture
<img src = https://github.com/yougahee/chatting-server/blob/master/image/chatting_architecture.png>


## :pushpin: 전체적인 Architecture
<img src = https://github.com/yougahee/chatting-server/blob/master/image/architecture.png>


</br>
</br>
</br>
</br>

## :pushpin: 채팅의 전체적인 Flow

### Presenter   

1. Presenter가 방송을 킨 후 시그널링이 완료되면, Presenter는 채팅 서버에 WebSocket을 연결한다.   
2. 연결이 정상적으로 이루어졌다면, User는 WebSocket을 통해 token을 전달한다.     

	- 이유 
		- 실제 서비스 유저인지 확인하기 위해(보안)
		- token을 통해 PresenterIdx를 받아오기 위함   
	- 확장성을 고려하여 roomIdx대신 PresenterIdx로 처리한다.    
		- WebSocket의 token 정보를 header에 넣어서 사용할 것 이다.    
		- Zuul2를 사용하여 API Gateway에서 WebSocket의 header에 token을 관리할 수 있게 해준다.     
		
3. token이 예외를 던지지 않고(만료, 변조 등) 정상적으로 작동한다면 token안의 정보를 decode해서 userIdx(=presenterIdx)를 꺼내올 수 있다.

4. presenterIdx와 WebSocketSession을 로컬에서 저장해준다. 
- Viewer들이 presenterIdx만 알면 WebSocketSession을 매칭할 수 있고, WebSocket을 통해 데이터(채팅내용-json)을 Presenter에게 보낼 수 있다.

<br>
<br>

### Viewer/Presenter의 채팅 보내기

1. 모든 유저는 HTTP 통신을 통해 채팅을 보낼 수 있다. 
2. HTTP Request Body에 TextMessage와 해당 룸의 PresenterIdx를 보낸다. 
3. HTTP Request를 받은 채팅 서버는 (전달 받은) PresenterIdx가 스트리밍 중인지 확인한다.
	- 실제로 스트리밍 하고 있는 상태라면 4번으로 넘어간다.
	- 스트리밍을 하고 있지 않다면 "방송 종료되었습니다" 메세지와 함께 Exception처리
4. 채팅 서버는 PresenterIdx에 해당하는 Presenter와 WebSocket이 연결되어 있는지 확인한다. 
	- WebSocket연결이 유지된 상태라면 5번으로 넘어간다. 
	- WebSocket연결이 되어 있지 않다면 재 연결 요청을 해야 한다. 
5. 해당 WebSocketSession을 통해 채팅 서버는 HTTP로 받은 메세지를 Presenter에게 전달해준다.
6. 메세지를 받은 Presenter는 DataChannel을 통해 메세지를 보내면 끝!


<br>
<br>


### 재 연결 로직

1. Presenter가 방송을 시작하면 시그널링 서버와 채팅서버에 각각 한 개씩 WebSocket을 연결시킨다.
	- Viewer인 경우는 시그널링 서버에만 WebSocket을 연결하면 된다.
	- Presenter와 채팅 서버 사이의 WebSocket은 채팅서버에 들어온 Message들을 Presenter에게 넘겨주는 역할을 하는 Socket
	</br>  

2. Presenter/Viewer 사용자 모두는 채팅을 token, roomIdx, textMessage 데이터와 함께 HTTP 통신을 한다. 
	
3. 채팅서버로 들어온 Message를 확인하고 room에 해당하는 Presenter에게 TextMessage를 넘겨준다.
4. Presenter는 채팅과 연결된 WebSocket에 데이터가 들어오면 Data-Channel을 통해 Text Message를 보낸다. 
	- Presenter를 구독하고 있는 모든 Viewer들은 Data Channel을 통해 채팅을 받을 수 있다.

<img src = https://github.com/yougahee/chatting-server/blob/master/image/chatting_flow.jpg>

</br>
</br>
</br>
</br>

## :pushpin: 고민/생각한 부분

### 1. WebSocketSession을 저장해야한다.

1) WebSocketSession을 Local 저장소에 저장하고 있다. 

</br>
</br>

### 2. 재연결 관리
1) 채팅서버가 다운된 경우
	- 채팅서버가 다운이 되었다는 말은 결국 모든 Presenter에 연결되어있는 WebSocket 연결도 모두 끊긴 상태일 것이다.(HTTP통신이 이루어지더라도 ) 그러므로 굳이 Global Cache에 WebSocketSession을 저장해서 관리해야 할 필요성이 있을까?

2) Presenter의 네트워크 상태가 좋지 않은 경우 ( WebSocket 끊김 )
	- Presenter도 WebSocket의 onClose를 통해 연결이 끊김을 파악할 수 있다. 그것을 통해 미디어가(스트리밍 하고 있는 방이) 계속 살아있다면 채팅서버와 재연결 시도를 할 수 있다. 
		
3) 사실 Presenter가 미디어를 계속해서 전송하고 있다고 했을 때, 채팅서버와 WebSocket연결이 끊어지는 것을 감지할 수 있다. 
	- Presenter가 WebSocket과 연결이 계속 되어있는지 확인을 하고 재연결하기

</br>
</br>

### 3. 채팅 로그
1. 채팅 로그에 어떤 정보를 저장할 것인가?
	- user_idx, 닉네임, 채팅 메세지, 시간 
2. 몽고 DB사용
3. 필요한 이유
	- 욕설 필터링
	- 데이터 분석
	- 강퇴/메세지 삭제
	- 동영상 저장 시 채팅도 함께 보여주기 위해 

<br>

### 4. 채팅로그를 저장할 때 몽고 DB를 사용하는 이유?
1. NoSQL 특성을 가지고 있는 DB 중 몽고DB와 Redis를 고려해보았다. 
2. Redis를 사용하지 않은 이유는 Redis는 인메모리 캐시이다. 그렇기 때문에 휘발성 메모리이다. 
	- 채팅의 정보를 저장하는 것은 스트리밍 미디어가 동영상으로 저장되었을 때, 함께 보여주기 위함이므로 영구적으로 저장해야하기 때문에 Redis는 성격에 맞지 않는다.  


</br>
</br>

### 5. 몽고 DB의 특징
1. JSON형태의 document형식이다. 
2. NoSQL
3. Read/Write 시간이 빠르다. 
</br>
</br>
</br>

# :pushpin: dependencies
```
dependencies {
    //lombok
	implementation 'org.springframework.boot:spring-boot-starter-web'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'

	//lombok
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'

	//db-mongodb
	implementation 'org.springframework.boot:spring-boot-starter-data-mongodb'

	//websocket
	implementation 'org.springframework.boot:spring-boot-starter-websocket'
	implementation 'org.webjars:webjars-locator-core'
	implementation 'org.webjars:sockjs-client:1.0.2'
	implementation 'org.webjars:stomp-websocket:2.3.3'
	implementation 'org.webjars:bootstrap:3.3.7'
	implementation 'org.webjars:jquery:3.1.1-1'

	//template-engine
	implementation 'org.springframework.boot:spring-boot-starter-mustache'

	//gson
	compile group: 'com.google.code.gson', name: 'gson', version: '2.7'

	//tomcat
	implementation 'com.bmuschko:gradle-tomcat-plugin:2.5'

	implementation 'org.apache.tomcat.embed:tomcat-embed-jasper'
	implementation 'javax.servlet:jstl:1.2'

	//jwt
	implementation group: 'io.jsonwebtoken', name: 'jjwt', version: '0.7.0'
	implementation 'com.auth0:java-jwt:3.4.0'

	//http-client
	compile 'org.apache.httpcomponents:httpclient:4.5.13'

	implementation 'org.springframework.boot:spring-boot-starter-test'
}
```
