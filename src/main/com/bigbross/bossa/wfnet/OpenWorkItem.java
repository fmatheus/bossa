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

import java.io.Serializable;

/**
 * This class
 * 
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
class OpenWorkItem extends WFNetCommand {

    private String caseTypeId;
    private int caseId;
    private String workItemId;
    
    OpenWorkItem(WorkItem workItem) {
        this.workItemId = workItem.getId();
        this.caseId = workItem.getCase().getId();
        this.caseTypeId = workItem.getCaseType().getId();
    }

    /**
     * @see com.bigbross.bossa.wfnet.command.WFNetCommand#execute(CaseTypeManager)
     */
    protected Serializable execute(CaseTypeManager caseTypeManager) {
    
        Case caze = caseTypeManager.getCaseType(caseTypeId).getCase(caseId);
        WorkItem workItem = caze.getWorkItem(workItemId);
        
        return caze.open(workItem);
    }
}
