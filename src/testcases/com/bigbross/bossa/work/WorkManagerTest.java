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
import com.bigbross.bossa.BossaTestSuite;
import com.bigbross.bossa.resource.Resource;
import com.bigbross.bossa.resource.ResourceManager;
import com.bigbross.bossa.resource.ResourceUtil;
import com.bigbross.bossa.wfnet.CaseTypeManager;
import com.bigbross.bossa.wfnet.WFNetUtil;
import com.bigbross.bossa.wfnet.WorkItem;

public class WorkManagerTest extends TestCase {

    private Resource joe, pan;
    private WorkManager workManager;
    private CaseTypeManager caseTypeManager;
    private ResourceManager resourceManager;

    public WorkManagerTest(String name) {
	super(name);
    }

    protected void setUp() throws Exception {
	System.out.println("Setting up a work manager test.");
        Bossa bossa = BossaTestSuite.createTestBossa();
        workManager = bossa.getWorkManager();
        caseTypeManager = bossa.getCaseTypeManager();
        resourceManager = bossa.getResourceManager();
        WFNetUtil.prepareWorkTest(caseTypeManager);
        ResourceUtil.prepareWorkTest(resourceManager);
        joe = resourceManager.getResource("joe");
        pan = resourceManager.getResource("pan");
    }

    public void testWorkItemList() {
        assertEquals(0, workManager.getWorkItems(joe).size());
        assertEquals(1, workManager.getWorkItems(joe, true).size());
    }

    public void testMetaResource() throws Exception {
        WorkItem work = (WorkItem) workManager.getWorkItems(joe, true).get(0);
        assertTrue(WFNetUtil.fire(work, null, joe));
        assertEquals(0, workManager.getWorkItems(joe).size());

        work = (WorkItem) workManager.getWorkItems(pan).get(0);
        assertTrue(WFNetUtil.fire(work, null, pan));
        assertEquals(1, workManager.getWorkItems(joe).size());
        assertEquals(0, workManager.getWorkItems(pan).size());
    }

    public void testActivitiesList() throws Exception {
        WFNetUtil.createActWorkTest(caseTypeManager);
        assertEquals(1, workManager.getActivities(joe).size());
    }
}
