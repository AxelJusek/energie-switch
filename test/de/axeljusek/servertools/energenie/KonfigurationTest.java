package de.axeljusek.servertools.energenie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;

public class KonfigurationTest {

	@Test
	public void testKonfiguration() {
		Konfiguration conf = Konfiguration.getInstance();
		assertNotNull("Default File wurde angelegt.", conf);
	}

	@Test
	public void testKonfigurationFile() {
		File config = new File("konfiguration.conf");
		Konfiguration conf = new Konfiguration(config);
		assertNotNull("Starten mit Konfig-File.", conf);
		
	}

	@Test
	public void testGetValueForKey() {
		Konfiguration conf = Konfiguration.getInstance();
		assertNotNull("Default File wurde angelegt.", conf);
		
		String port = conf.getValueForKey("port");
		assertEquals("Prüfen ob der Wert auch stimmt.", "5000", port);
		
		String ip = conf.getValueForKey("ip_address");
		assertEquals("Prüfen ob die IP stimmt.","192.168.158.2", ip);
	}

}
