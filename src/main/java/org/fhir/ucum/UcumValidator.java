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

import org.fhir.ucum.special.Registry;



public class UcumValidator {

	private UcumModel model;
	private List<String> result;
	private Registry handlers;

	public UcumValidator(UcumModel model, Registry handlers) {
		super();
		this.model = model;
		this.handlers = handlers;
	}

	public List<String> validate() {
		result = new ArrayList<String>();
		checkCodes();
		checkUnits();
		return result;
	}

	private void checkCodes() {
		for (Unit unit : model.getBaseUnits()) {
			checkUnitCode(unit.getCode(), true);
		}
		for (Unit unit : model.getDefinedUnits()) {
			checkUnitCode(unit.getCode(), true);
		}		
	}

	private void checkUnits() {
		for (DefinedUnit unit : model.getDefinedUnits()) {
			if (!unit.isSpecial())
				checkUnitCode(unit.getValue().getUnit(), false);
			else if (!handlers.exists(unit.getCode()))
				result.add("No Handler for "+unit.getCode().toString());
		}
	}

	private void checkUnitCode(String code, boolean primary) {
		try {
			Term term = new ExpressionParser(model).parse(code);
			String c = new ExpressionComposer().compose(term);
			if (!c.equals(code))
				result.add("Round trip failed: "+code+" -> "+c);
			new Converter(model, handlers).convert(term);			
		} catch (Exception e) {
			result.add(code+": "+e.getMessage());
		}
		if (primary)
			try {
				// there can't be any codes that have digits in them that aren't inside []
				boolean inBrack = false;
				boolean nonDigits = false;
				for (int i = 0; i < code.length(); i++) {
					char ch = code.charAt(i);
					if (ch == '[')
						if (inBrack)
							throw new Exception("nested [");
						else 
							inBrack = true;
					if (ch == ']')
						if (!inBrack)
							throw new Exception("] without [");
						else 
							inBrack = false;
					nonDigits = nonDigits || !(ch >= '0' && ch <= '9');
					if (ch >= '0' && ch <= '9' && !inBrack && nonDigits) {
						throw new Exception("code "+code+" is ambiguous because  it has digits outside []");
					}
				}
			} catch (Exception e) {
				result.add(e.getMessage());
			}

	}

}
