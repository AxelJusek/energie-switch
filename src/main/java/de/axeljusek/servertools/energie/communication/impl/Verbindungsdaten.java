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

import java.util.SortedSet;

import de.axeljusek.servertools.energie.commandline.ParameterKommandozeile;
import de.axeljusek.servertools.energie.configuration.Konfiguration;

public class Verbindungsdaten {

  private String ipAddress = "";
  private String portNr = "";
  private String password = "";


  private Verbindungsdaten(String ipAddress, String portNr, String password) {
    this.ipAddress = ipAddress;
    this.portNr = portNr;
    this.password = password;
  }

  private static Verbindungsdaten getVerbindungsDaten(String ip_address, String port,
      String passwd) {
    Konfiguration conf = Konfiguration.getInstance();

    if (null == ip_address) {
      ip_address = conf.getValueForKey("ip_address");
    }

    if (null == passwd) {
      passwd = conf.getValueForKey("password");
    }
    while (8 > passwd.length()) // Ist fest in der Steckdosenleiste
                                // eingebrannt - Passwortlaenge ist 8
                                // Zeichen - rechtsbuendig
    {
      passwd = " " + passwd;
    }

    if (null == port) {
      port = conf.getValueForKey("port");
    }

    return new Verbindungsdaten(ip_address, port, passwd);
  }

  public static Verbindungsdaten getVerbindungsDatenFromCommandos(SortedSet<Commando> commandos) {
    String ipAddress = null;
    String port = null;
    String passwd = null;

    if (null != commandos) {
      for (Commando commando : commandos) {
        if (ParameterKommandozeile.IpAdresseAngeben.equals(commando.getType())) {
          ipAddress = commando.getParameter();
        } else if (ParameterKommandozeile.PortNrAngeben.equals(commando.getType())) {
          port = commando.getParameter();
        } else if (ParameterKommandozeile.PasswordAngeben.equals(commando.getType())) {
          passwd = commando.getParameter();
        }
      }
    }
    Verbindungsdaten verbindungsDaten =
        Verbindungsdaten.getVerbindungsDaten(ipAddress, port, passwd);
    return verbindungsDaten;
  }


  public String getIpAddress() {
    return this.ipAddress;
  }

  public String getPort() {
    return this.portNr;
  }

  public String getPasswd() {
    return this.password;
  }
}

