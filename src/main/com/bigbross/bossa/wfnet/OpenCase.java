/*
 * Bossa Workflow System
 *
 * $Id$
 *
 * Copyright (C) 2004 OpenBR Sistemas S/C Ltda.
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

import java.util.Map;

import com.bigbross.bossa.BossaException;

/**
 * This class implements the open case operation of <code>CaseType</code>
 * through the prevalence subsystem. <p>
 * 
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 * @see CaseType#openCase()
 * @see CaseType#openCase(Map)
 * @see CaseType#openCaseImpl(Map)
 */
class OpenCase extends WFNetTransaction {

    private String caseTypeId;
    private Map state;

    /**
     * Creates a new open case operation. <p>
     * 
     * @param state an optional token count map.
     */    
    OpenCase(String caseTypeId, Map state) {
        this.caseTypeId = caseTypeId;
        this.state = state;
    }

    /**
     * Executes the operation. <p>
     * 
     * @exception SetAttributeException if the underlying expression
     *            evaluation system has problems setting an attribute.
     * @see WFNetTransaction#execute(CaseTypeManager)
     */
    protected Object execute(CaseTypeManager caseTypeManager)
        throws BossaException {
        CaseType caseType = caseTypeManager.getCaseType(caseTypeId);
        return caseType.openCaseImpl(state);
    }
}
