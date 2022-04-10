/*******************************************************************************
 *
 * Licensed under GPL-3.0-only or GPL-3.0-or-later
 * 
 * This file is part of EnergieSwitch.
 * 
 * EnergieSwitch is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * EnergieSwitch is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with EnergieSwitch. If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * Diese Datei ist Teil von EnergieSwitch.
 * 
 * EnergieSwitch ist Freie Software: Sie können es unter den Bedingungen der GNU General Public
 * License, wie von der Free Software Foundation, Version 3 der Lizenz oder (nach Ihrer Wahl) jeder
 * neueren veröffentlichten Version, weiter verteilen und/oder modifizieren.
 * 
 * EnergieSwitch wird in der Hoffnung, dass es nützlich sein wird, aber OHNE JEDE GEWÄHRLEISTUNG,
 * bereitgestellt; sogar ohne die implizite Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN
 * BESTIMMTEN ZWECK. Siehe die GNU General Public License für weitere Details.
 * 
 * Sie sollten eine Kopie der GNU General Public License zusammen mit diesem Programm erhalten
 * haben. Wenn nicht, siehe <https://www.gnu.org/licenses/>.
 * 
 * Copyright 2021, 2022 Axel Jusek
 * 
 *******************************************************************************/

package de.axeljusek.servertools.energie.configuration;

public enum Configurationsvalues {
  PORT("port", "Number", "5", "5000"), 
  IP_ADDRESS("ip_address", "String", "15", "192.168.0.254"), 
  PASSWORD("password", "String", "8", "       1");

  private final String name;
  private final String dataType;
  private final String length;
  private final String defaultValue;

  Configurationsvalues(String name, String dataType, String length, String defaultValue) {
    this.name = name;
    this.dataType = dataType;
    this.length = length;
    this.defaultValue = defaultValue;
  }

  public String getName() {
    return this.name;
  }

  public String getDataType() {
    return this.dataType;
  }

  public String getLength() {
    return this.length;
  }

  public String getDefaultValue() {
    return this.defaultValue;
  }
}
