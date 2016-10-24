package com.example;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.GetMethod;
import sun.net.www.http.HttpClient;

import java.io.IOException;

/**
 * Created by Artyom Karnov on 10/25/16.
 * artyom-karnov@yandex.ru
 **/
public class JavaStudyWS {


    public static void main(String[] args) throws IOException {
        String restServiceUrl = "http://localhost:8080/webapi/my";

        HttpClient httpClient;
        GetMethod getMethod = new GetMethod(restServiceUrl);

        Header mtHeader = new Header();
        mtHeader.setName("content-type");
        mtHeader.setValue("application/x-www-form-urlencoded");
        getMethod.addRequestHeader(mtHeader);

        mtHeader = new Header();
        mtHeader.setName("accept");
        mtHeader.setValue("application/xml");
        getMethod.addRequestHeader(mtHeader);

//        httpClient.executeMethod(getMethod);
        String output = getMethod.getResponseBodyAsString();

        System.out.println(output);
    }

}