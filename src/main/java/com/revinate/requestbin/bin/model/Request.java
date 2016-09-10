/*----------------------------------------
Model request
----------------------------------------*/
package com.revinate.requestbin.bin.model;

import org.apache.commons.lang3.NotImplementedException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Skeleton class that exposes some basic information that we want to store about a Request.
 * {
 *     "method": "POST",
 *     "body": "Hello World",
 *     "headers": {
 *         "Content-Type": ["application/json"]
 *     }
 * }
 *
 *
 * Add whatever you feel is missing.
 */
public class Request {
    public Map<String,Object> myRequest = new HashMap<String,Object>();
    public Request(HttpServletRequest thisRequest){
        myRequest.put("method",thisRequest.getMethod());
        parseHeader(thisRequest);
        parseBody(thisRequest);
    }
    /*
    request header parser
    takes the HttpServletRequest and convert the header to a map
    */
    public void parseHeader(HttpServletRequest thisRequest){
        String nextElement ;
        myRequest.put("headers",new HashMap<String,Object>());
        HashMap<String,Object> headers = (HashMap) myRequest.get("headers");
        Enumeration headerList = thisRequest.getHeaderNames();
        while(headerList.hasMoreElements()){
            nextElement = (String)headerList.nextElement();
            headers.put(nextElement,thisRequest.getHeader(nextElement));
        }
    }
    /*
    request body parser
    takes the HttpServletRequest and convert the body to a JSONstring
    */
    public void parseBody(HttpServletRequest thisRequest){
        String body = this.getBodyMessage(thisRequest);
        myRequest.put("body",body);
    }
    /*
    extract the requet method and store it seperately
    */
    public String getMethod() {
        return (String)this.myRequest.get("method");
    }
    /* 
    additional method to convert the body to string
    */
    public static String getBodyMessage(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = req.getReader();
            reader.mark(10000);
            String line = reader.readLine();
            while (line != null) {sb.append(line);line = reader.readLine();}
            reader.reset();
        } catch(Exception e) {
            return "empty";
        } 
        return (sb.toString() == null)?sb.toString():"empty";
    }
}
