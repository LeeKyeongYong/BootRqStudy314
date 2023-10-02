package com.qrstudy.qrstudy.base.app;

import lombok.Getter;
import lombok.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Getter
    public static String  genFileDirPath;

    @Value("${custom.genFile.dirPath}")
    public void setFileDirPath(String genFileDirPath){
        AppConfig.genFileDirPath = genFileDirPath;
    }
}
