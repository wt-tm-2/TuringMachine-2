/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package karel.application.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Michael Johnson
 */
public class KarelApplicationProperties {
    private final String directoryProperty = "last-used-directory";
    
    private final Properties properties;
    
    public KarelApplicationProperties() {
        properties = new Properties();
        loadDefaultProperties();
        loadApplicationProperties();
    }
    
    private void loadDefaultProperties() {
        try (FileInputStream propertiesFile = new FileInputStream("defaultProperties")) {
            properties.load(propertiesFile);
        } catch(FileNotFoundException ex) {
            
        } catch(IOException ex) {
            
        }
    }
    
    private void loadApplicationProperties() {
        try (FileInputStream propertiesFile = new FileInputStream("applicationProperties")) {
            properties.load(propertiesFile);
        } catch (FileNotFoundException ex) {
            
        } catch (IOException ex) {
            
        }
    }
    
    public String getLastUsedDirectory() {
        if (properties.containsKey(directoryProperty)) {
            return properties.getProperty(directoryProperty);
        } else {
            return System.getProperty("user.dir");
        }
    }
    
    public void setLastUsedDirectory(String directory) {
        properties.setProperty(directoryProperty, directory);
    }
    
    public void saveApplicationProperties() {
        try (FileOutputStream propertiesFile = new FileOutputStream("applicationProperties")) {
            properties.store(propertiesFile, "Karel Application Properties");
        } catch (IOException ex) {
            
        }
    }
}
