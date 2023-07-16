/*******************************************************************************
 * Crown Copyright (c) 2006 - 2014, Copyright (c) 2006 - 2014 Kestral Computing P/L.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Kestral Computing P/L - initial implementation
 *******************************************************************************/

package org.fhir.ucum;

import java.io.Serializable;

public class Pair implements Serializable {

    private static final long serialVersionUID = -1201864084549128274L;
    
    private Decimal value;
	private String code;
	
	
	/**
	 * @param value
	 * @param code
	 */
	public Pair(Decimal value, String code) {
		super();
		this.value = value;
		this.code = code;
	}
	/**
	 * @return the value
	 */
	public Decimal getValue() {
		return value;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
  @Override
  public boolean equals(Object other) {
    if (other instanceof Pair) {
      Pair p = (Pair) other;
      return value.equals(p.value) && code.equals(p.code);
    } else
      return super.equals(other);
  }
  
  @Override
  public int hashCode() {
    return toString().hashCode();
  }
  @Override
  public String toString() {
    return value.toString()+" "+code;
  }
	
	
}
