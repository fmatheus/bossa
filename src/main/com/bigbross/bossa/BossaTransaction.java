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

package com.bigbross.bossa;

import java.util.Date;

import org.prevayler.TransactionWithQuery;

import com.bigbross.bossa.Bossa;


/**
 * This class represents all transactions applied to a Bossa engine. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public abstract class BossaTransaction implements TransactionWithQuery {

    /**
     * Executes a transaction in a prevalent system. <p>
     *
     * This method sets the engine time source to the time
     * provided. It should only be called by the prevayler instance
     * providing persistence to the engine. <p>
     *
     * @see org.prevayler.TransactionWithQuery#executeAndQuery(Object, Date)
     */
    public Object executeAndQuery(Object system, Date time) throws Exception {
        Bossa bossa = (Bossa) system;
        ((DeterministicTimeSource) bossa.getTimeSource()).setTime(time);
        return execute(bossa);
    }

    /**
     * Executes a transaction in a Bossa engine. <p>
     *
     * This method uses the time currently set in the engine time source. <p>
     *
     * @param bossa the Bossa engine.
     * @return the value returned by the transaction.
     * @exception BossaException if a Bossa error occurs.
     */
    public abstract Object execute(Bossa bossa) throws BossaException;
}
