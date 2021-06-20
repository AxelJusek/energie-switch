package de.axeljusek.servertools.energie;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import de.axeljusek.servertools.energie.commandline.CommandoLineInterpreter;
import de.axeljusek.servertools.energie.communication.impl.VerbindungEnerGie;

//TODO - dieser Test hängt von der Konfig-Datei im User-Home-Verzeichnis ab - FIXME
class CommandoLineInterpreterTest {

  @Test
  void testCommandoLineInterpreter() throws IOException {
    CommandoLineInterpreter cli =
        new CommandoLineInterpreter(new String[] {}, new VerbindungEnerGie());
    assertNotNull(cli, "Ein CommandoLineInterpreter laesst sich erstellen.");
  }

  @Test
  void testACLISchaltenEin() throws IOException {
    CommandoLineInterpreter cli = new CommandoLineInterpreter(
        new String[] {"-s", "3", "-d", "true"}, new VerbindungEnerGie());
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
        new String[] {"-s", "3", "-d", "false"}, new VerbindungEnerGie());
    assertNotNull(cli, "Schalten der Dose 3 aus");
  }

  @Test
  void testCLIEinzelStatus() throws IOException {
    CommandoLineInterpreter cli =
        new CommandoLineInterpreter(new String[] {"-z", "3"}, new VerbindungEnerGie());
    assertNotNull(cli, "Aufruf des Status fuer Dose 3");
  }
}
