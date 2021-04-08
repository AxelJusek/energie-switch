package de.axeljusek.servertools.energie;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import de.axeljusek.servertools.energie.CommandoLineInterpreter;

public class CommandoLineInterpreterTest {

	@Test
	public void testCommandoLineInterpreter() throws IOException {
		CommandoLineInterpreter cli = new CommandoLineInterpreter(new String[] {});
		Assert.assertNotNull("Ein CommandoLineInterpreter laesst sich erstellen.", cli);
	}

	@Test
	public void testACLISchaltenEin() throws IOException {
		CommandoLineInterpreter cli = new CommandoLineInterpreter(new String[] { "-s", "3", "-d", "true" });
		Assert.assertNotNull("Schalten der Dose 3 ein", cli);
	}

	@Test
	public void testCLIHelp() throws IOException {
		CommandoLineInterpreter cli = new CommandoLineInterpreter(new String[] { "-h" });
		Assert.assertNotNull("Aufruf des Hilfetextes mit -h", cli);
	}

	@Test
	public void testCLIHelp2() throws IOException {
		CommandoLineInterpreter cli = new CommandoLineInterpreter(new String[] { "-help" });
		Assert.assertNotNull("Aufruf des Hilfetextes mit -help", cli);
	}

	@Test
	public void testCLIStatus() throws IOException {
		CommandoLineInterpreter cli = new CommandoLineInterpreter(new String[] {});
		Assert.assertNotNull("Aufruf des Status", cli);
	}

	@Test
	public void testZCLISchaltenAus() throws IOException {
		CommandoLineInterpreter cli = new CommandoLineInterpreter(new String[] { "-s", "3", "-d", "false" });
		Assert.assertNotNull("Schalten der Dose 3 aus", cli);
	}

	@Test
	public void testCLIEinzelStatus() throws IOException {
		CommandoLineInterpreter cli = new CommandoLineInterpreter(new String[] { "-z", "3" });
		Assert.assertNotNull("Aufruf des Status fuer Dose 3", cli);
	}
}
