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

import org.prevayler.TransactionWithQuery;

import com.bigbross.bossa.Bossa;


/**
 * This class represents all transactions applied to the WFNet persistent
 * objects. <p>
 * 
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
abstract class WFNetTransaction implements TransactionWithQuery {

    /**
     * Executes a transaction in a prevalent system. <p>
     * 
     * @see org.prevayler.TransactionWithQuery#executeAndQuery(Object, Date)
     */
    public Object executeAndQuery(Object system, Date time) throws Exception {
        return execute(((Bossa) system).getCaseTypeManager(), time);
    }

    /**
     * Executes a transaction in the object tree rooted at CaseTypeManager. <p>
     */
    protected abstract Object execute(CaseTypeManager caseTypeManager,
                                        Date time)
        throws Exception;
}
