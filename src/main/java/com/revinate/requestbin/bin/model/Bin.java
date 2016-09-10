/*----------------------------------------
Model Bin 
Require model reqest
----------------------------------------*/
package com.revinate.requestbin.bin.model;

import org.apache.commons.lang3.NotImplementedException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import com.revinate.requestbin.bin.model.Request;


public class Bin {
    // map id -> request
    private Map<String, Request> requestCollection;
    // contructor for class, 
    public Bin(){
        requestCollection = new HashMap<String, Request>();
    }
    // returns all the request in this particular bin
    public Map<String,Object> getAllRequests() {
        // get all keys in the collection
        List<String> keys = new ArrayList(requestCollection.keySet());
        Map<String,Object> allRequest = new HashMap<String,Object>();
        // one by one get the request and convert it to map(json)
        for (int i = 0; i < keys.size(); i++){
            allRequest.put(keys.get(i),getRequests(keys.get(i)));
        }
        return allRequest;
    }
    //get a particular request 
    public Map<String,Object> getRequests(String id) {
        this.lastId = id;
        Request found = (Request) this.requestCollection.get(id);
        return found.myRequest;
    }
    // add a request to the collection
    public void addRequest(String id,HttpServletRequest thisRequest) {
        this.requestCollection.put(id,new Request(thisRequest));
        return;
    }
}
