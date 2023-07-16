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

public class Concept {

	private ConceptKind kind;
	/**
	 * case sensitive code for this concept
	 */
	private String code;
	
	/**
	 * case insensitive code for this concept
	 */
	private String codeUC;
	
	/**
	 * print symbol for this code 
	 */
	private String printSymbol;
	
	/**
	 * names for the concept
	 */
	private List<String> names = new ArrayList<String>();
	
	
	/**
	 * @param code
	 * @param codeUC
	 */
	public Concept(ConceptKind kind, String code, String codeUC) {
		super();
		this.kind = kind;
		this.code = code;
		this.codeUC = codeUC;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the codeUC
	 */
	public String getCodeUC() {
		return codeUC;
	}

	/**
	 * @param codeUC the codeUC to set
	 */
	public void setCodeUC(String codeUC) {
		this.codeUC = codeUC;
	}

	/**
	 * @return the printSymbol
	 */
	public String getPrintSymbol() {
		return printSymbol;
	}

	/**
	 * @param printSymbol the printSymbol to set
	 */
	public void setPrintSymbol(String printSymbol) {
		this.printSymbol = printSymbol;
	}

	/**
	 * @return the name
	 */
	public List<String> getNames() {
		return names;
	}

	/**
	 * @return the kind
	 */
	public ConceptKind getKind() {
		return kind;
	}

	public String getDescription() {
		return  kind.toString().toLowerCase()+" "+code+" ('"+names.get(0)+"')";
	}
	
	@Override
	public String toString() {
		return this.getCode() + " = " + getDescription();
	}
}
