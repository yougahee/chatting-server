package com.morse.chatting_server.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class LiveCheckService {

	private static final Gson gson = new GsonBuilder().create();

	private String ROOM_BASE_URL = "http://downsups.onstove.com:8003/room/live";
	private JsonObject jsonObject = new JsonObject();
	private HttpClient client = HttpClientBuilder.create().build();

	public boolean checkLiveRoom(Long presenterIdx) throws IOException {
		HttpPost httpPost = new HttpPost(ROOM_BASE_URL + "/check");

		jsonObject.addProperty("presenterIdx", presenterIdx.toString());
		StringEntity entity = new StringEntity(jsonObject.toString());
		httpPost.setEntity(entity);

		HttpResponse response = client.execute(httpPost);

		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String responseJson = EntityUtils.toString(response.getEntity());
			jsonObject = gson.fromJson(responseJson, JsonObject.class);

			return jsonObject.get("data").getAsJsonObject().get("alive").getAsBoolean();
		}
		return false;
	}

	//## 시그널링 서버에게 reconnect 요청
	public void reconnectRequest(Long presenterIdx) throws IOException {
		//## uri 바꿔야 함
		HttpPost httpPost = new HttpPost(ROOM_BASE_URL + "/check");

		jsonObject.addProperty("presenterIdx", presenterIdx.toString());
		StringEntity entity = new StringEntity(jsonObject.toString());
		httpPost.setEntity(entity);

		HttpResponse response = client.execute(httpPost);

		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String responseJson = EntityUtils.toString(response.getEntity());
			jsonObject = gson.fromJson(responseJson, JsonObject.class);

			//## 이것도 수정
			jsonObject.get("data").getAsJsonObject().get("alive").getAsBoolean();
		} else {
			// ## exception 처리
		}
	}
}
