package de.axeljusek.servertools.energie;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.io.IOException;
import java.net.Socket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import de.axeljusek.servertools.energie.communication.impl.VerbindungEnerGie;
import de.axeljusek.servertools.energie.configuration.Configuration;

@Disabled // This test requires a real switch to be present.
class VerbindungEnerGieTest {
  private static String configFilename = "konfiguration.conf";
  private static String ipAddress = "192.168.178.111";
  private static String port = "5000";
  private static String password = "       1";
  
  @BeforeAll
  private static void prepare() {
    Configuration conf = Configuration.getInstanceForConfigFilename(configFilename);
    port = conf.getValueForKey("port");
    ipAddress = conf.getValueForKey("ip_address");
    password = conf.getValueForKey("password");
  }

  @Test
  void testVerbindungEnerGenie() {
    VerbindungEnerGie veg = new VerbindungEnerGie();
    assertNotNull(veg, "Verbindungsobjekt wurder erfolgreich erzeugt.");
    veg.verbindungTrennen();
  }

  @Test
  void testGetSolutionForRequest() {
    VerbindungEnerGie veg = new VerbindungEnerGie();
    assertNotNull(veg, "Verbindungsobjekt wurde erfolgreich erzeugt.");

    byte[] task = VerbindungEnerGie.hexStringToByteArray("3f0f4b4c");
    byte[] key = veg.putPasswordInArray(password);
    byte[] solution = veg.getSolutionForRequest(key, task);

    assertArrayEquals(VerbindungEnerGie.hexStringToByteArray("84258c25"), solution);

    task = VerbindungEnerGie.hexStringToByteArray("c3ae68d0");
    solution = veg.getSolutionForRequest(key, task);

    assertArrayEquals(VerbindungEnerGie.hexStringToByteArray("3b0b3031"), solution);

    task = VerbindungEnerGie.hexStringToByteArray("7d44568a");
    solution = veg.getSolutionForRequest(key, task);

    assertArrayEquals(VerbindungEnerGie.hexStringToByteArray("bb312a2c"), solution);

    task = VerbindungEnerGie.hexStringToByteArray("98f3c0a5");
    solution = veg.getSolutionForRequest(key, task);

    assertArrayEquals(VerbindungEnerGie.hexStringToByteArray("d803e53a"), solution);

  }


  @Test
  void testProbeVerbindungAufbauen() throws IOException {
    VerbindungEnerGie veg = new VerbindungEnerGie();
    byte[] key = veg.putPasswordInArray(password);
    Socket socket = veg.neueTCPVerbindung(ipAddress, port);
    veg.anmelden(key, socket);
    veg.schalteDose(3, true);
    veg.verbindungTrennen();
    assertNotNull(veg);
  }

  @Test
  void testSchaltenAus() throws IOException {
    VerbindungEnerGie veg = new VerbindungEnerGie();
    byte[] key = veg.putPasswordInArray(password);
    Socket socket = veg.neueTCPVerbindung(ipAddress, port);
    veg.anmelden(key, socket);
    veg.schalteDose(3, false);
    veg.verbindungTrennen();
    assertNotNull(veg);
  }

}
