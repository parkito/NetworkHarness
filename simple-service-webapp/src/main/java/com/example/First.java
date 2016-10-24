package com.example;

import com.owlike.genson.Genson;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Created by Artyom Karnov on 10/25/16.
 * artyom-karnov@yandex.ru
 **/
@Path("/my")
public class First {
    @GET
    @Produces("application/json")
    public Response convertFtoC() throws JSONException {
        Genson genson = new Genson();
        Sent sent = new Sent("myData");
        JSONObject jsonObject = new JSONObject();
        String rezult = genson.serialize(sent);
        return Response.status(200).entity(rezult).build();
    }
}
