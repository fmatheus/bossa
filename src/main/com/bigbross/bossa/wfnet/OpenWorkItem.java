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

import java.util.Date;

import com.bigbross.bossa.BossaException;
import com.bigbross.bossa.resource.Resource;

/**
 * This class implements the open operation of <code>WorkItem</code>
 * through the prevalence subsystem. <p>
 * 
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 * @see com.bigbross.bossa.wfnet.WorkItem#open(Resource)
 * @see com.bigbross.bossa.wfnet.Case#open(WorkItem, Resource)
 */
class OpenWorkItem extends WFNetTransaction {

    private String caseTypeId;
    private int caseId;
    private String workItemId;
    private String resourceId;
    
    /**
     * Creates a new open operation. <p>
     * 
     * @param workItem the work item to be opened.
     * @param resource the resource opening it.
     */    
    OpenWorkItem(WorkItem workItem, Resource resource) {
        this.workItemId = workItem.getId();
        this.caseId = workItem.getCase().getId();
        this.caseTypeId = workItem.getCaseType().getId();
        this.resourceId = resource.getId();
    }

    /**
     * Executes the operation. <p>
     * 
     * @exception SetAttributeException if the underling expression
     *            evaluation system has problems setting an attribute.
     * @exception EvaluationException if an expression evaluation error
     *            occurs. If this exception is thrown the state of the case
     *            may be left inconsistent.
     * @see com.bigbross.bossa.wfnet.WFNetTransaction#execute(
     *      CaseTypeManager, Date)
     */
    protected Object execute(CaseTypeManager caseTypeManager, Date time)
        throws BossaException {
        Resource resource = caseTypeManager.getBossa().getResourceManager().getResource(resourceId);
        Case caze = caseTypeManager.getCaseType(caseTypeId).getCase(caseId);
        WorkItem workItem = caze.getWorkItem(workItemId);
        
        return caze.open(workItem, resource);
    }
}
