/*
 * Bossa Workflow System
 *
 * $Id$
 *
 * Copyright (C) 2003 OpenBR Sistemas S/C Ltda.
 *
 * This file is part of Bossa.
 *
 * Bossa is free software; you can redistribute it and/or modify it
 * under the terms of version 2 of the GNU General Public License as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

package com.bigbross.bossa.wfnet;

import java.util.HashMap;
import java.util.Map;

/**
 * This class manages all registered case types of the workflow
 * system. It provides all the case type life cycle functions. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class CaseTypeManager {
    
    Map caseTypes;
    
    public CaseTypeManager() {
        caseTypes = new HashMap();
    }

    /**
     * Registers a new case type in the manager. <p>
     * 
     * @param The CaseType object containig the case type.
     * @return <code>true</code> if the case type is registered,
     *         <code>false</code> if there is already a case type
     *         registered with the same id.
     */    
    public boolean registerCaseType(CaseType caseType) {
        if (caseTypes.containsKey(caseType.getId())) {
            return false;
        }
        caseTypes.put(caseType.getId(), caseType);
        return true;
    }
    
    /**
     * Removes the case type from the manager. This operation will remove also
     * <emph>all</emph> cases of this case type.
     * 
     * @param id The id of the case type.
     */
    public void removeCaseType(String id) {
        caseTypes.remove(id);
    }
    
    /**
     * Returns the case type with the provided id. <p>
     * 
     * FIXME: Maybe this is the place to provide a facade do the prevalent
     * system, instead of giving access to the case type itself. <p>
     * 
     * @param id The id of the desired case type.
     * @return The case type if it exists, <code>null</code> otherwise. 
     */
    public CaseType getCaseType(String id) {
        return (CaseType) caseTypes.get(id);
    }
}
