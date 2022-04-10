package de.axeljusek.servertools.energie.configuration;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import org.junit.jupiter.api.Test;

class KonfigurationTest {
  private String configFilename = "konfiguration.conf";
  
  @Test
  void testGetInstance() {
    Konfiguration conf = Konfiguration.getInstance();
    assertNotNull(conf, "Default File wurde angelegt.");
  }

  @Test
  void testGetInstanceForConfigFilename() {
    Konfiguration conf =  Konfiguration.getInstanceForConfigFilename(configFilename);
    assertNotNull(conf, "Starten mit Konfig-File.");
  }

  @Test
  void testGetKonfigurationFile() {
    Konfiguration conf = Konfiguration.getInstanceForConfigFilename(configFilename);
    assertNotNull(conf, "Default File wurde angelegt.");
  
    File configfile = Konfiguration.getKonfigurationFile(configFilename);
    assertEquals(configFilename, configfile.getName());
  }

  @Test
  void testGetValueForKey() {
    Konfiguration conf = Konfiguration.getInstanceForConfigFilename(configFilename);
    assertNotNull(conf, "Default File wurde angelegt.");

    String port = conf.getValueForKey("port");
    assertEquals("5000", port, "Prüfen ob der Wert auch stimmt.");

    String ip = conf.getValueForKey("ip_address");
    assertEquals("192.168.178.111", ip, "Prüfen ob die IP stimmt.");
  }

}
