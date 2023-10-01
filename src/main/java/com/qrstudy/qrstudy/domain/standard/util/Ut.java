package com.qrstudy.qrstudy.domain.standard.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Ut {

    public static class url{
        public static String encode(String message){
            try{
                return URLEncoder.encode(message,"UTF-8");
            }catch(UnsupportedEncodingException e){
                return null;
            }
        }
    }
}
