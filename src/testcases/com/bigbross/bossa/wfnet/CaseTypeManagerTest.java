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

package com.bigbross.bossa.wfnet;

import java.util.List;

import junit.framework.TestCase;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaFactory;
import com.bigbross.bossa.BossaTestUtil;
import com.bigbross.bossa.resource.Resource;
import com.bigbross.bossa.resource.ResourceManager;
import com.bigbross.bossa.resource.ResourceRegistry;
import com.bigbross.bossa.resource.ResourceUtil;

public class CaseTypeManagerTest extends TestCase {

    static final Resource joe = ResourceUtil.createResource("joe");

    CaseTypeManager caseTypeManager;

    public CaseTypeManagerTest(String name) {
        super(name);
    }

    protected void setUp() {
        caseTypeManager = new CaseTypeManager();
    }

    public void testRegisterCaseType() throws Exception {
        assertTrue(caseTypeManager.registerCaseTypeImpl(
                        BossaTestUtil.createCaseType("test1")));
        assertFalse(caseTypeManager.registerCaseTypeImpl(
                        BossaTestUtil.createCaseType("test1")));
    }

    public void testQueryCaseType() throws Exception {
        CaseType expected = BossaTestUtil.createCaseType("test1");

        assertTrue(caseTypeManager.registerCaseTypeImpl(expected));
        assertSame(expected, caseTypeManager.getCaseType("test1"));
    }

    public void testQueryAllCaseTypes() throws Exception {
        CaseType ct1 = BossaTestUtil.createCaseType("test1");
        CaseType ct2 = BossaTestUtil.createCaseType("test2");

        assertTrue(caseTypeManager.registerCaseTypeImpl(ct1));
        assertTrue(caseTypeManager.registerCaseTypeImpl(ct2));

        List l = caseTypeManager.getCaseTypes();
        assertEquals(2, l.size());
        String id = ((CaseType) l.get(0)).getId();
        assertTrue(id.equals("test1") || id.equals("test2"));
        id = ((CaseType) l.get(1)).getId();
        assertTrue(id.equals("test1") || id.equals("test2"));
    }

    public void testRemoveCaseType() throws Exception {
        CaseType expected = BossaTestUtil.createCaseType("test1");

        assertTrue(caseTypeManager.registerCaseTypeImpl(expected));
        caseTypeManager.removeCaseTypeImpl("test1");
        assertNull(caseTypeManager.getCaseType("test1"));
        assertNull(expected.getCaseTypeManager());
    }

    public void testGetWorkItems() throws Exception {
        CaseType ct1 = BossaTestUtil.createCaseType("test1");
        CaseType ct2 = BossaTestUtil.createCaseType("test2");
        assertTrue(caseTypeManager.registerCaseTypeImpl(ct1));
        assertTrue(caseTypeManager.registerCaseTypeImpl(ct2));

        assertEquals(0, caseTypeManager.getWorkItems().size());
        assertEquals(2, caseTypeManager.getWorkItems(true).size());
    }

    public void testGetActivities() throws Exception {
        CaseType ct1 = BossaTestUtil.createCaseType("test1");
        CaseType ct2 = BossaTestUtil.createCaseType("test2");
        assertTrue(caseTypeManager.registerCaseTypeImpl(ct1));
        assertTrue(caseTypeManager.registerCaseTypeImpl(ct2));
        WorkItem wi = (WorkItem) caseTypeManager.getWorkItems(true).get(0);
        wi.getCase().open(wi, joe);

        assertEquals(1, caseTypeManager.getActivities().size());
    }

    public void testResourceRegistryManagement() throws Exception {
        Bossa bossa = BossaFactory.transientBossa();
        ResourceManager rm = bossa.getResourceManager();
        CaseTypeManager ctm = bossa.getCaseTypeManager();
        CaseType ct = BossaTestUtil.createCaseType("test");
        ResourceRegistry testRegistry = new ResourceRegistry(ct.getId());

        ctm.registerCaseTypeImpl(ct);
        assertFalse(rm.registerSubContext(testRegistry));
        ctm.removeCaseTypeImpl(ct.getId());
        assertTrue(rm.registerSubContext(testRegistry));
    }
}
