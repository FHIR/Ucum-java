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


public class DefinedUnit extends Unit{

	/**
	 * whether this is a metric unit or not
	 */
	private boolean metric;
	
	/**
	 * special means?
	 */
	private boolean isSpecial;
	
	/**
	 * The class of this unit
	 */
	private String class_;
	
	/**
	 * Value details
	 */
	private Value value;
	
	


	/**
	 * @param code
	 * @param codeUC
	 */
	public DefinedUnit(String code, String codeUC) {
		super(ConceptKind.UNIT, code, codeUC);
	}


	/**
	 * @return the metric
	 */
	public boolean isMetric() {
		return metric;
	}


	/**
	 * @param metric the metric to set
	 */
	public void setMetric(boolean metric) {
		this.metric = metric;
	}


	/**
	 * @return the isSpecial
	 */
	public boolean isSpecial() {
		return isSpecial;
	}


	/**
	 * @param isSpecial the isSpecial to set
	 */
	public void setSpecial(boolean isSpecial) {
		this.isSpecial = isSpecial;
	}


	/**
	 * @return the class_
	 */
	public String getClass_() {
		return class_;
	}


	/**
	 * @param class_ the class_ to set
	 */
	public void setClass_(String class_) {
		this.class_ = class_;
	}


	/**
	 * @return the value
	 */
	public Value getValue() {
		return value;
	}


	/**
	 * @param value the value to set
	 */
	public void setValue(Value value) {
		this.value = value;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.ohf.ucum.model.BaseUnit#getDescription()
	 */
	@Override
	public String getDescription() {
		return super.getDescription()+" = "+value.getDescription();
	}


		
	
}
