package org.main.backend.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * Класс настроек приложения
 */
public class ApplicationProperties {

    private Properties properties;
    private static ApplicationProperties applicationProperties;

    /**
     * Конструктор класса, реализованный через Singleton.
     * Получаем настройки из файла propertiesFile
     */
    private ApplicationProperties(){
        properties = new Properties();
        URL urlProperties = getClass().getResource("/properties/org/main/properties/connection.properties");
        File propertiesFile =new File(urlProperties.getPath());
        try {
            if(!propertiesFile.exists()) propertiesFile.createNewFile();
            properties.load(new FileInputStream(propertiesFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получаем единичный экземпляр класса (Singleton)
     * @return экземпляр класса
     */
    public static ApplicationProperties getInstance(){
        if(applicationProperties==null) applicationProperties = new ApplicationProperties();
        return applicationProperties;
    }

    /**
     * Получаем значение по ключу
     * @param key - Сис. наименование настройки
     * @return значение сис. настройки в строковом представлении
     */
    public String getValue(String key){
        return properties.getProperty(key);
    }
}
