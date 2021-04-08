package de.axeljusek.servertools.energie;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import de.axeljusek.servertools.energie.SchaltZustand;
import de.axeljusek.servertools.energie.VerbindungEnerGie;

public class SchaltZustandTest {

	//@Test
	public void testSchaltZustand() {
		fail("Not yet implemented");
	}

	//@Test
	public void testSchalten() {
		fail("Not yet implemented");
	}

	//@Test
	public void testGetSchaltZustand() {
		fail("Not yet implemented");
	}

	@Test
	public void testBerechneStatusWert() {
		VerbindungEnerGie veg = new VerbindungEnerGie();
		assertNotNull("Verbindungsobjekt wurder erfolgreich erzeugt.", veg);
		
		SchaltZustand sz = new SchaltZustand(veg);
		
		int dose=0;
		int sc0 = 44;
		int k1 = 32;
		int k0 = 49;
		int t3 = 7;
		int t2 = 119;
		
		int value = sz.berechneStatusWert(dose, sc0, k1, k0, t3, t2);
		Assert.assertEquals("Erster Test der Berechnung", 65, value);
		
		 sc0 = 237;
		 k1 = 32;
		 k0 = 49;
		 t3 = 7;
		 t2 = 119;
		 value = sz.berechneStatusWert(dose, sc0, k1, k0, t3, t2);
		 Assert.assertEquals("Erster Test der Berechnung", 130, value);
			
		 
		veg.verbindungTrennen();
		
	}

}
