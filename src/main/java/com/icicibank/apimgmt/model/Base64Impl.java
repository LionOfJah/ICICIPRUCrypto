package com.icicibank.apimgmt.model;

import org.apache.commons.codec.binary.Base64;

public class Base64Impl extends Base64 {

    
    private static Base64Impl singleton;
    /**
     * Create private constructor
     */
    private Base64Impl(){
        
    }
    /**
     * Create a static method to get instance.
     */
    public static Base64Impl getInstance(){
        if(singleton == null){
        	singleton = new Base64Impl();
        }
        return singleton;
    }
     
}
