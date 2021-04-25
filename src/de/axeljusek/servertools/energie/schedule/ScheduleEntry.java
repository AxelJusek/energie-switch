/*******************************************************************************
 *
 *    Licensed under GPL-3.0-only or GPL-3.0-or-later
 *  
 *  	This file is part of EnergieSwitch.
 *  
 *    EnergieSwitch is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *  
 *    EnergieSwitch is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *  
 *    You should have received a copy of the GNU General Public License
 *    along with EnergieSwitch.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *    Diese Datei ist Teil von EnergieSwitch.
 *  
 *    EnergieSwitch ist Freie Software: Sie können es unter den Bedingungen
 *    der GNU General Public License, wie von der Free Software Foundation,
 *    Version 3 der Lizenz oder (nach Ihrer Wahl) jeder neueren
 *    veröffentlichten Version, weiter verteilen und/oder modifizieren.
 *  
 *   EnergieSwitch wird in der Hoffnung, dass es nützlich sein wird, aber
 *    OHNE JEDE GEWÄHRLEISTUNG, bereitgestellt; sogar ohne die implizite
 *    Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
 *    Siehe die GNU General Public License für weitere Details.
 *  
 *    Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
 *    Programm erhalten haben. Wenn nicht, siehe <https://www.gnu.org/licenses/>.
 *  
 *    Copyright 2021 Axel Jusek
 *  
 *******************************************************************************/
package de.axeljusek.servertools.energie.schedule;

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
