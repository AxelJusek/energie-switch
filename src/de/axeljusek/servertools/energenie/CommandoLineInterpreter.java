/**
 *
 */
package de.axeljusek.servertools.energenie;

import java.io.IOException;
import java.net.Socket;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author axel
 *
 */
public class CommandoLineInterpreter {

	static Logger log = LogManager.getLogger("de.axeljusek.servertools.energenie");

	private Konfiguration conf = Konfiguration.getInstance();

	public CommandoLineInterpreter(String[] args) throws IOException {
		SortedSet<Commando> operationsParameter = null;

		operationsParameter = parameterAuswerten(args);

		commandoUmsetzen(operationsParameter);
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
		Verbindungsdaten verbindungsDaten = getVerbindungsDatenFromCommandos(commandos);

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
					doseSchalten(verbindungsDaten, doseNr, einschalten);
					break;
				case SchaltenEinerDoseGewuenschterZustand:
					break;
				case ZustandEinerDoseAbfragen:
					zustandAbfragen(verbindungsDaten, doseNr);
					break;
				default:
					schaltzustaendeAusgeben(verbindungsDaten);
					break;
				}
			}
		} else {
			schaltzustaendeAusgeben(verbindungsDaten);
		}
	}

	private void zustandAbfragen(Verbindungsdaten verbindungsDaten, String doseNr) throws IOException {
		VerbindungEnerGenie vEnGen = verbindungAufbauen(verbindungsDaten);
		Integer dose = Integer.parseInt(doseNr);
		SchaltZustand sz = new SchaltZustand(vEnGen);

		System.out.println(sz.getSchaltZustand(dose));

		vEnGen.verbindungTrennen();
	}

	private Verbindungsdaten getVerbindungsDatenFromCommandos(SortedSet<Commando> commandos) {
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
		Verbindungsdaten verbindungsDaten = getVerbindungsDaten(ipAddress, port, passwd);
		return verbindungsDaten;
	}

	private Verbindungsdaten getVerbindungsDaten(String ip_address, String port, String passwd) {

		if (null == ip_address) {
			ip_address = this.conf.getValueForKey("ip_address");
		}

		if (null == passwd) {
			passwd = this.conf.getValueForKey("password");
		}
		while (8 > passwd.length()) // Ist fest in der Steckdosenleiste
									// eingebrannt - Passwortlaenge ist 8
									// Zeichen - rechtsbuendig
		{
			passwd = " " + passwd;
		}

		if (null == port) {
			port = this.conf.getValueForKey("port");
		}

		return new Verbindungsdaten(ip_address, port, passwd);
	}

	private void schaltzustaendeAusgeben(Verbindungsdaten verbindungsDaten) throws IOException {
		VerbindungEnerGenie vEnGen = verbindungAufbauen(verbindungsDaten);

		SchaltZustand sz = new SchaltZustand(vEnGen);

		System.out.println("Verbindungsdaten zur Energenie LAN Steckdose:");
		System.out.println(sz.getSchaltZustandAlle());

		vEnGen.verbindungTrennen();
	}

	private void doseSchalten(Verbindungsdaten verbindungsDaten, String doseNr, String einschalten) throws IOException {
		VerbindungEnerGenie vEnGen = verbindungAufbauen(verbindungsDaten);

		SchaltZustand sz = new SchaltZustand(vEnGen);
		boolean ein = Boolean.parseBoolean(einschalten);
		int dose = Integer.parseInt(doseNr);
		sz.schalten(dose, ein);

		vEnGen.verbindungTrennen();
	}

	protected VerbindungEnerGenie verbindungAufbauen(Verbindungsdaten verbindungsDaten) throws IOException {

		VerbindungEnerGenie vEnGen = new VerbindungEnerGenie();
		byte[] key = vEnGen.putPasswordInArray(verbindungsDaten.getPasswd());

		log.info("Es werden die folgenden Parameter verwendet: \n" + "IP-Adresse '" + verbindungsDaten.getIpAddress()
				+ "'\n" + "Port '" + verbindungsDaten.getPort() + "'\n" + "Passwort '" + verbindungsDaten.getPasswd()
				+ "'\n" + "Key '" + key.length + "'\n");

		Socket socket = vEnGen.neueTCPVerbindung(verbindungsDaten.getIpAddress(), verbindungsDaten.getPort());
		vEnGen.anmelden(key, socket);
		return vEnGen;
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

		System.out.println("Diese Software ben��tigt JDK 1.8.0 und aktueller und eine Konfigurationsdatei.\n");
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
