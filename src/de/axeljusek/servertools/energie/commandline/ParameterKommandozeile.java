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
package de.axeljusek.servertools.energie.commandline;

public enum ParameterKommandozeile {
	HilfeAnzeigen("-h", false, null,"", "Zeigt diesen Hilfetext an."),
	HilfeAnzeigen2("-help", false, null, "", "siehe -h"),
	SchaltenEinerDoseGewuenschterZustand("-d", true, null, "true | false", "Der gewuenschte Schaltzustand: True steht fuer ein und False fuer aus."),
	SchaltenEinerDoseDosenNr("-s", true, ParameterKommandozeile.SchaltenEinerDoseGewuenschterZustand, "0 | 1 | 2 | 3", "Die Nummer der Dose die geschaltet werden soll."),
	IpAdresseAngeben("-ip", true, null, "z.B. 192.168.0.254", "Die IP-Adresse über welche die Steckdosenleiste erreicht wird."),
	PortNrAngeben("-port", true, null, "z.B. 5000", "Die Port-Nummer auf welche die Steckdosenleiste hört."),
	ProfileNr("-profile", true, null, "", "Zur Zeit noch nicht realisiert."),
	ZustandEinerDoseAbfragen("-z", true, null, "-z 0", "Abfragen des Zustands der gegebenen Dose."),
	PasswordAngeben("-passwd", true, null, "       1", "Das Passwort fuer die Steckdosenleiste.");
	
	
	private String parameter="";
	private boolean mitWert = false;
	private String werteListe;
	private String erlaeuterungstext;
	private ParameterKommandozeile partnerWert = null;
	
	ParameterKommandozeile(String parameter, boolean mitWert, ParameterKommandozeile partnerWert, String werteListe, String erlaeuterungstext)
	{
		this.parameter=parameter;
		this.mitWert = mitWert;
		this.partnerWert = partnerWert;
		this.werteListe=werteListe;
		this.erlaeuterungstext=erlaeuterungstext;
	}
	
	public String getParameter()
	{
		return parameter;
	}
	
	public boolean mitWert()
	{
		return mitWert;
	}
	
	public String getWerteListe()
	{
		return werteListe;
	}
	
	public String getErlaeuterungstext()
	{
		return erlaeuterungstext;
	}
	
	public ParameterKommandozeile getPartnerWert()
	{
		return this.partnerWert;
	}
}
