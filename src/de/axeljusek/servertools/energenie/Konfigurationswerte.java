package de.axeljusek.servertools.energenie;

public enum Konfigurationswerte {
	port("port", "Number", "5", "5000"),
	ip_address("ip_address", "String", "15", "192.168.0.254"),
	password("password", "String", "8", "       1");
	
	private final String name;
	private final String dataType;
	private final String length;
	private final String defaultValue;
	
	Konfigurationswerte(String name, String dataType, String length, String defaultValue)
	{
		this.name=name;
		this.dataType=dataType;
		this.length=length;
		this.defaultValue=defaultValue;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getDataType()
	{
		return this.dataType;
	}
	
	public String getLength()
	{
		return this.length;
	}
	
	public String getDefaultValue()
	{
		return this.defaultValue;
	}
}
