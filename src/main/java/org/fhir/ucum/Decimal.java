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

/**
    Precision aware Decimal implementation. Any size number with any number of significant digits is supported
    (but see the precision limits below).

    Note that operations are precision aware operations: the result is limited to the number of
    significant figures that the operands justify. Whole numbers are assumed to be exact for 
    multiplication. For example:
      2 x 2 = 4
      2.0 x 2.0 = 4.0
      2.00 x 2.0 = 4.0
    For division, a whole number has as many significant figures as it has digits, so:
     10 / 3 = 3.3        (10 has 2 significant figures)
     10.0 / 3 = 3.33     (3 significant figures)
     10.00 / 3 = 3.333   (4 significant figures)
     10.00 / 3.0 = 3.3   (3.0 has 2 significant figures)
     10 / 3.0 = 3.3
     10 / 4 = 2.5        (exact results are not truncated)

    Addition does not (yet) take precision into account:
      2 + 0.001 = 2.001
      2.0 + 0.001 = 2.001

    Note that the string representation is precision limited, but the internal representation
    is not.

    Precision limits: multiplication rounds its operands and its result to at most 
    getMultiplyPrecisionLimit() significant digits. Division rounds its operands to at most
    getDividePrecisionLimit() significant digits, and calculates at most that many digits beyond 
    the length of the operands. Both default to 40 and can be changed application wide. These
    limits exist so that long numbers, or repeated operations, can't make the cost of 
    operations grow without bound. 
    
    Numbers in scientific notation must have an exponent within +/- MAX_EXPONENT (1000).

    
  * This class is defined to work around the limitations of Java Big Decimal
 * 
 * @author Grahame
 *
 */
public class Decimal {

	/**
	 * The default limit for the number of significant digits that multiplication will produce,
	 * and the number of digits beyond the operands that division will calculate
	 */
	public static final int DEFAULT_PRECISION_LIMIT = 40;

	/**
	 * The largest (absolute) exponent accepted when parsing a number in scientific notation.
	 * Without a limit, a short string such as 1e300000000 is accepted but can't be 
	 * represented as a string (or compared or added to) without exhausting memory
	 */
	public static final int MAX_EXPONENT = 1000;

	private static volatile int multiplyPrecisionLimit = DEFAULT_PRECISION_LIMIT;
	private static volatile int dividePrecisionLimit = DEFAULT_PRECISION_LIMIT;

	/**
	 * @return the maximum number of significant digits a multiplication will produce. 
	 * The result is rounded (half up) to this many digits, and so are any operands with more digits
	 */
	public static int getMultiplyPrecisionLimit() {
		return multiplyPrecisionLimit;
	}

	/**
	 * Set the maximum number of significant digits a multiplication will produce 
	 * (application wide). Operands with more digits than this are rounded first. Without a 
	 * limit, repeated multiplication grows the number of digits without bound, and the cost
	 * of multiplication grows faster than the number of digits. Default is 40.
	 * 
	 * @param limit - must be at least 1
	 */
	public static void setMultiplyPrecisionLimit(int limit) {
		if (limit < 1)
			throw new IllegalArgumentException("Multiply precision limit must be at least 1");
		multiplyPrecisionLimit = limit;
	}

	/**
	 * @return the number of digits beyond the length of the operands that division will 
	 * calculate before stopping (for non-terminating results such as 1/3). Operands with
	 * more significant digits than this are rounded to this many digits first
	 */
	public static int getDividePrecisionLimit() {
		return dividePrecisionLimit;
	}

	/**
	 * Set the number of digits beyond the length of the operands that division will 
	 * calculate before stopping, for non-terminating results such as 1/3 (application wide). 
	 * Operands with more significant digits than this are rounded to this many digits first, 
	 * since the cost of division grows with the square of the number of digits. Default is 40.
	 * 
	 * @param limit - must be at least 1
	 */
	public static void setDividePrecisionLimit(int limit) {
		if (limit < 1)
			throw new IllegalArgumentException("Divide precision limit must be at least 1");
		dividePrecisionLimit = limit;
	}

	private int precision;
	private boolean scientific;
	private boolean negative;
	private String digits;
	private int decimal;

	private Decimal() {
		super();
	}
	
	public Decimal(String value) throws UcumException  {
		super();
		if (value == null)
			throw new UcumException("'null' is not a valid decimal");
		value = value.toLowerCase();
		if (value.contains("e"))
			setValueScientific(value);
		else
			setValueDecimal(value);
	}

	/**
	 * There are a few circumstances where a simple value is known to be correct to a high
	 * precision. For instance, the unit prefix milli is not ~0.001, it is precisely 0.001
	 * to whatever precision you want to specify. This constructor allows you to specify 
	 * an alternative precision than the one implied by the stated string
	 * 
	 * @param value - a string representation of the value
	 * @param precision - a 
	 * @throws UcumException 
	 * @
	 */
	public Decimal(String value, int precision) throws UcumException  {
		super();
		if (value == null)
			throw new UcumException("'null' is not a valid decimal");
		if (precision < 0)
			throw new UcumException("Precision cannot be negative: "+precision);
		value = value.toLowerCase();
		if (value.contains("e"))
			setValueScientific(value);
		else
			setValueDecimal(value);
		this.precision = precision;
	}

	public Decimal(int i) {
		super();
		try {
	    setValueDecimal(Integer.toString(i));
    } catch (Exception e) {
    }
  }

	private void setValueDecimal(String value) throws UcumException  {
		//	var
		//	  dec : integer;
		//	  i : integer;
		scientific = false;
		int dec = -1;
		negative = value.startsWith("-");
		if (negative)
			value = value.substring(1);

		int start = 0;
		while (start < value.length() - 1 && value.charAt(start) == '0')
			start++;
		value = value.substring(start);
		if (value.length() == 0)
			throw new UcumException("'"+(negative ? "-" : "")+"' is not a valid decimal");

		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) == '.' && dec == -1)
				dec = i;
			else if (!isAsciiDigit(value.charAt(i)))
				throw new UcumException("'"+Utilities.truncate(value)+"' is not a valid decimal");
		}

		if (dec == -1) {
			precision = value.length();
			decimal = value.length();
			digits = value;
		} else if (dec == value.length() -1)
			throw new UcumException("'"+Utilities.truncate(value)+"' is not a valid decimal");
		else {
			decimal = dec;
			if (allZeros(value, 1))
				precision = value.length() - 1;
			else
				precision = countSignificants(value);
			digits = delete(value, decimal, 1);
			if (allZeros(digits, 0))
				precision++;
			else {
				int z = 0;
				while (digits.charAt(z) == '0') 
					z++;
				digits = digits.substring(z);
				decimal = decimal - z;
			}
		}
	}

	private static boolean isAsciiDigit(char ch) {
		return ch >= '0' && ch <= '9';
	}

	private boolean allZeros(String s, int start) {
		boolean result = true;
		for (int i = start; i < s.length(); i++) {
			if (s.charAt(i) != '0')
				result = false;
		}
		return result;
	}

	private int countSignificants(String value) {
		int i = value.indexOf(".");
		if (i > -1)
			value = delete(value, i, 1);
		int z = 0;
		while (z < value.length() && value.charAt(z) == '0')
			z++;
		return value.length() - z;
	}

	private String delete(String value, int offset, int length) {
		if (offset == 0)
			return value.substring(length);
		else
			return value.substring(0,  offset)+value.substring(offset+length);
	}
	
	private void setValueScientific(String value) throws UcumException  {
		int i = value.indexOf("e");
		String s = value.substring(0, i);
		String e = value.substring(i+1);
				
	  if (Utilities.noString(s) || s.equals("-") || !Utilities.isDecimal(s))
	    throw new UcumException("'"+Utilities.truncate(value)+"' is not a valid decimal (numeric)");
	  if (Utilities.noString(e) || e.equals("-") || !Utilities.isInteger(e))
	    throw new UcumException("'"+Utilities.truncate(value)+"' is not a valid decimal (exponent)");

	  setValueDecimal(s);
	  scientific = true;

	  // now adjust for exponent

	  if (e.charAt(0) == '-')
	    i = 1;
	  else
	    i = 0;
	  while (i < e.length()) {
	    if (!isAsciiDigit(e.charAt(i)))
	      throw new UcumException("'"+Utilities.truncate(value)+"' is not a valid decimal");
	    i++;
	  }
	  i = Integer.parseInt(e);
	  long d = (long) decimal + i;
	  if (d - 1 > MAX_EXPONENT || d - 1 < -MAX_EXPONENT) // d - 1 is the exponent when written as d.ddd x 10^e
	    throw new UcumException("'"+Utilities.truncate(value)+"' is not a valid decimal (exponent is too large: limit is "+MAX_EXPONENT+")");
	  decimal = (int) d;
	}

	private String stringMultiply(char c, int i) {
	  return Utilities.padLeft("", c, i);
  }

	private String insert(String ins, String value, int offset) {
		if (offset == 0)
			return ins+value;
		else
			return value.substring(0, offset)+ins+value.substring(offset);
  }

	@Override
  public String toString() {
	  return asDecimal();
  }

	public Decimal copy() {
		Decimal result = new Decimal();
		result.precision = precision;
		result.scientific = scientific;
		result.negative = negative;
		result.digits = digits;
		result.decimal = decimal;
		return result;
	}

	public static Decimal zero()  {
		try {
			return new Decimal("0");
		} catch (Exception e) {
			return null; // won't happen
		}
	}

	public boolean isZero() {
	  return allZeros(digits, 0);
	}
	
	public static Decimal one() {
		try {
			return new Decimal("1");
		} catch (Exception e) {
			return null; // won't happen
		}
	}

	public boolean isOne() {
	  Decimal one = one();
	  return comparesTo(one) == 0;
	}

	public boolean equals(Decimal other) {
		return comparesTo(other) == 0;
	}
	
	public int comparesTo(Decimal other) {
		//	  s1, s2 : AnsiString;
		if (other == null)
			return 0;

		boolean thisZero = this.isZero();
		boolean otherZero = other.isZero();
		if (thisZero && otherZero)
			return 0;

		boolean thisNeg = this.negative && !thisZero;
		boolean otherNeg = other.negative && !otherZero;

		if (thisNeg && !otherNeg)
			return -1;
		else if (!thisNeg && otherNeg)
			return 1;
		else {
			int max = Math.max(this.decimal, other.decimal);
			String s1 = stringMultiply('0', max - this.decimal+1) + this.digits;
			String s2 = stringMultiply('0', max - other.decimal+1) + other.digits;
			if (s1.length() < s2.length()) 
				s1 = s1 + stringMultiply('0', s2.length() - s1.length());
			else if (s2.length() < s1.length()) 
				s2 = s2 + stringMultiply('0', s1.length() - s2.length());
			int result = s1.compareTo(s2);
			if (thisNeg)
				result = -result;
			return result;
		}
	}

	public boolean isWholeNumber() {
	  // equivalent to !asDecimal().contains("."), without building the string 
	  return decimal >= digits.length();
	}

	public String asDecimal() {
		String result = digits;
		if (decimal != digits.length())
			if (decimal < 0)
				result = "0."+stringMultiply('0', 0-decimal)+digits;
			else if (decimal < result.length())
				if (decimal == 0) 
					result = "0."+result;
				else
					result = insert(".", result, decimal);
			else
				result = result + stringMultiply('0', decimal - result.length());
		if (negative && !allZeros(result, 0))
			result = "-" + result;
		return result;
	}

	public int asInteger() throws UcumException  {
	  if (!isWholeNumber())
	    throw new UcumException("Unable to represent "+toString()+" as an integer");
	  if (comparesTo(new Decimal(Integer.MIN_VALUE)) < 0)
	  	throw new UcumException("Unable to represent "+toString()+" as a signed 8 byte integer");
	  if (comparesTo(new Decimal(Integer.MAX_VALUE)) > 0)
	  	throw new UcumException("Unable to represent "+toString()+" as a signed 8 byte integer");
	  return Integer.parseInt(asDecimal());
  }

	public String asScientific() {
	  String result = digits;
	  boolean zero = allZeros(result, 0);
	  if (zero) {
	    if (precision < 2)
	      result = "0e0";
	    else
	      result = "0."+stringMultiply('0', precision-1)+"e0";
	  } else {
	    if (digits.length() > 1)
	      result = insert(".", result, 1);
	    result = result + 'e'+Integer.toString(decimal - 1);
	  }
	  if (negative && !zero)
	    result = '-' + result;
	  return result;
  }

	public Decimal trunc() {
	  if (decimal < 0)
    return zero();

    Decimal result = copy();
    if (result.digits.length() >= result.decimal)
    	result.digits = result.digits.substring(0, result.decimal);
    if (Utilities.noString(result.digits)) {
      result.digits = "0";
      result.decimal = 1;
      result.negative = false;
    }
	  return result;
  }


	public Decimal add(Decimal other) {
	  if (other == null) 
	    return null;
	  
	  if (negative == other.negative) {
	    Decimal result = doAdd(other);
	    result.negative = negative;
	    return result;
	  } else if (negative) 
	    return other.doSubtract(this);
	  else
	  	return doSubtract(other);
	}

	public Decimal subtract(Decimal other) {
	  if (other == null) 
	    return null;

	  Decimal result;
    if (negative && !other.negative) {
	    result = doAdd(other);
	    result.negative = true;
    } else if (!negative && other.negative) {
	    result = doAdd(other);
    } else if (negative && other.negative) {
	    result = doSubtract(other);
	    result.negative = !result.negative;
    } else { 
	    result = other.doSubtract(this);
	    result.negative = !result.negative;
    }
    return result;
	}

	
	private Decimal doAdd(Decimal other) {
	  int max = Math.max(decimal, other.decimal);
	  String s1 = stringMultiply('0', max - decimal+1) + digits;
	  String s2 = stringMultiply('0', max - other.decimal+1) + other.digits;
	  if (s1.length() < s2.length())
	    s1 = s1 + stringMultiply('0', s2.length() - s1.length());
	  else if (s2.length() < s1.length())
	    s2 = s2 + stringMultiply('0', s1.length() - s2.length());

	  String s3 = stringAddition(s1, s2);

	  if (s3.charAt(0) == '1')
	    max++;
	  else
	    s3 = delete(s3, 0, 1);
	  
	  if (max != s3.length()) {
	    if (max < 0)
	      s3 = "0." + stringMultiply('0', -max) + s3;
	    else if (max < s3.length())
	      s3 = insert(".", s3, max);
	    else
	      s3 = s3 + stringMultiply('0', max - s3.length());
	  }
	  
	  Decimal result = new Decimal();
	  try {
	    result.setValueDecimal(s3);
    } catch (Exception e) {
    	// won't happen
    }
	  result.scientific = scientific || other.scientific;
	  // todo: the problem with this is you have to figure out the absolute precision and take the lower of the two, not the relative one
	  if (decimal < other.decimal)
	    result.precision = precision;
	  else if (other.decimal < decimal)
	    result.precision = other.precision;
	  else
	    result.precision = Math.min(precision, other.precision);
	  return result;
	}

	private int dig(char c) {
	  return (c) - ('0');
  }

	private char cdig(int i) {
	  return (char) (i + ('0'));
  }

	private Decimal doSubtract(Decimal other)  {
	  int max = Math.max(decimal, other.decimal);
	  String s1 = stringMultiply('0', max - decimal+1) + digits;
	  String s2 = stringMultiply('0', max - other.decimal+1) + other.digits;
	  if (s1.length() < s2.length())
	    s1 = s1 + stringMultiply('0', s2.length() - s1.length());
	  else if (s2.length() < s1.length())
	    s2 = s2 + stringMultiply('0', s1.length() - s2.length());

	  String s3;
	  boolean neg = (s1.compareTo(s2) <  0);
	  if (neg) {
	    s3 = s2;
	    s2 = s1;
	    s1 = s3;
	  }

	  s3 = stringSubtraction(s1, s2);

	  if (s3.charAt(0) == '1')
	    max++;
	  else
	    s3 = delete(s3, 0, 1);
	  if (max != s3.length()) {
	    if (max < 0)
	      s3 = "0." + stringMultiply('0', -max) + s3;
	    else if (max < s3.length())
	      s3 = insert(".", s3, max);
	    else
	      s3 = s3 + stringMultiply('0', max - s3.length());
	  }

	  Decimal result = new Decimal();
	  try {
	    result.setValueDecimal(s3);
    } catch (Exception e) {
    	// won't happen
    }
	  result.negative = neg;
	  result.scientific = scientific || other.scientific;
	  if (decimal < other.decimal)
	  	result.precision = precision;
	  else if (other.decimal < decimal)
	  	result.precision = other.precision;
	  else
	  	result.precision = Math.min(precision, other.precision);
	  return result;
	}

	private String stringAddition(String s1, String s2) {
	  assert(s1.length() == s2.length());
	  char[] result = new char[s2.length()];
	  for (int i = 0; i < s2.length(); i++)
	  	result[i] = '0';
	  int c = 0;
	  for (int i = s1.length() - 1; i >= 0; i--) {
	    int t = c + dig(s1.charAt(i)) + dig(s2.charAt(i));
	    result[i] = cdig(t % 10);
	    c = t / 10;
	  }
	  assert(c == 0);
	  return new String(result);
	}

	private String stringSubtraction(String s1, String s2) {
//	  i, t, c : integer;
	  assert(s1.length() == s2.length());

	  char[] result = new char[s2.length()];
	  for (int i = 0; i < s2.length(); i++)
	  	result[i] = '0';

	  int borrow = 0;
	  for (int i = s1.length() - 1; i >= 0; i--) {
	    int t = dig(s1.charAt(i)) - dig(s2.charAt(i)) - borrow;
	    if (t < 0) {
	      t = t + 10;
	      borrow = 1;
	    } else {
	      borrow = 0;
	    }
	    result[i] = cdig(t);
	  }
	  if (borrow != 0)
	    throw new Error("internal logic error");
	  return new String(result);
	}


	public Decimal multiply(Decimal other) {
		if (other == null)
			return null;
		int limit = multiplyPrecisionLimit;
		if (digits.length() > limit || other.digits.length() > limit)
			// operands with more digits than the limit are rounded first, so that the cost is bounded
			return roundedTo(limit).doMultiply(other.roundedTo(limit), limit);
		else
			return doMultiply(other, limit);
	}

	private Decimal doMultiply(Decimal other, int limit) {
		if (isZero() || other.isZero())
			return zero();

		// multiply the significant digits as integers; the cost depends only on the number of 
		// digits, not the magnitude of the numbers. The value is digits x 10^(decimal - digits.length())
		String res = new java.math.BigInteger(digits).multiply(new java.math.BigInteger(other.digits)).toString();
		int dec = res.length() + (decimal - digits.length()) + (other.decimal - other.digits.length());

		if (res.length() > limit) {
			res = roundDigits(res, limit);
			if (res.length() > limit) { // carried into a new leading digit
				res = res.substring(0, limit);
				dec++;
			}
		}

		int prec = 0;
		if (isWholeNumber() && other.isWholeNumber())
			// at least the specified precision, and possibly more
			prec = Math.max(Math.max(digits.length(), other.digits.length()), Math.min(precision, other.precision));
		else if (isWholeNumber())
			prec = other.precision;
		else if (other.isWholeNumber())
			prec = precision;
		else
			prec = Math.min(precision, other.precision);
		prec = Math.min(prec, limit);
		while (res.length() > prec && res.charAt(res.length()-1) == '0')
			res = delete(res, res.length()-1, 1);

		Decimal result = new Decimal();
		try {
	    result.setValueDecimal(res);
    } catch (Exception e) {
	    // won't happen
    }
		result.precision = prec;
		result.decimal = dec;
		result.negative = negative != other.negative;
		result.scientific = scientific || other.scientific;
		return result;
	}

	/**
	 * Round a string of digits (half up) to the given number of digits. The position of the 
	 * decimal point doesn't change unless the rounding carries into a new leading digit 
	 * (999.. -> 1000..), in which case the result has limit+1 digits 
	 */
	private static String roundDigits(String s, int limit) {
		if (s.length() <= limit)
			return s;
		boolean up = s.charAt(limit) >= '5';
		char[] rs = s.substring(0, limit).toCharArray();
		int i = rs.length - 1;
		while (up && i >= 0) {
			if (rs[i] == '9') {
				rs[i] = '0';
				i--;
			} else {
				rs[i]++;
				up = false;
			}
		}
		return up ? "1"+new String(rs) : new String(rs);
	}

	/**
	 * @return this value rounded to at most limit significant digits (or this, if it's already short enough)
	 */
	private Decimal roundedTo(int limit) {
		if (digits.length() <= limit)
			return this;
		Decimal result = copy();
		String r = roundDigits(digits, limit);
		if (r.length() > limit) {
			r = r.substring(0, limit);
			result.decimal++;
		}
		result.digits = r;
		result.precision = Math.min(precision, limit);
		return result;
	}

	public Decimal divide(Decimal other) throws UcumException  {
		if (other == null)
			return null;
		int limit = dividePrecisionLimit;
		if (digits.length() > limit || other.digits.length() > limit)
			// operands with more digits than the limit are rounded first, so that the cost is bounded
			return roundedTo(limit).doDivide(other.roundedTo(limit), limit);
		else
			return doDivide(other, limit);
	}

	private Decimal doDivide(Decimal other, int limit) throws UcumException  {

		if (other.isZero())
			throw new UcumException("Attempt to divide "+toString()+" by zero");

		if (isZero())
			return zero();

		String s = "0"+other.digits;
		int m = Math.max(digits.length(), other.digits.length()) + limit; // max loops we'll do
		String[] tens = new String[10];
		tens[0] = stringAddition(stringMultiply('0', s.length()), s);
		for (int i = 1; i < 10; i++)
			tens[i] = stringAddition(tens[i-1], s);
		String v = digits;
		String r = "";
		int l = 0;
		int d = (digits.length() - decimal + 1) - (other.digits.length() - other.decimal + 1);

		while (v.length() < tens[0].length()) {
			v = v + "0";
			d++;
		}

		String w;
		int vi;
		if (v.substring(0, other.digits.length()).compareTo(other.digits) < 0) {
			if (v.length() == tens[0].length()) {
				v = v + '0';
				d++;
			}
			w = v.substring(0, other.digits.length()+1);
			vi = w.length();
		} else {
			w = "0"+v.substring(0, other.digits.length());
			vi = w.length()-1;
		}

		boolean handled = false;
		boolean proc;

		while (!(handled && ((l > m) || ((vi >= v.length()) && ((Utilities.noString(w) || allZeros(w, 0))))))) {
			l++;
			handled = true;
			proc = false;
			for (int i = 8; i >= 0; i--) {
				if (tens[i].compareTo(w) <= 0) {
					proc = true;
					r = r + cdig(i+1);
					w = trimLeadingZeros(stringSubtraction(w, tens[i]));
					if (!(handled && ((l > m) || ((vi >= v.length()) && ((Utilities.noString(w) || allZeros(w, 0))))))) {
						if (vi < v.length()) {
							w = w + v.charAt(vi);
							vi++;
							handled = false;
						} else {
							w = w + '0';
							d++;
						}
						while (w.length() < tens[0].length()) 
							w = '0'+w;
					}
					break;
				}
			}
			if (!proc) {
				assert(w.charAt(0) == '0');
				w = delete(w, 0, 1);
				r = r + "0";
				if (!(handled && ((l > m) || ((vi >= v.length()) && ((Utilities.noString(w) || allZeros(w, 0))))))) {
					if (vi < v.length()) {
						w = w + v.charAt(vi);
						vi++;
						handled = false;
					} else {
						w = w + '0';
						d++;
					}
					while (w.length() < tens[0].length()) 
						w = '0'+w;
				}
			}
		}

		int prec;

		if (isWholeNumber() && other.isWholeNumber() && (l < m)) {
			for (int i = 0; i < d; i++) { 
				if (r.charAt(r.length()-1) == '0') { 
					r = delete(r, r.length()-1, 1);
					d--;
				}
			}
			prec = 100;
		} else {
			if (isWholeNumber() && other.isWholeNumber())
				prec = Math.max(digits.length(), other.digits.length());
			else if (isWholeNumber())
				prec = Math.max(other.precision, r.length() - d);
			else if (other.isWholeNumber())
				prec = Math.max(precision, r.length() - d);
			else
				prec = Math.max(Math.min(precision, other.precision), r.length() - d);
			if (r.length() > prec) {
			  d = d - (r.length() - prec);
			  char dig = r.charAt(prec);
			  boolean up = dig >= '5';
			  if (up) {
			    char[] rs = r.substring(0, prec).toCharArray();
			    int i = rs.length-1;
			    boolean carry = true;
			    while (carry && i >= 0) {
	          char ls = rs[i];
	          if (ls == '9') { 
	            rs[i] = '0';
	          } else { 
 			        ls = (char) (((int) ls) + 1);
 			        rs[i] = ls;
 			       carry = false;
	          }
	          i--;
			    }
			    if (carry) {
            r = "1"+new String(rs);
            d++; // cause we added one at the start
          }
			    else
            r = new String(rs);
			  } else 
			    r = r.substring(0, prec);
			}
		}

		Decimal result = new Decimal();
		result.setValueDecimal(r);
		result.decimal = r.length() - d;
		result.negative = negative != other.negative;
		result.precision = prec;
		result.scientific = scientific || other.scientific;
		return result;
	}


	private String trimLeadingZeros(String s) {
		if (s == null) 
			return null;

		int i = 0;
		while (i < s.length() && s.charAt(i) == '0') 
			i++;
		if (i == s.length())
			return "0";
		else
		  return s.substring(i);
	}

	public Decimal divInt(Decimal other) throws UcumException  {
	  if (other == null)
	  	return null;
	  Decimal t = divide(other);
	  return t.trunc();
	}

	public Decimal modulo(Decimal other) throws UcumException  {
	  if (other == null)  
      return null;
	  Decimal t = divInt(other);
	  Decimal t2 = t.multiply(other);
	  return subtract(t2);
	}

	public boolean equals(Decimal value, Decimal maxDifference) {
	  if (value == null || maxDifference == null)
	    return false;
	  Decimal diff = this.subtract(value).absolute();
	  return diff.comparesTo(maxDifference) <= 0;
  }

	private Decimal absolute() {
	  Decimal d = copy();
	  d.negative = false;
	  return d;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Decimal)
      return asDecimal().equals(((Decimal) other).asDecimal());
    else
      return super.equals(other);
  }

  @Override
  public int hashCode() {
    return asDecimal().hashCode();
  }

  public void limitPrecisionTo(Decimal other) {
    // precision can't be greater than other 
    if (other != null && precision > other.precision) {
      precision = other.precision;
    }
  }

  public void checkForCouldBeWholeNumber() {
    // whole numbers are tricky - they have implied infinite precision, but we need to check for digit errors in the last couple of digits
    // it's a whole number if all but the last one or two digits after the decimal place is 9 or 0 and the precision is >17 (arbitrary but enough)
    if (precision > 17 && digits.length() > 3) {
      int i = digits.length()-2;
      char ch = digits.charAt(i); // second last character
      if (ch == '9') {
        while (i > 0 && digits.charAt(i-1) == '9') {
          i--;
        }      
        if (i > 0 && i < digits.length() - 3) {
          digits = digits.substring(0, i-1)+Character.toString((char) (digits.charAt(i-1) + 1));
          precision = digits.length();
        }
      } else if (ch == '0') {
        while (i > 0 && digits.charAt(i-1) == '0') {
          i--;
        }      
        if (i > 0 && i < digits.length() - 3) {
          digits = digits.substring(0, i);
          precision = digits.length();
        }
      }
    }
  }
  
}
