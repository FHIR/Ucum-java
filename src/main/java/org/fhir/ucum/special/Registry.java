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

package org.fhir.ucum.special;

import java.util.HashMap;
import java.util.Map;

import org.fhir.ucum.Decimal;

public class Registry {

	Map<String, SpecialUnitHandler> handlers = new HashMap<String, SpecialUnitHandler>();

	public Registry() {
		super();
		init();
	}

	private void register(SpecialUnitHandler handler) {
		handlers.put(handler.getCode(), handler);		
	}
	
	private void init() {
		register(new CelsiusHandler());		
		register(new FahrenheitHandler());		
		register(new HoldingHandler("[p'diop]", "deg"));		
		register(new HoldingHandler("%[slope]", "deg"));		
		register(new HoldingHandler("[hp_X]", "1"));		
		register(new HoldingHandler("[hp_C]", "1"));		
		register(new HoldingHandler("[pH]", "mol/l"));		
		register(new HoldingHandler("Np", "1"));		
		register(new HoldingHandler("B", "1"));		
		register(new HoldingHandler("B[SPL]", "10*-5.Pa", new Decimal(2)));		
		register(new HoldingHandler("B[V]", "V"));		
		register(new HoldingHandler("B[mV]", "mV"));		
		register(new HoldingHandler("B[uV]", "uV"));		
		register(new HoldingHandler("B[W]", "W"));		
		register(new HoldingHandler("B[kW]", "kW"));		
		register(new HoldingHandler("bit_s", "1"));		
	}

	public boolean exists(String code) {
		return handlers.containsKey(code);
	}

	public SpecialUnitHandler get(String code) {
		return handlers.get(code);
	}

	

	
}
