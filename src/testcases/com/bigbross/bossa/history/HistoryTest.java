/*
 * Bossa Workflow System
 *
 * $Id$
 *
 * Copyright (C) 2003,2004 OpenBR Sistemas S/C Ltda.
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

import java.util.List;

import junit.framework.TestCase;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaException;
import com.bigbross.bossa.BossaFactory;
import com.bigbross.bossa.BossaTestUtil;
import com.bigbross.bossa.resource.Resource;
import com.bigbross.bossa.wfnet.WorkItem;
import com.bigbross.bossa.work.WorkManager;

public class HistoryTest extends TestCase {

    private Resource frank, sally;
    private WorkManager workManager;
    private Historian historian;

    public HistoryTest(String name) {
	super(name);
    }

    protected void setUp() {
    }

    public void testHistory() throws BossaException {
        Bossa bossa = BossaTestUtil.createCompleteTestBossa();
        frank = bossa.getResourceManager().getResource("frank");
        sally = bossa.getResourceManager().getResource("sally");
        workManager = bossa.getWorkManager();
        historian = bossa.getHistorian();

        WorkItem wi = (WorkItem) workManager.getWorkItems(frank, true).get(0);
        wi.open(frank).close();
        wi = (WorkItem) workManager.getWorkItems(sally).get(0);
        wi.open(sally).close();
        wi = (WorkItem) workManager.getWorkItems(frank).get(0);
        wi.open(frank).close();

        List events = historian.getHistory();
        assertEquals(29, events.size());
    }

    public void testNoHistory() throws BossaException {
        BossaFactory factory = new BossaFactory();
        factory.setTransientBossa(true);
        factory.setActiveHistorian(false);
        Bossa bossa = factory.createBossa();
        BossaTestUtil.setupTestBossa(bossa);
        frank = bossa.getResourceManager().getResource("frank");
        workManager = bossa.getWorkManager();
        historian = bossa.getHistorian();

        WorkItem wi = (WorkItem) workManager.getWorkItems(frank, true).get(0);
        wi.open(frank).close();

        List events = historian.getHistory();
        assertEquals(0, events.size());
    }
}
