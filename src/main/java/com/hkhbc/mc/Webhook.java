package com.hkhbc.mc;
import java.io.IOException;
import java.util.logging.Level;

//import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bukkit.plugin.java.JavaPlugin;

public class Webhook {	
	public void sendData(String data) throws IOException {
		StringEntity requestEntity = new StringEntity(data, ContentType.APPLICATION_JSON);
		JavaPlugin.getPlugin(Main.class).getLogger().log(Level.INFO, "Sending Round Data");
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost request = new HttpPost("http://mc.iamkevin.xyz:25808/werewolf");
			request.setEntity(requestEntity);
			httpClient.execute(request);
			request.releaseConnection();
			request = null;
			//HttpResponse rawResponse = httpClient.execute(request);
		} finally {
			httpClient.close();
			httpClient = null;
		}
	}
}
