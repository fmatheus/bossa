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


/**
 * This class implements the close operation of <code>Case</code>
 * through the prevalence subsystem. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 * @see Case#closeCase()
 */
class CloseCase extends WFNetTransaction {

    private String caseTypeId;
    private int caseId;

    /**
     * Creates a new open operation. <p>
     *
     * @param caze the case to be closed.
     */
    CloseCase(Case caze) {
        this.caseId = caze.getId();
        this.caseTypeId = caze.getCaseType().getId();
    }

    /**
     * Executes the operation. <p>
     *
     * @see WFNetTransaction#execute(CaseTypeManager)
     */
    protected Object execute(CaseTypeManager caseTypeManager) {
        CaseType caseType = caseTypeManager.getCaseType(caseTypeId);
        Case caze = caseType.getCase(caseId);
        return new Boolean(caseType.closeCase(caze));
    }
}
