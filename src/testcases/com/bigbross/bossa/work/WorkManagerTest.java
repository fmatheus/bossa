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

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaTestSuite;
import com.bigbross.bossa.resource.ResourceManager;
import com.bigbross.bossa.resource.ResourceManagerTest;
import com.bigbross.bossa.wfnet.CaseType;
import com.bigbross.bossa.wfnet.CaseTypeManager;
import com.bigbross.bossa.wfnet.CaseTypeManagerTest;
import com.bigbross.bossa.wfnet.CaseTypeTest;

import junit.framework.TestCase;

public class WorkManagerTest extends TestCase {

    public WorkManagerTest(String name) {
	super(name);
    }

    protected void setUp() {
	System.out.println("Setting up a work manager test.");
    }

    public void testWorkItemList() {
        Bossa bossa = BossaTestSuite.createTestBossa();
        WorkManager workManager = bossa.getWorkManager();
        CaseTypeManager caseTypeManager = bossa.getCaseTypeManager();
        ResourceManager resourceManager = bossa.getResourceManager();
        CaseTypeManagerTest.prepareWorkTest(caseTypeManager);
        ResourceManagerTest.prepareWorkTest(resourceManager);
        assertEquals(0, workManager.getWorkItems("joe").size());
        assertEquals(1, workManager.getWorkItems("joe", true).size());
    }
}
