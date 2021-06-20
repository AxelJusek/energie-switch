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
 * Copyright 2021 Axel Jusek
 * 
 *******************************************************************************/
package de.axeljusek.servertools.energie.communication.impl;

public enum Schaltzustaende {

  Eingeschaltet(-191, 65, "0x41", "On", "txt_plug_is_on", true), Ausgeschaltet(-126, 130, "0x82",
      "Off", "txt_plug_is_off", true), Unklar(0, 0, "0xFF", "Dead", "txt_plug_is_unknown", true);

  private final Integer signedIntID;
  private final Integer unsignedIntID;
  private final String hexValue;
  private final String defaultExpression;
  private final String textKey;
  private final Boolean on;

  Schaltzustaende(Integer signedIntID, Integer unsignedIntID, String hexValue,
      String defaultExpression, String textKey, Boolean on) {
    this.signedIntID = signedIntID;
    this.unsignedIntID = unsignedIntID;
    this.hexValue = hexValue;
    this.defaultExpression = defaultExpression;
    this.textKey = textKey;
    this.on = on;
  }

  public Integer getSignedIntID() {
    return signedIntID;
  }

  public Integer getUnsignedIntID() {
    return unsignedIntID;
  }

  public String getHexValue() {
    return hexValue;
  }

  public String getDefaultExpression() {
    return defaultExpression;
  }

  public String getTextKey() {
    return textKey;
  }

  public Boolean getOn() {
    return on;
  }

  public static Schaltzustaende getZustandByInt(Integer id) {
    Schaltzustaende gesucht = Schaltzustaende.Unklar;
    if (id.equals(Eingeschaltet.getSignedIntID()) || id.equals(Eingeschaltet.getUnsignedIntID())) {
      gesucht = Schaltzustaende.Eingeschaltet;
    } else if (id.equals(Ausgeschaltet.getSignedIntID())
        || id.equals(Ausgeschaltet.getUnsignedIntID())) {
      gesucht = Schaltzustaende.Ausgeschaltet;
    }

    return gesucht;
  }

}
