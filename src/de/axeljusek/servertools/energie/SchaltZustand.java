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
package de.axeljusek.servertools.energie;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author axel
 *
 */
public class SchaltZustand {
	
	static Logger log = LogManager.getLogger("de.axeljusek.servertools.energenie");
	
	private byte[] key;
	private byte[] task;
	private int cn = Byte.toUnsignedInt(VerbindungEnerGie.hexStringToByteArray("04")[0]); //Nicht schalten - Neutral
	private int ce = Byte.toUnsignedInt(VerbindungEnerGie.hexStringToByteArray("01")[0]); //Einschalten
	private int ca = Byte.toUnsignedInt(VerbindungEnerGie.hexStringToByteArray("02")[0]); //Ausschalten
	private VerbindungEnerGie verEngenie;

	public SchaltZustand(VerbindungEnerGie verEngenie)
	{
		this.verEngenie = verEngenie;		
		this.key= this.verEngenie.getKey();
		this.task = this.verEngenie.getTaskBuf();
	}
	
	public void schalten(int dose, boolean ein)
	{
		int cByte;
		if(ein)
		{
			cByte = ce;
		}
		else
		{
			cByte = ca;
		}
		int k1 = Byte.toUnsignedInt(key[1]);
		int k0 = Byte.toUnsignedInt(key[0]);
		int t3 = Byte.toUnsignedInt(task[3]);
		int t2 = Byte.toUnsignedInt(task[2]);	
		
		byte[] ctrlEncrypted = new byte[4];
		
		
		//ctrlcryp[i]=(((ctrl[3-i]^task[2])+task[3])^key[0])+key[1]
		for(int i=0; i<4; i++)
		{
			ctrlEncrypted[i] = encryptControlForDose(i, cn, k1, k0, t3, t2);
		}
		
		ctrlEncrypted[dose] = encryptControlForDose(dose, cByte, k1, k0, t3, t2);
		
		verEngenie.sendeLoesung(ctrlEncrypted);
	}

	private byte encryptControlForDose(int dose, int cByte, int k1, int k0, int t3, int t2) {
		VerbindungEnerGie.log.info("ctrlcryp[i]=(((ctrl[3-i]^task[2])+task[3])^key[0])+key[1]");
		VerbindungEnerGie.log.info("Mit i="+dose);
		VerbindungEnerGie.log.info("ctrlcryp["+dose+"]=((("+new Integer(cByte).toString()+"^"+new Integer(t2).toString()+")+"+new Integer(t3).toString()+")^"+new Integer(k0).toString()+")+"+new Integer(k1).toString()+"");
		
		int int1=cByte^t2;
		VerbindungEnerGie.log.info("ctrlcryp["+dose+"]=(("+new Integer(int1).toString()+"+"+new Integer(t3).toString()+")^"+new Integer(k0).toString()+")+"+new Integer(k1).toString()+"");
		
		int int2=int1 +t3;
		VerbindungEnerGie.log.info("ctrlcryp["+dose+"]=("+new Integer(int2).toString()+"^"+new Integer(k0).toString()+")+"+new Integer(k1).toString()+"");
		
		int int3=int2^k0;
		VerbindungEnerGie.log.info("ctrlcryp["+dose+"]="+new Integer(int3).toString()+"+"+new Integer(k1).toString()+"");
		
		int int4=int3+k1;
		VerbindungEnerGie.log.info("ctrlcryp["+dose+"]="+new Integer(int4).toString()+"");
		
		byte result = VerbindungEnerGie.convertIntToHexByteArray(int4)[1];
		return result;
	}
	
	/**
	 * Diese Methode geht davon aus, dass gerade jetzt der Status empfangen wurde und 
	 * noch in der Variablen dataEmpfVerschl steht, ebenso muss eine Verbindung bestehen und in der Variablen
	 * taskBuf die Task für diese Verbindung enthalten ist.
	 * @return
	 */
	public String getSchaltZustand(int dose) {
		
		int sc0 = Byte.toUnsignedInt(this.verEngenie.getEncryptedStatus()[dose]);
		int k1 = Byte.toUnsignedInt(key[1]);
		int k0 = Byte.toUnsignedInt(key[0]);
		int t3 = Byte.toUnsignedInt(task[3]);
		int t2 = Byte.toUnsignedInt(task[2]);		
		
		//stat[3-i]=(((statcryp[i]-key[1])^key[0])-task[3])^task[2]
		
		int int4 = berechneStatusWert(dose, sc0, k1, k0, t3, t2);		
		
		byte[] result = VerbindungEnerGie.convertIntToHexByteArray(int4);
				
		Schaltzustaende schaltZustand = Schaltzustaende.getZustandByInt(int4);
		log.info("Schaltzustand:"+ result[0] + ":" + result[1]);
		return schaltZustand.getDefaultExpression();
	}

	protected int berechneStatusWert(int dose, int sc0, int k1, int k0, int t3, int t2) {
		log.info("stat[3-i]=(((statcryp[i]-key[1])^key[0])-task[3])^task[2]");
		log.info("Mit i="+dose);
		log.info("stat[3-"+dose+"]=((("+new Integer(sc0).toString()+"-"+new Integer(k1).toString()+")^"+new Integer(k0).toString()+")-"+new Integer(t3).toString()+")^"+new Integer(t2).toString()+"");
		
		int int1= sc0 - k1;
		log.info("stat[3-i]=(("+new Integer(int1).toString()+"^"+new Integer(k0).toString()+")-"+new Integer(t3).toString()+")^"+new Integer(t2).toString()+"");
		
		int int2 = int1^k0;
		log.info("stat[3-i]=("+new Integer(int2).toString()+"-"+new Integer(t3).toString()+")^"+new Integer(t2).toString()+"");
		
		int int3 = int2-t3;
		log.info("stat[3-i]="+new Integer(int3).toString()+"^"+new Integer(t2).toString()+"");
		
		int int4 = int3^t2;
		log.info("stat[3-i]="+new Integer(int4).toString()+"");
		return int4;
	}

	public String getSchaltZustandAlle() {
		String zustaendeAlsString = "";
		for(int i=0; i<4; i++)
		{
			zustaendeAlsString = zustaendeAlsString + " Die Dose "+i+" ist im Zustand " + getSchaltZustand(i) +"\n";
		}
		return zustaendeAlsString;
	}
	
}
