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

public class Search {

	public List<Concept> doSearch(UcumModel model, ConceptKind kind, String text, boolean isRegex) {
		List<Concept> concepts = new ArrayList<Concept>();
		if (kind == null || kind == ConceptKind.PREFIX)
			searchPrefixes(concepts, model.getPrefixes(), text, isRegex);
		if (kind == null || kind == ConceptKind.BASEUNIT)
			searchUnits(concepts, model.getBaseUnits(), text, isRegex);
		if (kind == null || kind == ConceptKind.UNIT)
			searchUnits(concepts, model.getDefinedUnits(), text, isRegex);
		return concepts;
	}
	
	private void searchUnits(List<Concept> concepts, List<? extends Unit> units, String text, boolean isRegex) {
		for (Unit unit : units) {
			if (matchesUnit(unit, text, isRegex))
				concepts.add(unit);
		}
	}

	private boolean matchesUnit(Unit unit, String text, boolean isRegex) {
		return matches(unit.getProperty(), text, isRegex) || matchesConcept(unit, text, isRegex);
	}

	private void searchPrefixes(List<Concept> concepts, List<? extends Prefix> prefixes, String text, boolean isRegex) {
		for (Concept concept : prefixes) {
			if (matchesConcept(concept, text, isRegex))
				concepts.add(concept);
		}		
	}

	private boolean matchesConcept(Concept concept, String text, boolean isRegex) {
		for (String name : concept.getNames()) {
			if (matches(name, text, isRegex))
				return true;
		}
		if (matches(concept.getCode(), text, isRegex))
			return true;
		if (matches(concept.getCodeUC(), text, isRegex))
			return true;
		if (matches(concept.getPrintSymbol(), text, isRegex))
			return true;
		return false;
	}

	private boolean matches(String value, String text, boolean isRegex) {
		return (value != null) && ((isRegex  && value.matches(text)) || (!isRegex && value.toLowerCase().contains(text.toLowerCase())));
	}


}

