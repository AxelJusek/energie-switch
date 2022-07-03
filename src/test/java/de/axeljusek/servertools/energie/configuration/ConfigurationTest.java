package de.axeljusek.servertools.energie.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ConfigurationTest {
  private String configFilename = "konfiguration.conf";
  
  @Test
  void testGetInstance() {
    Configuration conf2 = Configuration.getInstance();
    conf2.clearConf();
    
    Configuration conf = Configuration.getInstance();
    assertNotNull(conf, "Default File wurde angelegt.");
  }

  @Test
  void testGetInstanceForConfigFilename() {
    Configuration conf2 = Configuration.getInstance();
    conf2.clearConf();
    
    Configuration conf =  Configuration.getInstanceForConfigFilename(configFilename);
    assertNotNull(conf, "Starten mit Konfig-File.");
    
    String uniqueFilename = configFilename + UUID.randomUUID().toString();    
    Configuration conf3 = Configuration.getInstanceForConfigFilename(uniqueFilename);
    assertNotNull(conf3, "Starten mit Unique-File.");  
  }

  @Test
  void testGetConfigurationFile() {
    Configuration conf2 = Configuration.getInstance();
    conf2.clearConf();
    Configuration conf = Configuration.getInstanceForConfigFilename(configFilename);
    assertNotNull(conf, "Default File wurde angelegt.");
  
    File configfile = Configuration.getConfigurationFile("", configFilename);
    assertEquals(configFilename, configfile.getName());
  }
  
  @Test
  void testGetConfigurationFileForNewDirectory() {
    Configuration conf2 = Configuration.getInstance();
    conf2.clearConf();
    String newDirConffile = "testfiles/";
    String newConfFile = "configurationFile.conf";
    Configuration conf = Configuration.getInstanceForConfigFilename(newDirConffile+newConfFile);
    assertNotNull(conf, "File wurde angelegt.");
  
    File configfile = Configuration.getConfigurationFile(newDirConffile, newConfFile);
    assertEquals(newConfFile, configfile.getName());
  }


  @Test
  void testGetValueForKey() {
    Configuration conf2 = Configuration.getInstance();
    conf2.clearConf();
    Configuration conf = Configuration.getInstanceForConfigFilename(configFilename);
    assertNotNull(conf, "Default File wurde angelegt.");

    String port = conf.getValueForKey("port");
    assertEquals("5000", port, "Prüfen ob der Wert auch stimmt.");

    String ip = conf.getValueForKey("ip_address");
    assertEquals("192.168.0.254", ip, "Prüfen ob die IP stimmt.");
  }

}
