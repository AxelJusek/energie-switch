/*******************************************************************************
 *******************************************************************************/
package de.axeljusek.servertools.energie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import de.axeljusek.servertools.energie.communication.impl.SchaltZustand;
import de.axeljusek.servertools.energie.communication.impl.VerbindungEnerGie;

class SchaltZustandTest {

  @Test
  void testBerechneStatusWert() {
    VerbindungEnerGie veg = new VerbindungEnerGie();
    assertNotNull(veg, "Verbindungsobjekt wurder erfolgreich erzeugt.");
    SchaltZustand sz = new SchaltZustand(veg);

    int dose = 0;
    int sc0 = 44;
    int k1 = 32;
    int k0 = 49;
    int t3 = 7;
    int t2 = 119;

    int value = sz.berechneStatusWert(dose, sc0, k1, k0, t3, t2);
    assertEquals(65, value, "Erster Test der Berechnung");

    sc0 = 237;
    k1 = 32;
    k0 = 49;
    t3 = 7;
    t2 = 119;
    value = sz.berechneStatusWert(dose, sc0, k1, k0, t3, t2);
    assertEquals(130, value, "Erster Test der Berechnung");

    veg.verbindungTrennen();
  }

}
