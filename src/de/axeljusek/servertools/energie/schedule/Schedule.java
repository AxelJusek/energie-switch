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

import java.util.ArrayList;
import java.util.List;

import de.axeljusek.servertools.energie.communication.VerbindungEnerGie;

/**
 * @author axel
 *
 */
public class Schedule {
	private byte[] timestamp = new byte[4];
	private List<ScheduleEntry> entries = new ArrayList<>();
	private byte loopPeriodMarker = (byte) Byte.toUnsignedInt(VerbindungEnerGie.hexStringToByteArray("E5")[0]);
	private byte[] loopPeriod = new byte[4];
	private byte socketNr; // Socket-Nummber and Dummy bit - the most significant bit
	private byte[] checksum = new byte[2];

	// TODO Hier weitermachen.
}
