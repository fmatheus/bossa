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

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaException;
import com.bigbross.bossa.BossaTransaction;


/**
 * This class represents all transactions applied to the WFNet persistent
 * objects. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
abstract class WFNetTransaction extends BossaTransaction {

    /**
     * @see BossaTransaction#execute(Bossa)
     */
    public Object execute(Bossa bossa) throws BossaException {
        return execute(bossa.getCaseTypeManager());
    }

    /**
     * Executes a transaction in a case type manager. <p>
     *
     * @param caseTypeManager the case type manager.
     * @return the value returned by the transaction.
     * @exception BossaException if the transaction throws an exception.
     */
    protected abstract Object execute(CaseTypeManager caseTypeManager)
        throws BossaException;
}
