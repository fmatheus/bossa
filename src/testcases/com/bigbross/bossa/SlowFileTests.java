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

package com.bigbross.bossa;

import java.io.File;

import junit.framework.TestCase;

import com.bigbross.bossa.resource.Resource;
import com.bigbross.bossa.wfnet.WorkItem;

public class SlowFileTests extends TestCase {

    private String testDirName;
    private File testDir;

    public SlowFileTests(String name) {
	super(name);
        testDirName = "build/TestDir";
        testDir = new File(testDirName);
    }

    private boolean createTestDir() {
        return testDir.mkdir();
    }
    
    private boolean removeTestDir() {
        if (testDir.exists()) {
            File[] contents = testDir.listFiles();
            for (int i = 0; i < contents.length; i++) {
                if (!contents[i].delete()) {
                    return false;
                }
            }
            return testDir.delete();
        } else {
            return true;
        }
    }

    protected void setUp() {
        assertTrue(removeTestDir());
        assertTrue(createTestDir());
    }
    
    protected void tearDown() {
        assertTrue(removeTestDir());
    }

    public void testPersistentBossa() throws Exception {
        BossaFactory factory = new BossaFactory();
        factory.setStateDir(testDirName);
        Bossa bossa = factory.createBossa();
        BossaTestUtil.setupTestBossa(bossa);        

        Resource r = bossa.getResourceManager().getResource("frank");
        WorkItem wi = (WorkItem)
            bossa.getWorkManager().getWorkItems(r, true).get(0);
        wi.open(r).close();
        
        bossa = null; r = null; wi = null;
        bossa = factory.createBossa();
        
        assertEquals(1, bossa.getCaseTypeManager().getCaseTypes().size());
        assertEquals(3, bossa.getResourceManager().getResources().size());
        assertEquals(1,
            bossa.getCaseTypeManager().getCaseType("test").getCases().size());
        assertEquals(1, bossa.getCaseTypeManager().getWorkItems().size());
        
        r = bossa.getResourceManager().getResource("sally");
        wi = (WorkItem) bossa.getWorkManager().getWorkItems(r).get(0);
        wi.open(r).close();
        bossa.takeSnapshot();

        bossa = null; r = null; wi = null;
        bossa = factory.createBossa();

        r = bossa.getResourceManager().getResource("frank");
        wi = (WorkItem) bossa.getWorkManager().getWorkItems(r).get(0);
        wi.open(r).close();

        assertEquals(0,
            bossa.getCaseTypeManager().getCaseType("test").getCases().size());
    }
}
