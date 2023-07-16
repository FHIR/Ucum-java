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


public class Lexer {

	private static final char NO_CHAR = Character.UNASSIGNED;
	private String source;
	private int index;
	
	private String token;
	private TokenType type;
	private int start;
	
	/**
	 * @param source
	 * @throws UcumException 
	 * @ 
	 */
	public Lexer(String source) throws UcumException  {
		super();
		this.source = source;
		if (source == null)
			source = "";
		index = 0;
		consume();
	}
	
	public void consume() throws UcumException  {
		token = null;
		type = TokenType.NONE;
		start = index;
		if (index < source.length()) {
			char ch = nextChar();
			if (!(checkSingle(ch, '/', TokenType.SOLIDUS) ||
					checkSingle(ch, '.', TokenType.PERIOD) || 
					checkSingle(ch, '(', TokenType.OPEN) || 
					checkSingle(ch, ')', TokenType.CLOSE) || 
					checkAnnotation(ch) ||
					checkNumber(ch) ||
					checkNumberOrSymbol(ch)))
				throw new UcumException("Error processing unit '"+source+"': unexpected character '"+ch+"' at position "+Integer.toString(start));			
		}		
	}

	private boolean checkNumber(char ch) throws UcumException  {
		if (ch == '+' || ch == '-') {
			token = String.valueOf(ch);
			ch = peekChar();
			while ((ch >= '0' && ch <= '9')) {
				token = token + ch;
				index++;
				ch = peekChar();
			}
			if (token.length() == 1) {
				throw new UcumException("Error processing unit'"+source+"': unexpected character '"+ch+"' at position "+Integer.toString(start)+": a + or - must be followed by at least one digit");			
				}
			type = TokenType.NUMBER;
			return true;
		} else
			return false;
	}

	private boolean checkNumberOrSymbol(char ch) throws UcumException  {
		boolean isSymbol = false;
		boolean inBrackets = false;
		if (isValidSymbolChar(ch, true, false)) {
			token = String.valueOf(ch);
			isSymbol = isSymbol || !((ch >= '0' && ch <= '9'));
			inBrackets = checkBrackets(ch, inBrackets);
			ch = peekChar();
			inBrackets = checkBrackets(ch, inBrackets);
			while (isValidSymbolChar(ch, !isSymbol || inBrackets, inBrackets)) {
				token = token + ch;
				isSymbol = isSymbol || ((ch != NO_CHAR) && !((ch >= '0' && ch <= '9')));
				index++;
				ch = peekChar();
				inBrackets = checkBrackets(ch, inBrackets);
			}
			if (isSymbol)
				type = TokenType.SYMBOL;
			else
				type = TokenType.NUMBER;
			return true;
		} else
			return false;
	}

	
	private boolean checkBrackets(char ch, boolean inBrackets) throws UcumException  {
		if (ch == '[')
			if (inBrackets)
				error("Nested [");
			else 
				return true;
		if (ch == ']')
			if (!inBrackets)
				error("] without [");
			else 
				return false;
		return inBrackets;
	}

	private boolean isValidSymbolChar(char ch, boolean allowDigits, boolean inBrackets) {
		return (allowDigits && ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') ||
		     ch == '[' || ch == ']' || ch == '%' || ch == '*' || ch == '^' || ch == '\'' || 
		     ch == '"' || ch == '_' || (inBrackets && ch == '.');
	}

	private boolean checkAnnotation(char ch) throws UcumException  {
		if (ch == '{') {
			StringBuilder b = new StringBuilder();
			while (ch != '}') {
				ch = nextChar();
				if (!Utilities.isAsciiChar(ch))
					throw new UcumException("Error processing unit'"+source+"': Annotation contains non-ascii characters");
				if (ch == 0) 
					throw new UcumException("Error processing unit'"+source+"': unterminated annotation");
				b.append(ch);
			}
			// got to the end of the annotation - need to do it again
			token = b.toString();
			type = TokenType.ANNOTATION;
			return true;
		} else
			return false;
	}

	private boolean checkSingle(char ch, char test, TokenType type) {
		if (ch == test) {
			token = String.valueOf(ch);
			this.type = type;			
			return true;
		}
		return false;
	}

	private char nextChar() {
		char res = index < source.length() ? source.charAt(index) : NO_CHAR;
		index++;
		return res;
	}

	private char peekChar() {
		return index < source.length() ? source.charAt(index) : NO_CHAR;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @return the type
	 */
	public TokenType getType() {
		return type;
	}

	public void error(String errMsg) throws UcumException  {
		throw new UcumException("Error processing unit '"+source+"': "+ errMsg +"' at position "+Integer.toString(start));			
		
	}

	public int getTokenAsInt() {
		return token.charAt(0) == '+' ? Integer.parseInt(token.substring(1)) : Integer.parseInt(token);
	}

	public boolean finished() {
		return index == source.length();
	}


}
