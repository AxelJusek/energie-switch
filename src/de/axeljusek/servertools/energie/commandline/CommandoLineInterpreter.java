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

import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.axeljusek.servertools.energie.communication.Commando;
import de.axeljusek.servertools.energie.communication.ConnectionEnergie;
import de.axeljusek.servertools.energie.communication.Verbindungsdaten;

/**
 * @author axel
 *
 */
public class CommandoLineInterpreter {

	@Inject
	private ConnectionEnergie conEnergie;

	static Logger log = LogManager.getLogger("de.axeljusek.servertools.energenie");

	@Inject
	public CommandoLineInterpreter(String[] args) {
		SortedSet<Commando> operationsParameter = null;

		operationsParameter = parameterAuswerten(args);

		try {
			commandoUmsetzen(operationsParameter);
		} catch (IOException e) {
			log.error(e);
		}
	}

	private SortedSet<Commando> parameterAuswerten(String[] args) {
		SortedSet<Commando> operationsParameter = null;
		int anzahlParameter = args.length;
		if (1 > anzahlParameter) {
			log.error("Ein Auftruf mit 0 Parametern ist nicht moeglich. Fuehrt zur Anzeige des Status.");
		} else {
			operationsParameter = putParametersInCommandoSet(args);
		}
		return operationsParameter;
	}

	private void commandoUmsetzen(SortedSet<Commando> commandos) throws IOException {
		// Die Verbindungsdaten aus den Commandos auspacken
		Verbindungsdaten verbindungsDaten = Verbindungsdaten.getVerbindungsDatenFromCommandos(commandos);

		if (null != commandos) {
			for (Commando commando : commandos) {
				String doseNr = commando.getParameter();
				switch (commando.getType()) {
				case HilfeAnzeigen:
					hilfetextAusgeben();
					break;
				case HilfeAnzeigen2:
					hilfetextAusgeben();
					break;
				case IpAdresseAngeben:
					break;
				case PasswordAngeben:
					break;
				case PortNrAngeben:
					break;
				case ProfileNr:
					break;
				case SchaltenEinerDoseDosenNr:
					String einschalten = commando.getFollowParameter();
					conEnergie.doseSchalten(verbindungsDaten, doseNr, einschalten);
					break;
				case SchaltenEinerDoseGewuenschterZustand:
					break;
				case ZustandEinerDoseAbfragen:
					conEnergie.zustandAbfragen(verbindungsDaten, doseNr);
					break;
				default:
					conEnergie.schaltzustaendeAusgeben(verbindungsDaten);
					break;
				}
			}
		} else {
			conEnergie.schaltzustaendeAusgeben(verbindungsDaten);
		}
	}



	protected SortedSet<Commando> putParametersInCommandoSet(String[] args) {

		SortedSet<Commando> operationsParameter = new TreeSet<>();

		operationsParameter = convertIntoCommandos(args, operationsParameter);

		operationsParameter = consolidateCommandos(operationsParameter);

		log.info("Parameter wurden ausgewertet.");
		log.info("Anzahl ist:" + operationsParameter.size());

		return operationsParameter;
	}

	// TODO Diese Methode sollte entsprechend den Einstellungen im ENUM
	// arbeiten.
	private SortedSet<Commando> consolidateCommandos(SortedSet<Commando> operationsParameter) {
		SortedSet<Commando> zuloeschendeCommandos = new TreeSet<>();

		// Schaltziele den Dosennummern zuordnen.
		for (Commando commando : operationsParameter) {
			if (ParameterKommandozeile.SchaltenEinerDoseGewuenschterZustand.equals(commando.getType())) {
				String schaltZiel = commando.getParameter();
				zuloeschendeCommandos.add(commando);

				Integer index = commando.getIndex();
				for (Commando commando2 : operationsParameter) {
					if (commando2.getIndex() == (index - 2)) {
						commando2.setFollowParameter(schaltZiel);
						break;
					}
				}
			}
		}

		// Verarbeitete Commandos entfernen
		for (Commando commando : zuloeschendeCommandos) {
			operationsParameter.remove(commando);
		}

		return operationsParameter;
	}

	private SortedSet<Commando> convertIntoCommandos(String[] args, SortedSet<Commando> operationsParameter) {
		int anzahlParameter = args.length;
		String argument = "";

		for (int i = 0; i < anzahlParameter; i++) {
			argument = args[i];

			ParameterKommandozeile[] paramIter = ParameterKommandozeile.values();
			for (int j = 0; j < paramIter.length; j++) {
				if (paramIter[j].getParameter().equals(argument)) {
					Commando commando = new Commando(paramIter[j], i);
					if (true == paramIter[j].mitWert()) {
						i++;
						argument = args[i];
						commando.setParameter(argument);
						operationsParameter.add(commando);
					} else {
						operationsParameter.add(commando);
					}

					j = paramIter.length;
				}
			}
		}
		return operationsParameter;
	}

	private void hilfetextAusgeben() {
		System.out.println("Hier k��nnte Ihr Hilfetext stehen \n");

		System.out.println("Diese Software ben��tigt JDK 11 und aktueller und eine Konfigurationsdatei.\n");
		System.out.println(
				"Ist die Konfigurationsdatei nicht vorhanden, so wird sie in dem Home-Verzeichnis des verwendeten Anwenders angelegt. "
						+ "Die Konfigurationsdatei liegt im Verzeichnis ~/.energenie_switch und wird angelegt aber nicht beschrieben. "
						+ "Bleibt die Konfigurationsdatei leer, so werden die in der Software vorgesehen Standardwert verwendet. Diese k��nnen "
						+ "durch Kommandozeilen-Parameter ��berschrieben werden. \n");
		System.out.println("Die folgenden Parameter k��nnen in der Konfigurationsdatei hinterlegt werden:");
		System.out.println(
				"ip_address - Die IP-Adresse der Steckdosenleiste, die genutzt werden soll, Standard ist 192.168.0.254 .");
		System.out.println("port - Die Port-Nr auf welche die Steckdosenleiste h��rt, Standard ist 5000 .");
		System.out.println(
				"password - Das genau acht Zeichen lange Passwort, Standard ist '       1'. Leerzeichen werden genutzt!");
		System.out.println(" ");
		System.out.println(
				"Der Aufbau ist zeilenweise mit \"Parameter\"=\"Wert\". Dabei werden die Leerzeichen ab dem \'=\' gezaehlt.");
		System.out.println(" ");
		System.out.println("Verwendung der Software auf der Kommandozeile: ");
		for (ParameterKommandozeile para : ParameterKommandozeile.values()) {
			System.out.println(stretchString(para.getParameter(), 10) + "" + stretchString(para.getWerteListe(), 30)
					+ "" + para.getErlaeuterungstext() + "");
		}
		System.out.println(" ");
		System.out.println("Beispiele zur Veranschaulichung des Einsatzes");
		System.out.println(" ");
		System.out.println("Zur Ausgabe dieses Textes: $JAVA_HOME/bin/java -jar energenie_switch.jar -h");
		System.out.println(
				"Wenn die Verbindungsdaten (inkl. Passwort) korrekt in der Konfigurationsdatei erfasst wurden, dann "
						+ "kann mit \"$JAVA_HOME/bin/java -jar energenie_switch.jar -s 3 -d true\" z.B. die Dose 3 eingeschaltet werden.");
		System.out.println(
				"\n Das folgende Beispiel zeigt die Verwendung mit den ben��tigten Parametern zum Schalten einer Dose der Steckdosenleiste ohne die Verwendung einer Konfigurationsdatei.");
		System.out.println(
				"Ein Beispiel mit allen Parametern: \"/usr/bin/java -jar ./energenie_switch.jar -ip 192.168.0.254 -port 5000 -s 3 -d false -passwd '       1'\"");
		System.out.println("\n");
		System.out.println(
				"Um den Schaltzustand der Dosen abzufragen, kann die Software ohne Steuerparameter aufgerufen werden.");
	}

	private String stretchString(String text, int length) {
		while (length > text.length()) {
			text = text + " ";
		}
		return text;
	}
}
