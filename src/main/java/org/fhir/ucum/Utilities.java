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

public class Utilities {
  public static boolean isWhitespace(String s) {
    boolean ok = true;
    for (int i = 0; i < s.length(); i++)
      ok = ok && Character.isWhitespace(s.charAt(i));
    return ok;
    
  }

  public static boolean isDecimal(String string) {
    if (Utilities.noString(string))
      return false;
    try {
      float r = Float.parseFloat(string);
      return r != r + 1; // just to suppress the hint
    } catch (Exception e) {
      return false;
    }
  }

  public static boolean isInteger(String string) {
    try {
      int i = Integer.parseInt(string);
      return i != i+1;
    } catch (Exception e) {
      return false;
    }
  }
  
  public static boolean isHex(String string) {
    try {
      int i = Integer.parseInt(string, 16);
      return i != i+1;
    } catch (Exception e) {
      return false;
    }
  }
  

  public static boolean noString(String v) {
    return v == null || v.equals("");
  }

  public static String padLeft(String src, char c, int len) {
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < len - src.length(); i++)
      s.append(c);
    s.append(src);
    return s.toString();
  }

  public static boolean isAsciiChar(char ch) {
    return ch >= ' ' && ch <= '~'; 
  }



}
