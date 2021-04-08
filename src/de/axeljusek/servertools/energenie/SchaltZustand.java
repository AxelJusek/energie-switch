/**
 * 
 */
package de.axeljusek.servertools.energenie;


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
	private int cn = Byte.toUnsignedInt(VerbindungEnerGenie.hexStringToByteArray("04")[0]); //Nicht schalten - Neutral
	private int ce = Byte.toUnsignedInt(VerbindungEnerGenie.hexStringToByteArray("01")[0]); //Einschalten
	private int ca = Byte.toUnsignedInt(VerbindungEnerGenie.hexStringToByteArray("02")[0]); //Ausschalten
	private VerbindungEnerGenie verEngenie;

	public SchaltZustand(VerbindungEnerGenie verEngenie)
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
		VerbindungEnerGenie.log.info("ctrlcryp[i]=(((ctrl[3-i]^task[2])+task[3])^key[0])+key[1]");
		VerbindungEnerGenie.log.info("Mit i="+dose);
		VerbindungEnerGenie.log.info("ctrlcryp["+dose+"]=((("+new Integer(cByte).toString()+"^"+new Integer(t2).toString()+")+"+new Integer(t3).toString()+")^"+new Integer(k0).toString()+")+"+new Integer(k1).toString()+"");
		
		int int1=cByte^t2;
		VerbindungEnerGenie.log.info("ctrlcryp["+dose+"]=(("+new Integer(int1).toString()+"+"+new Integer(t3).toString()+")^"+new Integer(k0).toString()+")+"+new Integer(k1).toString()+"");
		
		int int2=int1 +t3;
		VerbindungEnerGenie.log.info("ctrlcryp["+dose+"]=("+new Integer(int2).toString()+"^"+new Integer(k0).toString()+")+"+new Integer(k1).toString()+"");
		
		int int3=int2^k0;
		VerbindungEnerGenie.log.info("ctrlcryp["+dose+"]="+new Integer(int3).toString()+"+"+new Integer(k1).toString()+"");
		
		int int4=int3+k1;
		VerbindungEnerGenie.log.info("ctrlcryp["+dose+"]="+new Integer(int4).toString()+"");
		
		byte result = VerbindungEnerGenie.convertIntToHexByteArray(int4)[1];
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
		
		byte[] result = VerbindungEnerGenie.convertIntToHexByteArray(int4);
				
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
