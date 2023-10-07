package com.qrstudy.qrstudy.base.app;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Getter
    public static String  genFileDirPath;
    @Getter
    public static String siteName;
    @Getter
    public static String siteBaseUrl;

    @Value("${custom.genFile.dirPath}")
    public void setGenFileDirPath(String genFileDirPath){
        AppConfig.genFileDirPath = genFileDirPath;
    }

    @Value("${custom.site.name}")
    public void setSiteName(String siteName){
        AppConfig.siteName = siteName;
    }

    @Value("${custom.site.baseUrl}")
    public void setSiteBaseUrl(String siteBaseUrl){
        AppConfig.siteBaseUrl = siteBaseUrl;
    }
}
