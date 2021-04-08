/**
 *
 */
package de.axeljusek.servertools.energie;

/**
 * @author axel
 *
 */
public enum ControlAndPeriodScheduleEntry {
	OnceOn("00"), OnceOff("01"), PeriodicallyOn("02"), PeriodicallyOff("03");

	private String byteExpression;

	ControlAndPeriodScheduleEntry(String byteExpression) {
		this.byteExpression = byteExpression;
	}

	public byte getByte() {
		return (byte) Byte.toUnsignedInt(VerbindungEnerGie.hexStringToByteArray(this.byteExpression)[0]);
	}
}
