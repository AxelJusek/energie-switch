package de.axeljusek.servertools.energie;

public enum Schaltzustaende {

	Eingeschaltet(-191, 65, "0x41", "On", "txt_plug_is_on", new Boolean(true)),
	Ausgeschaltet(-126, 130, "0x82", "Off", "txt_plug_is_off", new Boolean(false)),
	Unklar(0, 0, "0xFF", "Dead", "txt_plug_is_unknown", new Boolean(false));
	
	private final Integer signedIntID;
	private final Integer unsignedIntID;
	private final String hexValue;
	private final String defaultExpression;
	private final String textKey;
	private final Boolean on;
	
	Schaltzustaende(Integer signedIntID, Integer unsignedIntID, String hexValue, String defaultExpression, String textKey, Boolean on)
	{
		this.signedIntID= signedIntID;
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
	
	public static Schaltzustaende getZustandByInt(Integer id)
	{
		Schaltzustaende gesucht = Schaltzustaende.Unklar;
		if(id.equals(Eingeschaltet.getSignedIntID()) || id.equals(Eingeschaltet.getUnsignedIntID()))
		{
			gesucht=Schaltzustaende.Eingeschaltet;
		}
		else if(id.equals(Ausgeschaltet.getSignedIntID()) || id.equals(Ausgeschaltet.getUnsignedIntID()))
		{
			gesucht=Schaltzustaende.Ausgeschaltet;
		}
		
		return gesucht;
	}
	
}
