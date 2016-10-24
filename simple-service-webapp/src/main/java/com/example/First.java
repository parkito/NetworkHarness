package com.example;

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
    @Produces("application/xml")
    public Response convertFtoC() throws JSONException {
        Sent sent = new Sent("myData");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Data", sent);
        return Response.status(200).entity(jsonObject).build();
    }
}
