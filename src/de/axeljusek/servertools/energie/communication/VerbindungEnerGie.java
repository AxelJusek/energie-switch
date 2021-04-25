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
package de.axeljusek.servertools.energie.communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author axel
 *
 */

public class VerbindungEnerGie implements ConnectionEnergie{

	static Logger log = LogManager.getLogger("de.axeljusek.servertools.energenie");
	private Socket socket;
	private InputStream inStream;
	private OutputStream outStream;
	private byte startByte = 0x11;
	private byte[] taskBuf = new byte[4];
	private byte[] resBuf = new byte[4];
	private byte[] key = hexStringToByteArray("2020202020202031");
	private byte[] dataEmpfVerschl; // = new byte[4];
	private byte[] encryptedStatus;
	private int warteMilliSekunden = 800;

	public VerbindungEnerGie() {
		//Default Constructor for Dependency Injection.
	}

	public byte[] getTaskBuf() {
		return this.taskBuf;
	}

	public byte[] getResBuf() {
		return this.resBuf;
	}

	public byte[] getKey() {
		return this.key;
	}

	public byte[] getEncryptedStatus() {
		return this.encryptedStatus;
	}

	public boolean anmelden(byte[] key, Socket tcpSocket) throws IOException {
		boolean angemeldet = false;

		if (this.socket.isConnected()) {
			// Senden des Start-Bytes
			sendeStart();

			// Empfangen Task[4] mit 4 Byte
			boolean taskEmpfangen = empfangeTask();

			if (taskEmpfangen) {
				// Berechnung der Lösung
				this.resBuf = getSolutionForRequest(key, this.taskBuf);
				log.info("Es wurde die folgende Antwort errechnet:" + byteArraytoString(this.resBuf));

				// Senden der Lösung
				sendeLoesung(this.resBuf);

				// für 4 sek. warten auf den Empfang des verschlüsselten Status
				angemeldet = empfangeStatus();
			}
		} else {
			log.error("Vor dem Anmelden muss die TCP-Verbindung stehen.");
		}
		return angemeldet;
	}

	private boolean empfangeStatus() throws IOException {
		boolean empfangen = false;

		empfangen = receiveFourBytes();
		this.encryptedStatus = this.dataEmpfVerschl;

		if (!empfangen) {
			log.error("Das Empfangen der Statusmeldung schlug fehl.");
			throw new IOException("Verbindungsaufbau war nicht möglich. Keinen Status empfangen.");
		} else {
			log.info("Es wurde die Statusmeldung: >" + byteArraytoString(this.encryptedStatus) + "< empfangen.");
			SchaltZustand sz = new SchaltZustand(this);
			log.info("Der Status der Dose 0 ist:" + sz.getSchaltZustand(0));
			log.info("Der Status der Dose 1 ist:" + sz.getSchaltZustand(1));
			log.info("Der Status der Dose 2 ist:" + sz.getSchaltZustand(2));
			log.info("Der Status der Dose 3 ist:" + sz.getSchaltZustand(3));
		}
		return empfangen;
	}

	protected void sendeLoesung(byte[] res) {
		try {
			getOutStream().write(res);
			getOutStream().flush();
		} catch (IOException e) {
			log.error("Senden des Handshakes schlug fehl.", e);
			e.printStackTrace();
		}
	}

	protected void sendeStart() {
		try {
			getOutStream().write(new byte[] { this.startByte });
			getOutStream().flush();
		} catch (IOException e1) {
			log.error("Konnte Startzeichen nicht senden.", e1);
			e1.printStackTrace();
		}
	}

	private boolean empfangeTask() {
		boolean taskEmpfangen = receiveFourBytes();
		if (taskEmpfangen) {
			this.taskBuf[0] = (this.dataEmpfVerschl[0]);
			this.taskBuf[1] = (this.dataEmpfVerschl[1]);
			this.taskBuf[2] = (this.dataEmpfVerschl[2]);
			this.taskBuf[3] = (this.dataEmpfVerschl[3]);
		} else {
			log.error("Die Task zur Authentifizierung konnte nicht empfangen werden.");
		}
		return taskEmpfangen;
	}

	/**
	 * Sondermethode speziell fuer das Debugging der Kommunikation
	 * 
	 * @param arrayOfByte
	 *            Bytes auf der Schnittstelle
	 * @return menschenlesbare Form zum Vergleich mit Wireshark
	 */
	protected String byteArraytoString(byte[] arrayOfByte) {
		int valueInt = 0;
		String data = "";
		for (byte element : arrayOfByte) {
			valueInt = Byte.toUnsignedInt(element);
			if (16 > valueInt) {
				data = data + "0";
			}
			data = data + "" + Integer.toUnsignedString(valueInt, 16);
		}
		data = data + "";
		return data;
	}

	private boolean receiveFourBytes() {
		long startDate = new Date().getTime();
		long endDate = startDate + this.warteMilliSekunden;
		Date aktuellesDate = null;
		this.dataEmpfVerschl = new byte[4];
		boolean istEmpfangen = false;

		do {
			try {
				while (0 < getInStream().available()) {
					getInStream().read(this.dataEmpfVerschl);
					istEmpfangen = true;
				}
			} catch (IOException e) {
				log.error("Lesen scheiterte.", e);
				e.printStackTrace();
			}

			try {
				synchronized (this) {
					this.wait(100);
				}
			} catch (InterruptedException e) {
				log.warn("Warten beim Empfang unterbrochen.", e);
				e.printStackTrace();
			}
			aktuellesDate = new Date();
		} while ((aktuellesDate.getTime() <= endDate) && !istEmpfangen);

		return istEmpfangen;
	}

	public byte[] getSolutionForRequest(byte[] key, byte[] task) {
		byte[] res = new byte[4];

		// res[1:0]=((task[0]^key[2])*key[0])^(key[6]|(key[4]<<8))^task[2]
		byte[] int10 = getResponseForChallenge(task[0], key[2], key[0], key[4], key[6], task[2]);

		// res[3:2]=((task[1]^key[3])*key[1])^(key[7]|(key[5]<<8))^task[3]
		byte[] int32 = getResponseForChallenge(task[1], key[3], key[1], key[5], key[7], task[3]);

		res[0] = int10[1];
		res[1] = int10[0];
		res[2] = int32[1];
		res[3] = int32[0];

		return res;
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	private byte[] getResponseForChallenge(byte task0, byte key2, byte key0, byte key4, byte key6, byte task2) {
		int t0 = Byte.toUnsignedInt(task0);
		int k2 = Byte.toUnsignedInt(key2);
		int k0 = Byte.toUnsignedInt(key0);
		int k4 = Byte.toUnsignedInt(key4);
		int k6 = Byte.toUnsignedInt(key6);
		int t2 = Byte.toUnsignedInt(task2);

		log.info("res[1:0]=((task[0]^key[2])*key[0]) ^ (key[6] | (key[4]<<8)) ^ task[2]");
		log.info("res[1:0]=((" +  Integer.valueOf(t0).toString() + "^" +  Integer.valueOf(k2).toString() + ")*"
				+  Integer.valueOf(k0).toString() + ")^(" +  Integer.valueOf(k6).toString() + " | (" +  Integer.valueOf(k4).toString()
				+ "<<8))^" +  Integer.valueOf(t2).toString() + "");

		int int1 = t0 ^ k2;
		log.info("res[1:0]=(" +  Integer.valueOf(int1).toString() + "*" +  Integer.valueOf(k0).toString() + ")^("
				+  Integer.valueOf(k6).toString() + " | (" +  Integer.valueOf(k4).toString() + "<<8))^"
				+  Integer.valueOf(t2).toString() + "");

		int int2 = int1 * k0;
		log.info("res[1:0]=" +  Integer.valueOf(int2).toString() + "^(" +  Integer.valueOf(k6).toString() + " | ("
				+  Integer.valueOf(k4).toString() + "<<8))^" +  Integer.valueOf(t2).toString() + "");

		int int3 = k4 << 8;
		log.info("res[1:0]=" +  Integer.valueOf(int2).toString() + "^(" +  Integer.valueOf(k6).toString() + " | "
				+  Integer.valueOf(int3).toString() + ")^" +  Integer.valueOf(t2).toString() + "");

		int int4 = k6 | int3;
		log.info("res[1:0]=" +  Integer.valueOf(int2).toString() + "^" +  Integer.valueOf(int4).toString() + "^"
				+  Integer.valueOf(t2).toString() + "");

		int int5 = int2 ^ int4;
		log.info("res[1:0]=" +  Integer.valueOf(int5).toString() + "^" +  Integer.valueOf(t2).toString() + "");

		int int6 = int5 ^ t2; // res[1:0]
		log.info("res[1:0]=" +  Integer.valueOf(int6).toString() + "");
		String hexString = Integer.toHexString(int6);
		log.info("res[1:0]=" + hexString);

		byte[] result = convertIntToHexByteArray(int6);

		log.info("res[1:0]=" + result[1] + ":" + result[0] + " -- Ende des Halbenschluessels.\n");

		return result;
	}

	protected static byte[] convertIntToHexByteArray(int input) {
		byte[] hexByteArray = new byte[2];
		hexByteArray[1] = (byte) ((byte) input & 0xff);
		input >>= 8;
		hexByteArray[0] = (byte) input;
		return hexByteArray;
	}

	public byte[] putPasswordInArray(String password) {
		this.key = new byte[8];
		if (8 >= password.length()) {
			while (8 > password.length()) {
				password = " " + password;
			}
			this.key = password.getBytes();

			// Der Schluessel muss gedreht werden.
			byte[] inversKey = new byte[8];
			for (int i = 0; i < 8; i++) {
				inversKey[i] = this.key[7 - i];
			}
			this.key = inversKey;
		} else {
			log.error("Der Schlüssel ist länger als 8 Zeichen. Es sind nur 8 Zeichen erlaubt.");
			throw new IllegalArgumentException("Laenge des Passwortes ueberschreitet die Hoechstlaenge von 8 Zeichen.");
		}
		return this.key;
	}

	public Socket neueTCPVerbindung(String ip_address, String port) {
		Integer portNr = Integer.parseInt(port);
		try {
			this.socket = new Socket(ip_address, portNr);
		} catch (IOException e) {
			log.error("Die Verbindung konnte nicht aufgebaut werden. TCP-Fehler");
			e.printStackTrace();
		}

		return this.socket;
	}

	private InputStream getInStream() {
		if ((null == this.socket) || this.socket.isClosed()) {
			log.error("Socket muss vor dem Anlegen des Inputstreams verbunden sein.");
		} else {
			if (null == this.inStream) {
				try {
					this.inStream = this.socket.getInputStream();
				} catch (IOException e) {
					log.error("Lesender Stream konnte nicht erstellt werden.", e);
					e.printStackTrace();
				}
			}
		}
		return this.inStream;
	}

	private OutputStream getOutStream() {
		if ((null == this.socket) || this.socket.isClosed()) {
			log.error("Socket muss vor dem Anlegen des Inputstreams verbunden sein.");
		} else {
			if (null == this.outStream) {
				try {
					this.outStream = this.socket.getOutputStream();
				} catch (IOException e) {
					log.error("Schreibender Stream konnte nicht erstellt werden", e);
					e.printStackTrace();
				}
			}
		}
		return this.outStream;
	}

	public void schalteDose(int dose, boolean ein) {
		SchaltZustand sz = new SchaltZustand(this);
		sz.schalten(dose, ein);
	}

	public void verbindungTrennen() {
		if (null != this.outStream) {
			try {
				this.outStream.close();
				this.outStream = null;
			} catch (IOException e) {
				log.error("Schließen des schreibenden Stream verursachte Fehler.", e);
				e.printStackTrace();
			}
		}

		if (null != this.inStream) {
			try {
				this.inStream.close();
				this.inStream = null;
			} catch (IOException e) {
				log.error("Schließen des lesenden Stream verursachte Fehler.", e);
				e.printStackTrace();
			}
		}

		if (null != this.socket) {
			if (this.socket.isConnected() && !this.socket.isClosed()) {
				try {
					this.socket.shutdownOutput();
					this.socket.shutdownInput();
				} catch (IOException e) {
					log.error("Beenden der Kommunikation verursachte eine Fehler.", e);
					e.printStackTrace();
				}
			}
			try {
				this.socket.close();
			} catch (IOException e) {
				log.error("Schließen der Verbindung verursachte einen Fehler.", e);
				e.printStackTrace();
			}
		}

		// Damit das Endgeraet Energenie auch sicher im Grundzustand ist, muss 4
		// sec. lang keine Kommunikation erfolgen.
		try {
			synchronized (this) {
				this.wait(4500);
			}
		} catch (InterruptedException e) {
			log.warn("Warten beim Trennen unterbrochen.", e);
			e.printStackTrace();
		}
	}
	
	public void zustandAbfragen(Verbindungsdaten verbindungsDaten, String doseNr) throws IOException {
		VerbindungEnerGie vEnGen = verbindungAufbauen(verbindungsDaten);
		Integer dose = Integer.parseInt(doseNr);
		SchaltZustand sz = new SchaltZustand(vEnGen);

		System.out.println(sz.getSchaltZustand(dose));

		vEnGen.verbindungTrennen();
	}

	

	public void schaltzustaendeAusgeben(Verbindungsdaten verbindungsDaten) throws IOException {
		VerbindungEnerGie vEnGen = verbindungAufbauen(verbindungsDaten);

		SchaltZustand sz = new SchaltZustand(vEnGen);

		System.out.println("Verbindungsdaten zur Energenie LAN Steckdose:");
		System.out.println(sz.getSchaltZustandAlle());

		vEnGen.verbindungTrennen();
	}

	public void doseSchalten(Verbindungsdaten verbindungsDaten, String doseNr, String einschalten) throws IOException {
		VerbindungEnerGie vEnGen = verbindungAufbauen(verbindungsDaten);

		SchaltZustand sz = new SchaltZustand(vEnGen);
		boolean ein = Boolean.parseBoolean(einschalten);
		int dose = Integer.parseInt(doseNr);
		sz.schalten(dose, ein);

		vEnGen.verbindungTrennen();
	}

	private static VerbindungEnerGie verbindungAufbauen(Verbindungsdaten verbindungsDaten) throws IOException {

		VerbindungEnerGie vEnGen = new VerbindungEnerGie();
		byte[] key = vEnGen.putPasswordInArray(verbindungsDaten.getPasswd());

		log.info("Es werden die folgenden Parameter verwendet: \n" + "IP-Adresse '" + verbindungsDaten.getIpAddress()
				+ "'\n" + "Port '" + verbindungsDaten.getPort() + "'\n" + "Passwort '" + verbindungsDaten.getPasswd()
				+ "'\n" + "Key '" + key.length + "'\n");

		Socket socket = vEnGen.neueTCPVerbindung(verbindungsDaten.getIpAddress(), verbindungsDaten.getPort());
		vEnGen.anmelden(key, socket);
		return vEnGen;
	}

}
