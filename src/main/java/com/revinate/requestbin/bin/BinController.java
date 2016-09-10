/*----------------------------------------
Controller
----------------------------------------*/
package com.revinate.requestbin.bin;

import com.revinate.requestbin.bin.model.Bin;
import com.revinate.requestbin.bin.model.Request;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * A Bin is an entity that has a URL and who store any HTTP request that was made against it.
 * It also have an inspect sub resource that allow to inspect the requests made on this bin.
 */
@RestController
public class BinController {
    private String port = "8080";   //port that the application is listenning
    private String ip = "localhost:"+port;  //combine the ip and port together
    @Autowired(required=true)
    private HttpServletRequest request; // autowire the request 
    private boolean touched = false; // indicator for bin creation
    private int count = 0; // bin count
    private Map<String,Bin> bList = new HashMap<String,Bin>(); //Map the bins to its bin IDs
    public String create(){
        // buildin method to call the createBin method
        String id ;
        Map<String,String> newBin = createBin();
        id = newBin.get("id");
        return id;
    }
    /*---------------------------------------------------
    createBin method
    Method returns success status otherwise returns error
    ---------------------------------------------------*/
    public Map<String,String> createBin(){
        Map<String,String> returnBody = new HashMap<String,String>();
        try{
            bList.put("bin"+count,new Bin());
            returnBody.put("status","New Bin Created");
            returnBody.put("id","bin"+count);
            count += 1;
            this.touched = true;
            return returnBody;
        }catch(Exception e){returnBody.put("Status","Error");return returnBody;}
    }
    /*---------------------------------------------------
    API entry for root call
    returns some basic information
    ---------------------------------------------------*/
    @RequestMapping("/")
    public ResponseEntity<?> home(){
        Map<String,String> returnBody = new HashMap<String,String>();
        returnBody.put("By","Chu Lin");
        returnBody.put("Application","Request Bin");
        returnBody.put("Enviorment","Java");
        return new ResponseEntity<>(returnBody, HttpStatus.OK);
    }
    /*---------------------------------------------------
    API entry for /bin post call
    it creates a bin an returns the id and url associate to it
    ---------------------------------------------------*/
    @RequestMapping(value="/bin",method=RequestMethod.POST) 
    public ResponseEntity<?> createBinViewer(){
        //calls the createBin method and return the url.
        Map<String,String> returnBody = createBin();
        if (returnBody.get("status") == "Error"){
            return new ResponseEntity<>(returnBody, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(returnBody, HttpStatus.OK);
    }
    /*---------------------------------------------------
    API entry for /bin call (all other method)
    returns all bin and its Url
    ---------------------------------------------------*/
    @RequestMapping(value="/bin") 
    public ResponseEntity<?> getAllBin(){
        Map<String,String> returnBody = new HashMap<String,String>();
        System.out.println("in");
        // if no bin is created (!touched), return error
        if (!this.touched){
            returnBody.put("status","No Bin Found");
            returnBody.put("PostToCreateBin","http://"+ip+"/bin");
            return new ResponseEntity<>(returnBody, HttpStatus.BAD_REQUEST);
        }
        for (int i = 0; i < count ; i ++){
            //else construct a list url of all bins
            returnBody.put("bucket"+Integer.toString(i),"http://"+ip+"/bin"+Integer.toString(i));
        }
        return new ResponseEntity<>(returnBody, HttpStatus.OK);
    }
    /*---------------------------------------------------
    API entry for /bin{id} (all method)
    it takes a url encoded parameter as mapping key and 
    and log the request to the bin with that key
    ---------------------------------------------------*/
    @RequestMapping("/bin/{id}")
    public ResponseEntity<?> storeRequest(@PathVariable(value="id") String id){
        Map<String,String> returnBody = new HashMap<String,String>();
        try {
            Bin myBin = bList.get(id); // throw exception if it is invaild id
            try{
                myBin.addRequest(Long.toString(System.currentTimeMillis()),request); //add the request and the key for this request is the current time 
                returnBody.put("status","success");
                return new ResponseEntity<>(returnBody, HttpStatus.OK);
            }catch(Exception e){System.out.println(e.toString());returnBody.put("status","error");return new ResponseEntity<>(returnBody, HttpStatus.BAD_REQUEST);}
        }catch(Exception e){returnBody.put("error","Invalid ID");return new ResponseEntity<>(returnBody, HttpStatus.BAD_REQUEST);}
    }
    /*---------------------------------------------------
    API entry for /bin{id}/inspect (all method)
    it takes a url encoded parameter as mapping key and
    returns everything inside of the bin
    ---------------------------------------------------*/
    @RequestMapping("/bin/{id}/inspect")
    public ResponseEntity<?> inspectRequest(@PathVariable(value="id") String id){
        Map<String,Object> returnBody = new HashMap<String,Object>();
        try {
            Bin myBin = bList.get(id);
            try {
                Map<String,Object> requestRecord = myBin.getAllRequests();
                return new ResponseEntity<Map<String,Object>>(requestRecord, HttpStatus.OK);
            }catch(Exception e){
                returnBody.put("status","error");
                System.out.println(e.toString());
                return new ResponseEntity<>(returnBody, HttpStatus.BAD_REQUEST);
            }
        }catch(Exception e){returnBody.put("error","Invalid ID");return new ResponseEntity<>(returnBody, HttpStatus.BAD_REQUEST);}
    }
}
