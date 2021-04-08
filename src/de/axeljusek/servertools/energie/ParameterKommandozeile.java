package de.axeljusek.servertools.energie;

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
