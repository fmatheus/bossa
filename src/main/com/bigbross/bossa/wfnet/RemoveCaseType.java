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

/**
 * This class implements the remove case type operation of
 * <code>CaseTypeManager</code> through the prevalence subsystem. <p>
 * 
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 * @see com.bigbross.bossa.wfnet.CaseTypeManager#removeCaseType(String)
 * @see com.bigbross.bossa.wfnet.CaseTypeManager#removeCaseTypeImpl(String)
 */
class RemoveCaseType extends WFNetTransaction {

    private String caseTypeId;
    
    /**
     * Creates a new remove case type operation. <p>
     * 
     * @param id the id of the case type to be removed.
     */    
    RemoveCaseType(String id) {
        this.caseTypeId = id;
    }

    /**
     * Executes the operation. <p>
     * 
     * @see com.bigbross.bossa.wfnet.WFNetTransaction#execute(
     *      CaseTypeManager, Date)
     */
    protected Object execute(CaseTypeManager caseTypeManager, Date time) {
        caseTypeManager.removeCaseTypeImpl(caseTypeId);
        return null;
    }
}
