# :feet: Chatting Server

</br>

## 사용 프레임워크
- Spring boot 

## :pushpin: 무엇을 만들 것 인가? 
- 로그 관리 및 Presenter(=Streamer) 와 연결해주는 채팅서버 구현

</br>
</br>
0
<img src =>



</br>
</br>
</br>

## :pushpin: Flow
1. Presenter가 방송을 시작하면 시그널링 서버와 채팅서버에 각각 한 개씩 WebSocket을 연결시킨다.
	- Viewer인 경우는 시그널링 서버에만 WebSocket을 연결하면 된다.
2. 

<img src = >

</br>


## :pushpin: 



</br>
</br>

## :pushpin: 기능 구현


</br>
</br>
</br>

## :pushpin:
<img src =>



</br></br>
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

