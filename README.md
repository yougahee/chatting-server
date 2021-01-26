# :feet: Chatting Server

</br>

## 사용 프레임워크
- Spring boot 

</br>
</br>

## :pushpin: 무엇을 만들 것 인가? 
- 로그 관리 및 Presenter(=Streamer) 와 연결해주는 채팅서버 구현

</br>
</br>


## :pushpin: Flow
1. Presenter가 방송을 시작하면 시그널링 서버와 채팅서버에 각각 한 개씩 WebSocket을 연결시킨다.
	- Viewer인 경우는 시그널링 서버에만 WebSocket을 연결하면 된다.
	- Presenter와 채팅 서버 사이의 WebSocket은 채팅서버에 들어온 Message들을 Presenter에게 넘겨주는 역할을 하는 Socket
2. Presenter/Viewer 사용자 모두는 채팅을 token, roomIdx, textMessage 데이터와 함께 HTTP 통신을 한다. 
3. 채팅서버로 들어온 Message를 확인하고 room에 해당하는 Presenter에게 TextMessage를 넘겨준다.
4. Presenter는 채팅과 연결된 WebSocket에 데이터가 들어오면 Data-Channel을 통해 Text Message를 보낸다. 
	- Presenter를 구독하고 있는 모든 Viewer들은 Data Channel을 통해 채팅을 받을 수 있다.

<img src = >

</br>

## :pushpin: 



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

