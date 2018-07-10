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

import java.util.Objects;

public class Pair {

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Objects.equals(value, pair.value) &&
            Objects.equals(code, pair.code);
    }

    @Override
    public int hashCode() {

        return Objects.hash(value, code);
    }
}
