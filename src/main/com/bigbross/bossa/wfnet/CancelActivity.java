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
 * This class implements the cancel operation of <code>Activity</code>
 * through the prevalence subsystem. <p>
 * 
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 * @see com.bigbross.bossa.wfnet.Activity#cancel()
 * @see com.bigbross.bossa.wfnet.Case#cancel(Activity)
 */
class CancelActivity extends WFNetCommand {

    private String caseTypeId;
    private int caseId;
    private int activityId;
    
    /**
     * Creates a new cancel operation. <p>
     * 
     * @param activity the activity to be canceled.
     */    
    CancelActivity(Activity activity) {
        this.activityId = activity.getId();
        this.caseId = activity.getCase().getId();
        this.caseTypeId = activity.getCaseType().getId();
    }

    /**
     * Executes the operation. <p>
     * 
     * @exception EvaluationException if an expression evaluation error
     *            occurs. If this exception is thrown the state of the case
     *            may be left inconsistent.
     * @see com.bigbross.bossa.wfnet.WFNetCommand#execute(CaseTypeManager)
     */
    protected Serializable execute(CaseTypeManager caseTypeManager) 
        throws EvaluationException {
    
        Case caze = caseTypeManager.getCaseType(caseTypeId).getCase(caseId);
        Activity activity = caze.getActivity(activityId);
        
        boolean result = caze.cancel(activity);
        
        return new Boolean(result); /* FIXME: Ridiculous, isn't? */ 
    }
}
