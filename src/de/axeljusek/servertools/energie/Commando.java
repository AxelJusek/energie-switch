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
