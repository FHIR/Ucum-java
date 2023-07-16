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

public class FormalStructureComposer {

	public String compose(Term term) {
		StringBuilder bldr = new StringBuilder();
		composeTerm(bldr, term);
		return bldr.toString();
	}

	private void composeTerm(StringBuilder bldr, Term term) {
		if (term.getComp() != null)
			composeComp(bldr, term.getComp());
		if (term.getOp() != null)
			composeOp(bldr, term.getOp());
		if (term.getTerm() != null) { 
//			bldr.append('(');
			composeTerm(bldr, term.getTerm());		
//			bldr.append('}');
		}
	}

	private void composeComp(StringBuilder bldr, Component comp) {
		if (comp instanceof Factor)
			composeFactor(bldr, (Factor)comp);
		else if (comp instanceof Symbol)
			composeSymbol(bldr, (Symbol)comp);
		else if (comp instanceof Term)
			composeTerm(bldr, (Term)comp);
		else
			bldr.append('?');
	}

	private void composeSymbol(StringBuilder bldr, Symbol symbol) {
		bldr.append('(');
		if (symbol.getPrefix() != null) { 
			bldr.append(symbol.getPrefix().getNames().get(0));
		}
		bldr.append(symbol.getUnit().getNames().get(0));
		if (symbol.getExponent() != 1) { 
			bldr.append(" ^ ");
			bldr.append(symbol.getExponent());
		}
		bldr.append(')');
	}

	private void composeFactor(StringBuilder bldr, Factor comp) {
	   bldr.append(comp.getValue());		
	}

	private void composeOp(StringBuilder bldr, Operator op) {
		if (op == Operator.DIVISION)
			bldr.append(" / ");
		else
			bldr.append(" * ");
	}

}
