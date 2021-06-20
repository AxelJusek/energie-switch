
package de.axeljusek.servertools.energie;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import de.axeljusek.servertools.energie.commandline.CommandoLineInterpreter;
import de.axeljusek.servertools.energie.communication.impl.VerbindungEnerGie;
import de.axeljusek.servertools.energie.configuration.Konfiguration;

@Disabled // This test requires a real switch to be present.
class CommandoLineInterpreterTest {
  private static String configFilename = "konfiguration.conf";
  private static String port;
  private static String ip;
  private static String passwd;
  
  @BeforeAll
  private static void prepare() {
    Konfiguration conf = Konfiguration.getInstanceForConfigFilename(configFilename);
    port = conf.getValueForKey("port");
    ip = conf.getValueForKey("ip_address");
    passwd = conf.getValueForKey("password");
  }

  @Test
  void testCommandoLineInterpreter() throws IOException {
    CommandoLineInterpreter cli =
        new CommandoLineInterpreter(new String[] {}, new VerbindungEnerGie());
    assertNotNull(cli, "Ein CommandoLineInterpreter laesst sich erstellen.");
  }

  @Test
  void testACLISchaltenEin() throws IOException {
    CommandoLineInterpreter cli = new CommandoLineInterpreter(
        new String[] {"-ip", ip, "-port", port, "-s", "3", "-d", "true", "-passwd", passwd}, new VerbindungEnerGie());
    assertNotNull(cli, "Schalten der Dose 3 ein");
  }

  @Test
  void testCLIHelp() throws IOException {
    CommandoLineInterpreter cli =
        new CommandoLineInterpreter(new String[] {"-h"}, new VerbindungEnerGie());
    assertNotNull(cli, "Aufruf des Hilfetextes mit -h");
  }

  @Test
  void testCLIHelp2() throws IOException {
    CommandoLineInterpreter cli =
        new CommandoLineInterpreter(new String[] {"-help"}, new VerbindungEnerGie());
    assertNotNull(cli, "Aufruf des Hilfetextes mit -help");
  }

  @Test
  void testCLIStatus() throws IOException {
    CommandoLineInterpreter cli =
        new CommandoLineInterpreter(new String[] {}, new VerbindungEnerGie());
    assertNotNull(cli, "Aufruf des Status");
  }

  @Test
  void testZCLISchaltenAus() throws IOException {
    CommandoLineInterpreter cli = new CommandoLineInterpreter(
        new String[] {"-ip", ip, "-port", port, "-s", "3", "-d", "false", "-passwd", passwd}, new VerbindungEnerGie());
    assertNotNull(cli, "Schalten der Dose 3 aus");
  }

  @Test
  void testCLIEinzelStatus() throws IOException {
    CommandoLineInterpreter cli =
        new CommandoLineInterpreter(
            new String[] {"-ip", ip, "-port", port, "-z", "3", "-passwd", passwd}, new VerbindungEnerGie());
    assertNotNull(cli, "Aufruf des Status fuer Dose 3");
  }
}
