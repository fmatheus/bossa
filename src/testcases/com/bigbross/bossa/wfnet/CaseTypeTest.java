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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.bigbross.bossa.BossaException;
import com.bigbross.bossa.BossaTestUtil;
import com.bigbross.bossa.resource.Resource;
import com.bigbross.bossa.resource.ResourceRegistry;
import com.bigbross.bossa.resource.ResourceUtil;

public class CaseTypeTest extends TestCase {

    public CaseTypeTest(String name) {
        super(name);
    }

    protected void setUp() {
    }

    public void testCreation() throws Exception {
        CaseType caseType = BossaTestUtil.createCaseType("test");
        assertNotNull(caseType);
    }

    public void testPlace() throws Exception {
        CaseType caseType = BossaTestUtil.createCaseType("test");
        Place A = caseType.getPlace("A");
        Place B = caseType.getPlace("B");

        assertEquals(1, A.getInitialMarking());
        assertEquals(0, B.getInitialMarking());
    }

    public void testTransition() throws BossaException {
        CaseType caseType = new CaseType("transitions");
        caseType.registerTransition("a", "bosses");
        caseType.registerTransition("b", "", 0);
        caseType.registerTransition("c", null, 1);
        caseType.buildTemplate(null);

        Transition t = caseType.getTransition("a");
        assertEquals("bosses", t.getResource().toString());
        assertEquals(-1, t.getTimeout());
        t = caseType.getTransition("b");
        assertNull(t.getResource());
        assertEquals(0, t.getTimeout());
        t = caseType.getTransition("c");
        assertNull(t.getResource());
        assertEquals(1, t.getTimeout());
    }

    public void testEdge() throws Exception {
        CaseType caseType = BossaTestUtil.createCaseType("test");
        Transition a = caseType.getTransition("a");
        Transition b = caseType.getTransition("b");

        assertEquals("-1", a.getInputEdges().get(0).toString());
        assertEquals( "1", a.getOutputEdges().get(0).toString());

        assertEquals("-1", b.getInputEdges().get(0).toString());
        assertEquals("!SOK", b.getOutputEdges().get(0).toString());
        assertEquals( "SOK && DIR", b.getOutputEdges().get(1).toString());
        assertEquals( "SOK && !DIR", b.getOutputEdges().get(2).toString());
    }

    public void testTemplate() throws Exception {
        CaseType caseType = BossaTestUtil.createCaseType("test");

        List workItems = caseType.getWorkItems(true);
        assertEquals(1, workItems.size());
        Case template = ((WorkItem) workItems.get(0)).getCase();
        assertNotNull(template);
        assertEquals(0, template.getId());
        Map expected = new HashMap();
        expected.put("A", new Integer(1));
        CaseTest.sameState(expected, template.getState());
        assertEquals(1, template.getWorkItems().size());
        assertEquals("a",
                     ((WorkItem) template.getWorkItems().get(0)).getId());
    }

    public void testMissingInitialValues() {
        CaseType caseType = new CaseType("missing");
        Place A = caseType.registerPlace("A", 1);
        Place B = caseType.registerPlace("B");
        Transition a = caseType.registerTransition("a", "boss");
        a.input(A,  "1");
        a.output(B, "FOO");

        try {
            caseType.buildTemplate(null);
            fail("Undetected undeclared attribute.");
        } catch (BossaException e) {
        }
    }

    public void testOpenCase() throws Exception {
        CaseType caseType = BossaTestUtil.createCaseType("test_open");
        Case caze = caseType.openCaseImpl(null);
        assertNotNull(caze);
        assertSame(caze, caseType.getCase(1));
        CaseTest.sameState(caseType.getCase(0).getState(), caze.getState());
        assertEquals(caseType.getCase(0).getAttributes(), caze.getAttributes());
    }

    public void testOpenCaseState() throws Exception {
        CaseType caseType = BossaTestUtil.createCaseType("test_open");
        Map state = new HashMap();
        state.put("A", new Integer(0));
        state.put("B", new Integer(0));
        state.put("C", new Integer(1));
        state.put("D", new Integer(0));
        state.put("E", new Integer(1));
        state.put("F", new Integer(0));
        state.put("G", new Integer(2));
        state.put("H", new Integer(0));
        Case caze = caseType.openCaseImpl(state);
        assertNotNull(caze);
        assertSame(caze, caseType.getCase(1));
        CaseTest.sameState(state, caze.getState());
        assertEquals(caseType.getCase(0).getAttributes(), caze.getAttributes());
    }

    public void testCloseCase() throws Exception {
        Case caze = WFNetUtil.createCase(new int[] {0,0,0,0,1,0,0,0});
        CaseType caseType = caze.getCaseType();
        assertSame(caze, caseType.getCase(1));
        assertTrue(WFNetUtil.fire(caze, "d", null));
        assertSame(caze, caseType.getCase(1));
        assertTrue(WFNetUtil.fire(caze, "e", null));
        assertNull(caseType.getCase(1));
    }

    public void testGetCase() throws Exception {
        CaseType caseType = BossaTestUtil.createCaseType("test");
        Case caze = null;
        caze = caseType.openCaseImpl(null);
        assertSame(caze, caseType.getCase(caze.getId()));
    }

    public void testGetAllCases() throws Exception {
        CaseType caseType = BossaTestUtil.createCaseType("test");
        caseType.openCaseImpl(null);
        caseType.openCaseImpl(null);

        List l = caseType.getCases();
        assertEquals(2, l.size());
        int id = ((Case) l.get(0)).getId();
        assertTrue(id == 1 || id == 2);
        id = ((Case) l.get(1)).getId();
        assertTrue(id == 1 || id == 2);
    }

    public void testGetWorkItems() throws Exception {
        CaseType caseType = BossaTestUtil.createCaseType("test");
        Case c1 = null;
        Case c2 = null;
        c1 = caseType.openCaseImpl(null);
        c2 = caseType.openCaseImpl(null);

        List wi = caseType.getWorkItems();
        assertEquals(2, wi.size());
        WorkItem wi1 = (WorkItem) wi.get(0);
        WorkItem wi2 = (WorkItem) wi.get(1);
        assertFalse(wi1.getCase().getId() == wi2.getCase().getId());
        assertTrue(c1.getId() == wi1.getCase().getId() ||
                   c1.getId() == wi2.getCase().getId());
        assertTrue(c2.getId() == wi1.getCase().getId() ||
                   c2.getId() == wi2.getCase().getId());
    }

    public void testGetActivities() throws Exception {
        CaseType caseType = BossaTestUtil.createCaseType("test");
        Resource jdoe = ResourceUtil.createResource("jdoe");
        Case c1 = null;
        Case c2 = null;
        c1 = caseType.openCaseImpl(null);
        c2 = caseType.openCaseImpl(null);
        c1.open(c1.getWorkItem("a"), jdoe);
        c2.open(c2.getWorkItem("a"), jdoe);

        List activities = caseType.getActivities();
        assertEquals(2, activities.size());
        Activity a1 = (Activity) activities.get(0);
        Activity a2 = (Activity) activities.get(1);
        assertFalse(a1.getCase().getId() == a2.getCase().getId());
        assertTrue(c1.getId() == a1.getCase().getId() ||
                   c1.getId() == a2.getCase().getId());
        assertTrue(c2.getId() == a1.getCase().getId() ||
                   c2.getId() == a2.getCase().getId());
        assertEquals("a", a1.getWorkItemId());
        assertEquals("a", a2.getWorkItemId());
    }

    public void testResourceRegistryManagement() throws Exception {
        Case caze = WFNetUtil.createCase(new int[] {0,0,0,0,1,0,0,0});
        ResourceRegistry caseTypeRegistry =
            caze.getCaseType().getResourceRegistry();
        ResourceRegistry testRegistry =
            new ResourceRegistry(Integer.toString(caze.getId()));

        assertFalse(caseTypeRegistry.registerSubContext(testRegistry));
        assertTrue(WFNetUtil.fire(caze, "d", null));
        assertTrue(WFNetUtil.fire(caze, "e", null));
        assertTrue(caseTypeRegistry.registerSubContext(testRegistry));
    }
}
