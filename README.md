# :feet: Chatting Server

</br>

## :pushpin: 사용 프레임워크 및 기술
- Spring boot 
- WebSocket

</br>
</br>

## :pushpin: 무엇을 만들 것 인가? 
- 로그 관리 및 Presenter(=Streamer) 와 연결해주는 채팅서버 구현

</br>
</br>

## :pushpin: 기능
1. Presenter와 WebSocket으로 연결
2. HTTP 채팅 Message Send ( API Gateway를 통해서 넘어온다. )
3. 채팅로그 남기기

</br>
</br>

## :pushpin: 간략한 Architecture
<img src = https://github.com/yougahee/chatting-server/blob/master/image/chatting_architecture.png>

</br>
</br>
</br>
</br>

## :pushpin: Flow
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

1) 현재 코드 상에는 WebSocketSession을 저장하고 있는 상태이다.   
2) Global cache가 필요한 이유?
	- 만약, 채팅 Server가 다운될 경우 WebSocketSession의 정보는 사라지게 된다.
	- 그렇기 때문에, Redis에 WebSocketSession을 저장하던지, 다른 방법을 찾아보던지 해야한다.   
3) Client 단에서 WebSocket이 onClose 메소드를 통해 소켓 연결이 끊김을 알아차릴 수 있다. Presenter가 WebSocket에 재연결( 그 사이에 보낸 채팅은 HTTP 통신이 이뤄지지 않으니 Client측에서 채팅 전송에 실패했다는 메세지를 User에게 알려줘야 한다.)

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
</br>
</br>

# :pushpin: dependencies
```
dependencies {
    //lombok
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'

	//db
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	runtimeOnly 'mysql:mysql-connector-java'

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
}
```

