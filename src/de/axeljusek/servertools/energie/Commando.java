/**
 * 
 */
package de.axeljusek.servertools.energie;

/**
 * @author axel
 *
 */
public class Commando implements Comparable<Commando>{

	private Integer index =0;
	private ParameterKommandozeile type = null;
	private String parameter = "";
	private String followParameter = "";
	
	
	public Commando(ParameterKommandozeile type, Integer index)
	{
		this.index = index;
		this.type = type;
	}
	
	public int getIndex()
	{
		return this.index;
	}
	
	public ParameterKommandozeile getType()
	{
		return this.type;
	}
	
	public String getParameter()
	{
		return this.parameter;
	}

	public String getFollowParameter()
	{
		return this.followParameter;
	}
	
	public void setParameter(String parameter)
	{
		this.parameter = parameter;
	}
	
	public void setFollowParameter(String parameter)
	{
		this.followParameter= parameter;
	}

	/**
	 * We do not compare the object with each other, just their index. Therefore we leave the equals and hashCode as they are.
	 * TODO While we need this only to sort for the execution order, an external Comparator may be the cleaner solution here.
	 */
	@Override
	public int compareTo(Commando o) {
		int c=0;
		if(null != o)
		{
			if(this.getIndex() > o.getIndex())
			{
				c=1;
			}
			else
			{
				c=-1;
			}
		}
		else
		{
			throw new NullPointerException("To be compared object of commando is null.");
		}
		return c;
	}
	
	
	
}
