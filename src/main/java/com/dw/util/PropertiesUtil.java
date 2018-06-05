package com.dw.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertiesUtil {
    private static final String CONFIG_PATH = System.getProperty("user.dir")  + File.separator + "config" + File.separator + "config.properties";
    private static Properties properties = null;

    public static String getStringByKeyO(String key) {
        if (properties == null) {
            properties = new Properties();
            InputStream in = null;
            try {
                in = new FileInputStream(CONFIG_PATH);
                properties.load(in);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties.getProperty(key);
    }

}

