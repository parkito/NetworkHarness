package REST;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.codec.binary.Base64;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;


@Path("myresource")
public class MyResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Sent readREST() throws IOException {

        String result = null;
        String URL = "http://localhost:8080/my";
        java.net.URL url = new URL(URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "Basic " +
                new String(new Base64().encodeBase64("b@b.ru:12345".getBytes())));
        if (conn.getResponseCode() != 200) {
            throw new IOException();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));
        result = br.readLine();
        Gson gson = new Gson();
        Type userListType = new TypeToken<Sent>() {
        }.getType();
        Sent sent = gson.fromJson(result, userListType);
        conn.disconnect();
        return sent;
    }

    public static void main(String[] args) throws IOException {
        MyResource myResource = new MyResource();
        System.out.println(myResource.readREST());
    }
}
