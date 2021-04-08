/**
 * 
 */
package de.axeljusek.servertools.energenie;

/**
 * @author axel
 *
 */
public class Verbindungsdaten {

	private String ipAddress = "";
	private String portNr = "";
	private String password = "";
	
	public Verbindungsdaten()
	{		
	}
	
	public Verbindungsdaten(String ipAddress, String portNr, String password)
	{
		this.ipAddress= ipAddress;
		this.portNr = portNr;
		this.password=password;
	}
	
	public String getIpAddress()
	{
		return this.ipAddress;
	}
	
	public String getPort()
	{
		return this.portNr;
	}
	
	public String getPasswd()
	{
		return this.password;
	}
}

