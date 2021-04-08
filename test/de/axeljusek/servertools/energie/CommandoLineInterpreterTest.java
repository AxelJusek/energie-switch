package de.axeljusek.servertools.energie;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import de.axeljusek.servertools.energie.CommandoLineInterpreter;

@Disabled
public class CommandoLineInterpreterTest {

	@Test
	public void testCommandoLineInterpreter() throws IOException {
		CommandoLineInterpreter cli = new CommandoLineInterpreter(new String[] {});
		assertNotNull(cli, "Ein CommandoLineInterpreter laesst sich erstellen.");
	}

	@Test
	public void testACLISchaltenEin() throws IOException {
		CommandoLineInterpreter cli = new CommandoLineInterpreter(new String[] { "-s", "3", "-d", "true" });
		assertNotNull(cli, "Schalten der Dose 3 ein");
	}

	@Test
	public void testCLIHelp() throws IOException {
		CommandoLineInterpreter cli = new CommandoLineInterpreter(new String[] { "-h" });
		assertNotNull( cli,"Aufruf des Hilfetextes mit -h");
	}

	@Test
	public void testCLIHelp2() throws IOException {
		CommandoLineInterpreter cli = new CommandoLineInterpreter(new String[] { "-help" });
		assertNotNull(cli, "Aufruf des Hilfetextes mit -help");
	}

	@Test
	public void testCLIStatus() throws IOException {
		CommandoLineInterpreter cli = new CommandoLineInterpreter(new String[] {});
		assertNotNull(cli, "Aufruf des Status");
	}

	@Test
	public void testZCLISchaltenAus() throws IOException {
		CommandoLineInterpreter cli = new CommandoLineInterpreter(new String[] { "-s", "3", "-d", "false" });
		assertNotNull(cli, "Schalten der Dose 3 aus");
	}

	@Test
	public void testCLIEinzelStatus() throws IOException {
		CommandoLineInterpreter cli = new CommandoLineInterpreter(new String[] { "-z", "3" });
		assertNotNull(cli, "Aufruf des Status fuer Dose 3");
	}
}
