package de.axeljusek.servertools.energie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;

import org.junit.jupiter.api.Test;

import de.axeljusek.servertools.energie.configuration.Konfiguration;

public class KonfigurationTest {

	@Test
	public void testKonfiguration() {
		Konfiguration conf = Konfiguration.getInstance();
		assertNotNull( conf, "Default File wurde angelegt.");
	}

	@Test
	public void testKonfigurationFile() {
		File config = new File("konfiguration.conf");
		Konfiguration conf = new Konfiguration(config);
		assertNotNull(conf, "Starten mit Konfig-File.");
		
	}

	@Test
	public void testGetValueForKey() {
		Konfiguration conf = Konfiguration.getInstance();
		assertNotNull( conf, "Default File wurde angelegt.");
		
		String port = conf.getValueForKey("port");
		assertEquals( "5000", port, "Prüfen ob der Wert auch stimmt.");
		
		String ip = conf.getValueForKey("ip_address");
		assertEquals("192.168.158.2", ip, "Prüfen ob die IP stimmt.");
	}

}
