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

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fhir.ucum.definitions.DefinitionsProvider;
import org.fhir.ucum.definitions.DefinitionsProviderFactory;
import org.fhir.ucum.definitions.XmlDefinitionsParser;
import org.fhir.ucum.special.Registry;


/**
 * implements UCUM services. Applications must provide a copy of 
 * ucum-essence.xml as either a stream or a file name to create 
 * the services.
 * 
 * the provided ucum-essence.xml must be released on 25 Apr 2008 
 * or more recent. Note that if the ucum-essence.xml file does not 
 * contain a release date on an attribute of the root element, it 
 * is not more recent than this date (Gunther added it on this date for 
 * this usage, thanks)
 * 
 * See UcumService for documentation concerning the services this class provides
 * 
 * @author Grahame Grieve
 *
 */
public class UcumEssenceService implements UcumService {

	public static final String UCUM_OID = "2.16.840.1.113883.6.8";
	
	private UcumModel model;
	private Registry handlers = new Registry();
	
	/**
	 * Create an instance of Ucum services. Stream must point to a
	 * valid ucum-essence file (see class documentation) 
	 * @throws UcumException 
	 */
	public UcumEssenceService(InputStream stream) throws UcumException  {
		super();
		assert stream != null : paramError("factory", "stream", "must not be null");
		try {
			model = DefinitionsProviderFactory.getProvider().parse(stream);
		} catch (Exception e) {
			throw new UcumException(e); 
		}
	}

	/**
	 * Create an instance of Ucum services. filename must point to a
	 * valid ucum-essence file (see class documentation) 	
	 * @throws UcumException 
	 */
	public UcumEssenceService(String filename) throws UcumException  {
		super();
		assert new File(filename).exists() : paramError("factory", "file", "must exist");
		try {
			model = new XmlDefinitionsParser().parse(filename);
		} catch (Exception e) {
			throw new UcumException(e); 
		}
	}

	private String paramError(String method, String param, String msg) {
		return getClass().getName()+"."+method+"."+param+" is not acceptable: "+msg;
	}


	@Override
  public UcumVersionDetails ucumIdentification() {
		return new UcumVersionDetails(model.getRevisionDate(), model.getVersion());
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ohf.ucum.UcumServiceEx#getModel()
	 */
	@Override
  public UcumModel getModel() {
		return model;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ohf.ucum.UcumServiceEx#search(org.eclipse.ohf.ucum.model.ConceptKind, java.lang.String, boolean)
	 */
	@Override
  public List<Concept> search(ConceptKind kind, String text, boolean isRegex) {
		assert checkStringParam(text) : paramError("search", "text", "must not be null or empty");
		return new Search().doSearch(model, kind, text, isRegex);
	}


	/* (non-Javadoc)
	 * @see org.eclipse.ohf.ucum.UcumServiceEx#validateUCUM()
	 */
	@Override
  public List<String> validateUCUM() {		
		return new UcumValidator(model, handlers).validate();		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ohf.ucum.UcumServiceEx#getProperties()
	 */
	@Override
  public Set<String> getProperties() {
		Set<String> result = new HashSet<String>();
		for (DefinedUnit unit : model.getDefinedUnits()) {
			result.add(unit.getProperty());
		}
		return result;		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ohf.ucum.UcumServiceEx#validate(java.lang.String)
	 */
	@Override
  public String validate(String unit) {
		assert unit != null : paramError("validate", "unit", "must not be null");
		try {
			new ExpressionParser(model).parse(unit);
			return null;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ohf.ucum.UcumServiceEx#validateInProperty(java.lang.String, java.lang.String)
	 */
	@Override
  public String validateInProperty(String unit, String property) {
		assert checkStringParam(unit) : paramError("validate", "unit", "must not be null or empty");
		assert checkStringParam(property) : paramError("validateInProperty", "property", "must not be null or empty");
		try {
			Term term = new ExpressionParser(model).parse(unit);
			Canonical can = new Converter(model, handlers).convert(term);
			String cu = new ExpressionComposer().compose(can, false);
			if (can.getUnits().size() == 1) {
					if (property.equals(can.getUnits().get(0).getBase().getProperty()))
						return null;
					else
						return "unit "+unit+" is of the property type "+can.getUnits().get(0).getBase().getProperty()+" ("+cu+"), not "+property+" as required.";
			}
			// defined special case
			if ("concentration".equals(property) && ("g/L".equals(cu) || "mol/L".equals(cu)))
				return null;
			
			return "unit "+unit+" has the base units "+cu+", and are not from the property "+property+" as required.";
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ohf.ucum.UcumServiceEx#validateCanonicalUnits(java.lang.String, java.lang.String)
	 */
	@Override
  public String validateCanonicalUnits(String unit, String canonical) {
		assert checkStringParam(unit) : paramError("validate", "unit", "must not be null or empty");
		assert checkStringParam(canonical) : paramError("validateCanonicalUnits", "canonical", "must not be null or empty");
		try {
			Term term = new ExpressionParser(model).parse(unit);
			Canonical can = new Converter(model, handlers).convert(term);
			String cu = new ExpressionComposer().compose(can, false);
			if (!canonical.equals(cu))
				return "unit "+unit+" has the base units "+cu+", not "+canonical+" as required.";
			return null;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	/**
	 * given a unit, return a formal description of what the units stand for using
	 * full names 
	 * @param units the unit code
	 * @return formal description
	 * @throws UcumException 
	 * @ 
	 */
	@Override
  public String analyse(String unit) throws UcumException  {
		if (Utilities.noString(unit))
			return "(unity)";
		assert checkStringParam(unit) : paramError("analyse", "unit", "must not be null or empty");
		Term term = new ExpressionParser(model).parse(unit);
		return new FormalStructureComposer().compose(term);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ohf.ucum.UcumServiceEx#getCanonicalUnits(java.lang.String)
	 */
	@Override
  public String getCanonicalUnits(String unit) throws UcumException  {
		assert checkStringParam(unit) : paramError("getCanonicalUnits", "unit", "must not be null or empty");
		try {
			Term term = new ExpressionParser(model).parse(unit);
			return new ExpressionComposer().compose(new Converter(model, handlers).convert(term), false);	
		} catch (Exception e) {
			throw new UcumException("Error processing "+unit+": "+e.getMessage(), e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ohf.ucum.UcumServiceEx#getDefinedForms(java.lang.String)
	 */
	@Override
  public List<DefinedUnit> getDefinedForms(String code) throws UcumException  {
		assert checkStringParam(code) : paramError("getDefinedForms", "code", "must not be null or empty");
		List<DefinedUnit> result = new ArrayList<DefinedUnit>(); 
		BaseUnit base = model.getBaseUnit(code);
		if (base != null) {
			for (DefinedUnit unit : model.getDefinedUnits()) {
				if (!unit.isSpecial() && code.equals(getCanonicalUnits(unit.getCode())))
					result.add(unit);
			}
		}		
		return result;
	}
	
	private boolean checkStringParam(String s) {
		return s != null && !s.equals("");
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.ohf.ucum.UcumServiceEx#getCanonicalForm(org.eclipse.ohf.ucum.UcumEssenceService.Pair)
	 */
	@Override
	public Pair getCanonicalForm(Pair value) throws UcumException  {
	  assert value != null : paramError("getCanonicalForm", "value", "must not be null");
	  assert checkStringParam(value.getCode()) : paramError("getCanonicalForm", "value.code", "must not be null or empty");

	  Term term = new ExpressionParser(model).parse(value.getCode());
	  Canonical c = new Converter(model, handlers).convert(term);
	  Pair p = null;
	  if (value.getValue() == null)
	    p = new Pair(null, new ExpressionComposer().compose(c, false));
	  else {
	    p = new Pair(value.getValue().multiply(c.getValue()), new ExpressionComposer().compose(c, false));
	    if (value.getValue().isWholeNumber()) {
	      // whole numbers are tricky - they have implied infinite precision, but we need to check for digit errors in the last couple of digits
	      p.getValue().checkForCouldBeWholeNumber();
	    }
	  }		
	  return p;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ohf.ucum.UcumServiceEx#convert(java.math.BigDecimal, java.lang.String, java.lang.String)
	 */
	@Override
	public Decimal convert(Decimal value, String sourceUnit, String destUnit) throws UcumException  {
	  assert value != null : paramError("convert", "value", "must not be null");
	  assert checkStringParam(sourceUnit) : paramError("convert", "sourceUnit", "must not be null or empty");
	  assert checkStringParam(destUnit) : paramError("convert", "destUnit", "must not be null or empty");

	  if (sourceUnit.equals(destUnit))
	    return value;

	  Canonical src = new Converter(model, handlers).convert(new ExpressionParser(model).parse(sourceUnit));
	  Canonical dst = new Converter(model, handlers).convert(new ExpressionParser(model).parse(destUnit));
	  String s = new ExpressionComposer().compose(src, false);
	  String d = new ExpressionComposer().compose(dst, false);
	  if (!s.equals(d))
	    throw new UcumException("Unable to convert between units "+sourceUnit+" and "+destUnit+" as they do not have matching canonical forms ("+s+" and "+d+" respectively)");
	  Decimal canValue = value.multiply(src.getValue());
	  Decimal res = canValue.divide(dst.getValue());
	  if (value.isWholeNumber()) {
	    // whole numbers are tricky - they have implied infinite precision, but we need to check for digit errors in the last couple of digits
	    res.checkForCouldBeWholeNumber();
	  }
	  return res;
	}

	@Override
  public Pair multiply(Pair o1, Pair o2) throws UcumException  {
	  Pair res = new Pair(o1.getValue().multiply(o2.getValue()), o1.getCode() +"."+o2.getCode());
	  return getCanonicalForm(res);
	}

  @Override
  public Pair divideBy(Pair dividend, Pair divisor) throws UcumException  {
    Pair res = new Pair(dividend.getValue().divide(divisor.getValue()), 
        (dividend.getCode().contains("/") || dividend.getCode().contains("*") ? "("+ dividend.getCode() +")" : dividend.getCode())+"/"+(divisor.getCode().contains("/") || divisor.getCode().contains("*") ? "("+ divisor.getCode()+")" : divisor.getCode()));
    return getCanonicalForm(res);
  }

	@Override
  public String getCommonDisplay(String code) {
		//TODO: improvements
	  return code.replace("[", "").replace("]", "");
  }

  @Override
  public boolean isComparable(String units1, String units2) throws UcumException  {
    if (units1 == null)
      return false;
    if (units2 == null)
      return false;
    
    String u1 = getCanonicalUnits(units1);
    String u2 = getCanonicalUnits(units2);
    return u1.equals(u2);
  }

}
