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
 * This class implements the set state operation of <code>Case</code>
 * through the prevalence subsystem. <p>
 * 
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 * @see Case#setState(Map)
 */
class SetState extends WFNetTransaction {

    private String caseTypeId;
    private int caseId;
    private Map newState;
    
    /**
     * Creates a new open operation. <p>
     * 
     * @param workItem the work item to be opened.
     * @param resource the resource opening it.
     */    
    SetState(Case caze, Map newState) {
        this.caseId = caze.getId();
        this.caseTypeId = caze.getCaseType().getId();
        this.newState = newState;
    }

    /**
     * Executes the operation. <p>
     * 
     * @exception EvaluationException if an expression evaluation error
     *            occurs. If this exception is thrown the state of the case
     *            may be left inconsistent.
     * @see WFNetTransaction#execute(CaseTypeManager)
     */
    protected Object execute(CaseTypeManager caseTypeManager)
        throws BossaException {
        Case caze = caseTypeManager.getCaseType(caseTypeId).getCase(caseId);
        caze.setStateImpl(newState);
        return null;
    }
}
