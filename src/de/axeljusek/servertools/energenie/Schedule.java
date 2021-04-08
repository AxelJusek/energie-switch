/**
 *
 */
package de.axeljusek.servertools.energenie;

import java.util.ArrayList;
import java.util.List;

/**
 * @author axel
 *
 */
public class Schedule {
	private byte[] timestamp = new byte[4];
	private List<ScheduleEntry> entries = new ArrayList<>();
	private byte loopPeriodMarker = (byte) Byte.toUnsignedInt(VerbindungEnerGenie.hexStringToByteArray("E5")[0]);
	private byte[] loopPeriod = new byte[4];
	private byte socketNr; // Socket-Nummber and Dummy bit - the most significant bit
	private byte[] checksum = new byte[2];

	// TODO Hier weitermachen.
}
