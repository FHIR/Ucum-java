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

	private UcumModel model;
	
	/**
	 * @param model
	 */
	public ExpressionParser(UcumModel model) {
		super();
		this.model = model;
	}

	public Term parse(String code) throws UcumException  {
		Lexer lexer = new Lexer(code);
		Term res = parseTerm(lexer, true);
		if (!lexer.finished())
			throw new UcumException("Expression was not parsed completely. Syntax Error?");
		return res;
	}
	
	private Term parseTerm(Lexer lexer, boolean first) throws UcumException  {
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
		if (lexer.getType() == TokenType.NUMBER) { 
			Factor fact = new Factor(lexer.getTokenAsInt());
			lexer.consume();
			return fact;
		} else if (lexer.getType() == TokenType.SYMBOL)
			return parseSymbol(lexer);
		else if  (lexer.getType() == TokenType.NONE)
			lexer.error("unexpected end of expression looking for a symbol or a number");
		else if (lexer.getType() == TokenType.OPEN) {
      lexer.consume();
      Term res = parseTerm(lexer, true);
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
				lexer.error("The unit '"+sym+"' is unknown");
		}
		
		lexer.consume();
		if (lexer.getType() == TokenType.NUMBER) {
			symbol.setExponent(lexer.getTokenAsInt());
			lexer.consume();
		} else
			symbol.setExponent(1);

		return symbol;
	}
}
