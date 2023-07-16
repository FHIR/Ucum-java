
/*******************************************************************************
BSD 3-Clause License

Copyright (c) 2006+, Health Intersections Pty Ltd
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of the copyright holder nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 *******************************************************************************/

package org.fhir.ucum;

import java.util.ArrayList;
import java.util.List;

public class Canonical {

	public static class CanonicalUnit {
    private BaseUnit base;
    private int exponent;
		protected CanonicalUnit(BaseUnit base, int exponent) {
	    super();
	    this.base = base;
	    this.exponent = exponent;
    }
		public BaseUnit getBase() {
			return base;
		}
		public int getExponent() {
			return exponent;
		}
		public void setExponent(int exponent) {
			this.exponent = exponent;
		}
    
    
  }

	private Decimal value;
	private List<CanonicalUnit> units = new ArrayList<CanonicalUnit>();
	
	/**
	 * @param value
	 * @param unit
	 */
	public Canonical(Decimal value) {
		super();
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public Decimal getValue() {
		return value;
	}

	/**
	 * @return the unit
	 */
	public List<CanonicalUnit> getUnits() {
		return units;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Decimal value) {
		this.value = value;
	}

	public void multiplyValue(Decimal multiplicand) {
		value = value.multiply(multiplicand);		
	}

	public void multiplyValue(int multiplicand) {
		value = value.multiply(new Decimal(multiplicand));		
	}

	
	public void divideValue(Decimal divisor) throws UcumException  {
		value = value.divide(divisor);		
	}
	
	public void divideValue(int divisor) throws UcumException  {
		value = value.divide(new Decimal(divisor));		
	}

	
}
