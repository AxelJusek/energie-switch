/**
 *
 */
package de.axeljusek.servertools.energenie;

import java.io.IOException;

/**
 * @author axel
 *
 */
public class SwitchStart {

	/**
	 *
	 */
	public SwitchStart() {
		// Nix
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		new CommandoLineInterpreter(args);

	}

}
