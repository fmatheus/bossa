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

package com.bigbross.bossa.work;

import junit.framework.TestCase;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaTestUtil;
import com.bigbross.bossa.resource.Resource;
import com.bigbross.bossa.wfnet.WorkItem;

public class WorkManagerTest extends TestCase {

    private Resource frank, sally;
    private WorkManager workManager;

    public WorkManagerTest(String name) {
	super(name);
    }

    protected void setUp() throws Exception {
        Bossa bossa = BossaTestUtil.createCompleteTestBossa();
        workManager = bossa.getWorkManager();
        frank = bossa.getResourceManager().getResource("frank");
        sally = bossa.getResourceManager().getResource("sally");
    }

    public void testWorkItemList() {
        assertEquals(0, workManager.getWorkItems(frank).size());
        assertEquals(1, workManager.getWorkItems(frank, true).size());
    }

    public void testMetaResource() throws Exception {
        WorkItem work = (WorkItem) workManager.getWorkItems(frank, true).get(0);
        assertTrue(work.open(frank).close());
        assertEquals(0, workManager.getWorkItems(frank).size());

        work = (WorkItem) workManager.getWorkItems(sally).get(0);
        assertTrue(work.open(sally).close());
        assertEquals(1, workManager.getWorkItems(frank).size());
        assertEquals(0, workManager.getWorkItems(sally).size());
    }

    public void testActivitiesList() throws Exception {
        WorkItem work = (WorkItem) workManager.getWorkItems(frank, true).get(0);
        work.open(frank);
        assertEquals(1, workManager.getActivities(frank).size());
    }
}
