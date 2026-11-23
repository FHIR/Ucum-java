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

public class ExpressionParser {

	/**
	 * Limits on the complexity of a unit expression. Converting a unit to canonical form
	 * multiplies exact decimal values together once per unit of exponent, so without these
	 * a short hostile unit string (e.g. "km2000000000" or a long chain of "Ym24") can 
	 * consume unbounded CPU. The limits are well beyond anything used in practice 
	 * (the largest real exponents are things like 10*23 or 10*-24). 
	 */
	public static final int MAX_EXPONENT = 30; // absolute value of any single exponent
	public static final int MAX_TOTAL_EXPONENT = 60; // sum of absolute values of all exponents in the expression
	public static final int MAX_COMPONENTS = 20; // number of symbols and numbers in the expression
	public static final int MAX_NESTING = 10; // depth of nested brackets
	public static final int MAX_TERMS = 50; // number of terms (including annotations and stray '/'s) - each is a level of recursion

	private UcumModel model;
	private int componentCount;
	private int totalExponent;
	private int nesting;
	private int termCount;
	
	/**
	 * @param model
	 */
	public ExpressionParser(UcumModel model) {
		super();
		this.model = model;
	}

	public Term parse(String code) throws UcumException  {
		componentCount = 0;
		totalExponent = 0;
		nesting = 0;
		termCount = 0;
		Lexer lexer = new Lexer(code);
		Term res = parseTerm(lexer, true);
		if (!lexer.finished())
			throw new UcumException("Expression was not parsed completely. Syntax Error?");
		return res;
	}
	
	private Term parseTerm(Lexer lexer, boolean first) throws UcumException  {
		termCount++;
		if (termCount > MAX_TERMS)
			lexer.error("The unit expression is too complex (limit is "+MAX_TERMS+" terms)");
		Term res = new Term();
		if (first && lexer.getType() == TokenType.NONE) {
			res.setComp(new Factor(1));
		} else if (lexer.getType() == TokenType.SOLIDUS) {
			res.setOp(Operator.DIVISION);
			lexer.consume();
			res.setTerm(parseTerm(lexer, false));
		} else {
		  if (lexer.getType() == TokenType.ANNOTATION) {
         res.setComp(new Factor(1)); // still lose the annotation
         lexer.consume();
		} else
      	 res.setComp(parseComp(lexer));
			if (lexer.getType() != TokenType.NONE && lexer.getType() != TokenType.CLOSE) {
				if (lexer.getType() == TokenType.SOLIDUS) {
					res.setOp(Operator.DIVISION);
					lexer.consume();
				} else if (lexer.getType() == TokenType.PERIOD) {
					res.setOp(Operator.MULTIPLICATION);
					lexer.consume();
				} else if (lexer.getType() == TokenType.ANNOTATION)
					res.setOp(Operator.MULTIPLICATION); // implicit
				else
					lexer.error("Expected '/' or '.'");
				res.setTerm(parseTerm(lexer, false));
			}
		} 
		return res;
	}

	private Component parseComp(Lexer lexer) throws UcumException  {
		if (lexer.getType() == TokenType.NUMBER || lexer.getType() == TokenType.SYMBOL) {
			componentCount++;
			if (componentCount > MAX_COMPONENTS)
				lexer.error("Too many components in the unit expression (limit is "+MAX_COMPONENTS+")");
		}
		if (lexer.getType() == TokenType.NUMBER) { 
			Factor fact = new Factor(parseInt(lexer));
			lexer.consume();
			return fact;
		} else if (lexer.getType() == TokenType.SYMBOL)
			return parseSymbol(lexer);
		else if  (lexer.getType() == TokenType.NONE)
			lexer.error("unexpected end of expression looking for a symbol or a number");
		else if (lexer.getType() == TokenType.OPEN) {
      nesting++;
      if (nesting > MAX_NESTING)
        lexer.error("Brackets are nested too deeply (limit is "+MAX_NESTING+")");
      lexer.consume();
      Term res = parseTerm(lexer, true);
      nesting--;
      if (lexer.getType() == TokenType.CLOSE) 
        lexer.consume();
      else
        lexer.error("Unexpected Token Type '"+lexer.getType().toString()+"' looking for a close bracket");
      return res;
		} else 
			lexer.error("unexpected token looking for a symbol or a number");
		return null; // we never get to here
	}

	private Component parseSymbol(Lexer lexer) throws UcumException  {
		Symbol symbol = new Symbol(); 
		String sym = lexer.getToken();
		
		// now, can we pick a prefix that leaves behind a metric unit?
		Prefix selected = null;
		Unit unit = null;
		for (Prefix prefix : model.getPrefixes()) {
			if (sym.startsWith(prefix.getCode())) {
				unit = model.getUnit(sym.substring(prefix.getCode().length()));
				if (unit != null && (unit.getKind() == ConceptKind.BASEUNIT || ((DefinedUnit) unit).isMetric())) {
					selected = prefix;
					break;
				};				
			}
		}

		if (selected != null) {
			symbol.setPrefix(selected);
			symbol.setUnit(unit);
		} else {
			unit = model.getUnit(sym);
			if (unit != null) 
				symbol.setUnit(unit);
			else if (!sym.equals("1"))
				lexer.error("The unit '"+Utilities.truncate(sym)+"' is unknown");
		}
		
		lexer.consume();
		if (lexer.getType() == TokenType.NUMBER) {
			int exp = parseInt(lexer);
			if (exp > MAX_EXPONENT || exp < -MAX_EXPONENT)
				lexer.error("The exponent "+exp+" is too large (limit is +/-"+MAX_EXPONENT+")");
			symbol.setExponent(exp);
			lexer.consume();
		} else
			symbol.setExponent(1);
		totalExponent += Math.abs(symbol.getExponent());
		if (totalExponent > MAX_TOTAL_EXPONENT)
			lexer.error("The exponents in the unit expression are too large in total (limit is "+MAX_TOTAL_EXPONENT+")");

		return symbol;
	}

	private int parseInt(Lexer lexer) throws UcumException {
		try {
			return lexer.getTokenAsInt();
		} catch (NumberFormatException e) {
			lexer.error("The number '"+Utilities.truncate(lexer.getToken())+"' is not valid");
			return 0; // never gets here
		}
	}
}
