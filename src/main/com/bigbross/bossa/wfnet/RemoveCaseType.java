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
 * This class implements the remove case type operation of
 * <code>CaseTypeManager</code> through the prevalence subsystem. <p>
 * 
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 * @see com.bigbross.bossa.wfnet.CaseTypeManager#removeCaseType(String)
 * @see com.bigbross.bossa.wfnet.CaseTypeManager#removeCaseTypeImpl(String)
 */
class RemoveCaseType extends WFNetCommand {

    private String caseTypeId;
    
    RemoveCaseType(String id) {
        this.caseTypeId = id;
    }

    /**
     * @see com.bigbross.bossa.wfnet.WFNetCommand#execute(CaseTypeManager)
     */
    protected Serializable execute(CaseTypeManager caseTypeManager) {
        caseTypeManager.removeCaseTypeImpl(caseTypeId);
        return null;
    }
}
