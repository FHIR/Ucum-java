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

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Evaluates a caller supplied regular expression against a series of strings, giving up
 * once a deadline has passed, so that a pathological expression (catastrophic backtracking, 
 * aka ReDoS) can't consume unbounded CPU. 
 * 
 * Adapted from org.hl7.fhir.utilities.regex.RegexTimeout in the FHIR core library (which 
 * this library can't depend on). The deadline is enforced on the calling thread: the input 
 * is wrapped in a CharSequence that throws once the deadline has passed. The regex engine 
 * reads the input through charAt() as it matches, including while backtracking, so a 
 * runaway evaluation is stopped promptly. The deadline is checked every CHECK_INTERVAL 
 * characters rather than on every one.
 * 
 * Unlike the core version, one deadline covers all the evaluations made through an instance,
 * since a search evaluates the same expression against thousands of short strings.
 */
class RegexTimeout {

  static final long DEFAULT_TIMEOUT = 500; // milliseconds

  private final Pattern pattern;
  private final String regex;
  private final long timeoutMillis;
  private final long deadlineNanos;

  /**
   * @throws IllegalArgumentException if the regex isn't valid
   */
  RegexTimeout(String regex, long timeoutMillis) {
    this.regex = regex;
    this.timeoutMillis = timeoutMillis;
    try {
      this.pattern = Pattern.compile(regex);
    } catch (PatternSyntaxException e) {
      throw new IllegalArgumentException("The regular expression '"+Utilities.truncate(regex)+"' is not valid: "+Utilities.truncate(e.getDescription()), e);
    }
    this.deadlineNanos = System.nanoTime() + timeoutMillis * 1000000L;
  }

  /**
   * @return true if the whole of value matches the regex (as String.matches)
   * @throws IllegalArgumentException if the deadline has passed
   */
  boolean matches(CharSequence value) {
    if (System.nanoTime() > deadlineNanos)
      throw timedOut();
    try {
      return pattern.matcher(new DeadlineCharSequence(value, deadlineNanos)).matches();
    } catch (DeadlineExceededException e) {
      throw timedOut();
    }
  }

  private IllegalArgumentException timedOut() {
    return new IllegalArgumentException("The regular expression '"+Utilities.truncate(regex)+"' took too long to evaluate (limit is "+timeoutMillis+"ms)");
  }

  /**
   * Thrown from inside the regex engine when the deadline has passed. No stack trace
   */
  private static final class DeadlineExceededException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    DeadlineExceededException() {
      super(null, null, false, false);
    }
  }

  static final class DeadlineCharSequence implements CharSequence {
    /**
     * How many characters are read between deadline checks. Catastrophic backtracking reads 
     * orders of magnitude more than this before it becomes slow, so it is still caught
     */
    static final int CHECK_INTERVAL = 1024;

    private final CharSequence inner;
    private final long deadlineNanos;
    private int countdown = CHECK_INTERVAL;

    DeadlineCharSequence(CharSequence inner, long deadlineNanos) {
      this.inner = inner;
      this.deadlineNanos = deadlineNanos;
    }

    @Override
    public char charAt(int index) {
      if (--countdown <= 0) {
        countdown = CHECK_INTERVAL;
        if (System.nanoTime() > deadlineNanos)
          throw new DeadlineExceededException();
      }
      return inner.charAt(index);
    }

    @Override
    public int length() {
      return inner.length();
    }

    @Override
    public CharSequence subSequence(int start, int end) {
      return new DeadlineCharSequence(inner.subSequence(start, end), deadlineNanos);
    }

    @Override
    public String toString() {
      return inner.toString();
    }
  }
}
