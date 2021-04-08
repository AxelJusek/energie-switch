/**
 *
 */
package de.axeljusek.servertools.energie;

/**
 * @author axel
 *
 */
public class ScheduleEntry {

	ControlAndPeriodScheduleEntry controlAndPeriodByte; // one byte
	Byte[] startTime = new Byte[5]; // in seconds since 1. jan. 1970

	public ScheduleEntry(ControlAndPeriodScheduleEntry entryType, Byte[] startTime) {
		this.controlAndPeriodByte = entryType;
		this.startTime = startTime;
	}

	public byte[] getBytes() {
		byte[] bytes = new byte[6];
		bytes[0] = this.controlAndPeriodByte.getByte();
		bytes[1] = this.startTime[0];
		bytes[2] = this.startTime[1];
		bytes[3] = this.startTime[2];
		bytes[4] = this.startTime[3];
		bytes[5] = this.startTime[4];
		return bytes;
	}

}
