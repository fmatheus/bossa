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

package com.bigbross.bossa.history;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import com.bigbross.bossa.notify.Event;

public class TransactionsTest extends TestCase {

    private Historian historian;

    public TransactionsTest(String name) {
        super(name);
    }

    protected void setUp() {
        historian = new Historian(null);
        HashMap attributes = new HashMap();
        Date aTime = new Date();
        aTime.setTime(HistorianTest.t1);
        historian.newEvent(new Event("teste0", Event.WFNET_EVENT,
                                     attributes, aTime));
        aTime = new Date();
        aTime.setTime(HistorianTest.t2);
        historian.newEvent(new Event("teste1", Event.WFNET_EVENT,
                                     attributes, aTime));
        aTime = new Date();
        aTime.setTime(HistorianTest.t3);
        historian.newEvent(new Event("teste2", Event.WFNET_EVENT,
                                     attributes, aTime));
        aTime = new Date();
        aTime.setTime(HistorianTest.t4);
        historian.newEvent(new Event("teste3", Event.WFNET_EVENT,
                                     attributes, aTime));
    }


    public void testPurgeHistory() {
        Date end = new Date();
        end.setTime(HistorianTest.t3);
        PurgeHistory transaction = new PurgeHistory(end);
        transaction.execute(historian);
        List events = historian.getHistory();
        assertEquals(2, events.size());
    }
}
