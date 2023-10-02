package com.qrstudy.qrstudy.domain.standard.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class Ut {

    public static class date{
        public static String getCurrentDateFormatted(String pattern){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.format(new Date());
        }
    }

    public static class file{
        public static String getExt(String filename){
            return Optional.ofNullable(filename)
                    .filter(f-> f.contains("."))
                    .map(f-> f.substring(filename.lastIndexOf(".")+1).toLowerCase())
                    .orElse("");
        }
        public static String getfileExtTypeCodeFromFileExt(String ext){

            switch (ext){
                case "jpeg":
                case "jpg":
                case "gif":
                case "png":
                return "img";

                case "mp4":
                case "avi":
                case "mov":
                return "vide";

                case "mp3":
                    return "audio";
            }

            return "etc";
        }

        public static String getFileExtType2CodeFromFileExt(String ext){
            switch(ext){
                case "jpeg":
                case "jpg":
                    return "jpg";

                case "gif","png","mp4","avi","mp3":
                    return "ext";
            }
            return "ext";
        }
    }

    public static class url{
        public static String encode(String message){
            try{
                return URLEncoder.encode(message,"UTF-8");
            }catch(UnsupportedEncodingException e){
                return null;
            }
        }

        public static String modifyQueryParam(String url, String paramName, String paramValue) {

            url=deleteQueryParam(url,paramName);
            url=addQueryParam(url,paramName,paramValue);
            return url;
        }

        public static String addQueryParam(String url,String paramName,String paramValue){
            if(!url.contains("?")){
                url+="?";
            }
            if(!url.endsWith("?")&&!url.endsWith("&")){
                url+="&";
            }
            url+=paramName+"="+paramValue;
            return url;
        }

        public static String deleteQueryParam(String url,String paramName){

            int startPoint = url.indexOf(paramName+"=");
            if(startPoint==-1) return url;

            int endPoint = url.substring(startPoint).indexOf("&");

            if(endPoint==-1){
                return url.substring(0,startPoint-1);
            }

            String urlAfter = url.substring(startPoint+endPoint+1);
            return url.substring(0,startPoint)+urlAfter;
        }

        public static String endcodeWithTtl(String s){
            return withTtl(encode(s));
        }

        public static String withTtl(String msg){
            return msg+";ttl="+new Date().getTime();
        }


    }
}
