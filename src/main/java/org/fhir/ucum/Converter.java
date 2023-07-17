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

import java.util.Collections;
import java.util.Comparator;

import org.fhir.ucum.Canonical.CanonicalUnit;
import org.fhir.ucum.special.Registry;

public class Converter {

	private UcumModel model;
	private Registry handlers;
	
	/**
	 * @param model
	 */
	public Converter(UcumModel model, Registry handlers) {
		super();
		this.model = model;
		this.handlers = handlers;
	}

	
	public Canonical convert(Term term) throws UcumException  {
		return normalise("  ", term);
	}
	
	private Canonical normalise(String indent, Term term) throws UcumException  {
		Canonical result = new Canonical(new Decimal("1.000000000000000000000000000000"));
		
		debug(indent, "canonicalise", term);
    boolean div = false;
		Term t = term;
		while (t != null) {
    	if (t.getComp() instanceof Term) {
    		Canonical temp = normalise(indent+"  ", (Term) t.getComp());
    		if (div) {
    			result.divideValue(temp.getValue());
    			for (CanonicalUnit c : temp.getUnits()) 
    				c.setExponent(0-c.getExponent());
    		} else {
    			result.multiplyValue(temp.getValue());
    		}
    		result.getUnits().addAll(temp.getUnits());
    	} else if (t.getComp() instanceof Factor) {
    		if (div)
    			result.divideValue(((Factor) t.getComp()).getValue());
    		else
    			result.multiplyValue(((Factor) t.getComp()).getValue());
    	} else if (t.getComp() instanceof Symbol) {
    		Symbol o = (Symbol) t.getComp();
  			Canonical temp = normalise(indent, o);
    		if (div) {
    			result.divideValue(temp.getValue());
    			for (CanonicalUnit c : temp.getUnits()) 
    				c.setExponent(0-c.getExponent());
    		} else {
    			result.multiplyValue(temp.getValue());
    		}
    		result.getUnits().addAll(temp.getUnits());
    	}
			div = t.getOp() == Operator.DIVISION;
			t = t.getTerm();
		}

		debug(indent, "collate", result);

		for (int i = result.getUnits().size()-1; i >= 0; i--) {
			CanonicalUnit sf = result.getUnits().get(i);
			for (int j = i-1; j >=0; j--) {
				CanonicalUnit st = result.getUnits().get(j);
				if (st.getBase() == sf.getBase()) {
					st.setExponent(sf.getExponent()+st.getExponent());
					result.getUnits().remove(i);
					break;
				}
			}
		}
		for (int i = result.getUnits().size()-1; i >= 0; i--) {
			CanonicalUnit sf = result.getUnits().get(i);
			if (sf.getExponent() == 0)
				result.getUnits().remove(i);
		}
		
		debug(indent, "sort", result);
		Collections.sort(result.getUnits(), new Comparator<CanonicalUnit>(){
		   @Override
		   public int compare(final CanonicalUnit lhs, CanonicalUnit rhs) {
		  	 return lhs.getBase().getCode().compareTo(rhs.getBase().getCode());
		     }
		 });
		debug(indent, "done", result);
		return result;
	}

  private Canonical normalise(String indent, Symbol sym) throws UcumException  {
    Canonical result = new Canonical(new Decimal("1.000000000000000000000000000000"));
  	
  	if (sym.getUnit() instanceof BaseUnit) {
  		result.getUnits().add(new CanonicalUnit((BaseUnit) sym.getUnit(), sym.getExponent()));
  	} else {
			Canonical can = expandDefinedUnit(indent, (DefinedUnit) sym.getUnit());
			for (CanonicalUnit c : can.getUnits()) {
				c.setExponent(c.getExponent() *  sym.getExponent());
			}
			result.getUnits().addAll(can.getUnits());
			if (sym.getExponent() > 0) 
				for (int i = 0; i < sym.getExponent(); i++)
					result.multiplyValue(can.getValue());
			else
				for (int i = 0; i > sym.getExponent(); i--)
					result.divideValue(can.getValue());
		} 
		if (sym.getPrefix() != null) {
			if (sym.getExponent() > 0) 
				for (int i = 0; i < sym.getExponent(); i++)
					result.multiplyValue(sym.getPrefix().getValue());
			else
				for (int i = 0; i > sym.getExponent(); i--)
					result.divideValue(sym.getPrefix().getValue());
		}
		return result;
  }

	private Canonical expandDefinedUnit(String indent, DefinedUnit unit) throws UcumException  {
		String u = unit.getValue().getUnit();
		Decimal v = unit.getValue().getValue();

		if (unit.isSpecial()) {
			if (!handlers.exists(unit.getCode())) 
				throw new UcumException("Not handled yet (special unit)");
			else {
				u = handlers.get(unit.getCode()).getUnits();
				v = handlers.get(unit.getCode()).getValue();
				if (handlers.get(unit.getCode()).hasOffset()) {
				  // the problem here is that supporting this requires a total rework of the architecture, because the actual value isn't available here (and don't know whether it's needed to do offset either)
				  throw new UcumException("Not handled yet (special unit with offset from 0 at intersect)");
				}
			}
		}
			
		Term t = new ExpressionParser(model).parse(u);
		debug(indent, "now handle", t);
		Canonical result = normalise(indent+"  ", t);
		result.multiplyValue(v);
		return result;
  }


	private void debug(String indent, String state, Term unit) {
//		System.out.println(indent+state+": "+new ExpressionComposer().compose(unit));
	}


	private void debug(String indent, String state, Canonical can) {
//		 System.out.println(indent+state+": "+new ExpressionComposer().compose(can));
	}



}
