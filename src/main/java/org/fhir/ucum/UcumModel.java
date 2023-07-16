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
import java.util.Date;
import java.util.List;

public class UcumModel {

	/**
	 * version="1.7" 
	 */
	private String version;
	
	/**
	 * revision="$Revision: 1.1 $"
	 */
	private String revision;
	
	/**
	 * date this revision was made public
	 */
	private Date revisionDate;
	
	private List<Prefix> prefixes = new ArrayList<Prefix>();
	private List<BaseUnit> baseUnits = new ArrayList<BaseUnit>();
	private List<DefinedUnit> definedUnits = new ArrayList<DefinedUnit>();

	
	/**
	 * @param revision
	 * @param revisionDate
	 */
	public UcumModel(String version, String revision, Date revisionDate) {
		super();
		this.version = version;
		this.revision = revision;
		this.revisionDate = revisionDate;
	}
	/**
	 * @return the prefixes
	 */
	public List<Prefix> getPrefixes() {
		return prefixes;
	}
	/**
	 * @return the baseUnits
	 */
	public List<BaseUnit> getBaseUnits() {
		return baseUnits;
	}
	/**
	 * @return the units
	 */
	public List<DefinedUnit> getDefinedUnits() {
		return definedUnits;
	}
	/**
	 * @return the revision
	 */
	public String getRevision() {
		return revision;
	}
	/**
	 * @param revision the revision to set
	 */
	public void setRevision(String revision) {
		this.revision = revision;
	}
	/**
	 * @return the revisionDate
	 */
	public Date getRevisionDate() {
		return revisionDate;
	}
	/**
	 * @param revisionDate the revisionDate to set
	 */
	public void setRevisionDate(Date revisionDate) {
		this.revisionDate = revisionDate;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	public Unit getUnit(String code) {
		for (Unit unit : getBaseUnits()) {
			if (unit.getCode().equals(code))
				return unit;
		}
		for (Unit unit : getDefinedUnits()) {
			if (unit.getCode().equals(code))
				return unit;
		}
		return null;
	}
	
	public BaseUnit getBaseUnit(String code) {
		for (BaseUnit unit : getBaseUnits()) {
			if (unit.getCode().equals(code))
				return unit;
		}
		return null;
	}
	
	
}
