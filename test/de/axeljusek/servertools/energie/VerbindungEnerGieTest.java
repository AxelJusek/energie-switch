package de.axeljusek.servertools.energie;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.Socket;

import org.junit.Assert;
import org.junit.Test;

import de.axeljusek.servertools.energie.VerbindungEnerGie;

public class VerbindungEnerGieTest {
	
	private String ipAddress="192.168.158.2";
	private String port = "5000";
	private String password = "       1";
	
	@Test
	public void testVerbindungEnerGenie() {
		VerbindungEnerGie veg = new VerbindungEnerGie();
		assertNotNull("Verbindungsobjekt wurder erfolgreich erzeugt.", veg);
		veg.verbindungTrennen();
	}

//	@Test
	public void testGetStatusEnerGen() {
		fail("Not yet implemented");
	}

//	@Test
	public void testVerbindungTrennen() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSolutionForRequest()
	{
		VerbindungEnerGie veg = new VerbindungEnerGie();
		assertNotNull("Verbindungsobjekt wurde erfolgreich erzeugt.", veg);
		
		byte[] task = VerbindungEnerGie.hexStringToByteArray("3f0f4b4c");
		byte[] key = veg.putPasswordInArray(password);
		byte[] solution = veg.getSolutionForRequest(key,task);
		
		Assert.assertArrayEquals(VerbindungEnerGie.hexStringToByteArray("84258c25"), solution);		
		
		task = VerbindungEnerGie.hexStringToByteArray("c3ae68d0");
		solution = veg.getSolutionForRequest(key,task);
		
		Assert.assertArrayEquals(VerbindungEnerGie.hexStringToByteArray("3b0b3031"), solution);
		
		task = VerbindungEnerGie.hexStringToByteArray("7d44568a");
		solution = veg.getSolutionForRequest(key,task);
		
		Assert.assertArrayEquals(VerbindungEnerGie.hexStringToByteArray("bb312a2c"), solution);
		
		task = VerbindungEnerGie.hexStringToByteArray("98f3c0a5");
		solution = veg.getSolutionForRequest(key,task);
		
		Assert.assertArrayEquals(VerbindungEnerGie.hexStringToByteArray("d803e53a"), solution);
		
	}
	
	
	@Test
	public void testProbeVerbindungAufbauen() throws IOException
	{
		VerbindungEnerGie veg = new VerbindungEnerGie();
		byte[] key = veg.putPasswordInArray(password);
		Socket socket = veg.neueTCPVerbindung(ipAddress, port);
		veg.anmelden(key, socket);	
		veg.schalteDose(3, true);
		veg.verbindungTrennen();
	}
	
	@Test
	public void testSchaltenAus() throws IOException
	{
		VerbindungEnerGie veg = new VerbindungEnerGie();
		byte[] key = veg.putPasswordInArray(password);
		Socket socket = veg.neueTCPVerbindung(ipAddress, port);
		veg.anmelden(key, socket);	
		veg.schalteDose(3, false);
		veg.verbindungTrennen();
	}
	
}
