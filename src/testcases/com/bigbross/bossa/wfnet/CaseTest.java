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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.bigbross.bossa.BossaException;
import com.bigbross.bossa.BossaTestUtil;
import com.bigbross.bossa.resource.Resource;
import com.bigbross.bossa.resource.ResourceUtil;

public class CaseTest extends TestCase {

    private Resource jdoe;

    public CaseTest(String name) {
	super(name);
    }

    protected void setUp() {
        jdoe = ResourceUtil.createResource("jdoe");
    }

    static void sameState(Map expected, Map actual) {
        int matches = 0;
        for (Iterator i = actual.keySet().iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            if (expected.get(key) != null) {
                assertEquals(expected.get(key), actual.get(key));
                matches++;
            } else {
                assertEquals(new Integer(0), actual.get(key));
            }
        }
        assertEquals(matches, expected.size());
    }

    public void testGetWorkItem() throws Exception {
        Case caze = WFNetUtil.createCase();

        WorkItem wi = caze.getWorkItem("a");
        assertNotNull(wi);
        assertEquals("a", wi.getId());
        assertNull(caze.getWorkItem("invalid id"));
    }

    public void testGetState() throws Exception {
        Case caze = WFNetUtil.createCase();

        Map expected = new HashMap();
        expected.put("A", new Integer(1));
        expected.put("B", new Integer(0));
        expected.put("C", new Integer(0));
        expected.put("D", new Integer(0));
        expected.put("E", new Integer(0));
        expected.put("F", new Integer(0));
        expected.put("G", new Integer(0));
        expected.put("H", new Integer(0));

        CaseTest.sameState(expected, caze.getState());
    }

    public void testOpenClose() throws Exception {
        Case caze = WFNetUtil.createCase();

        Map expected = new HashMap();
        expected.put("B", new Integer(1));

        assertTrue(WFNetUtil.fire(caze, "a", null));

        CaseTest.sameState(expected, caze.getState());
    }

    public void testInvalidOpen() throws Exception {
        Case caze = WFNetUtil.createCase();

        Map start = caze.getState();

        assertFalse(WFNetUtil.fire(caze, "b", null));

        CaseTest.sameState(start, caze.getState());
    }

    public void testOpenCancel() throws Exception {
        Case caze = WFNetUtil.createCase();

        Map start = caze.getState();

        Activity act = caze.open(caze.getWorkItem("a"), jdoe);
        assertNotNull(act);
        assertTrue(caze.cancel(act));

        CaseTest.sameState(start, caze.getState());
    }

    public void testInvalidCloseCancel() throws Exception {
        Case caze = WFNetUtil.createCase();

        Map expected = new HashMap();
        expected.put("B", new Integer(1));
        Activity act = caze.open(caze.getWorkItem("a"), jdoe);
        assertNotNull(act);
        assertTrue(caze.close(act, null));

        CaseTest.sameState(expected, caze.getState());

        assertFalse(caze.close(act, null));
        CaseTest.sameState(expected, caze.getState());

        assertFalse(caze.cancel(act));
        CaseTest.sameState(expected, caze.getState());
    }

    public void testAttributes() throws Exception {
        Case caze = WFNetUtil.createCase();
        Map attributes = caze.getAttributes();

        assertEquals(4, attributes.size());
        assertEquals(new Boolean(false), attributes.get("SOK"));
        assertEquals(new Boolean(false), attributes.get("DIR"));
        assertEquals("", attributes.get("ADIR"));
        assertEquals(new Boolean(false), attributes.get("OK"));
    }

    public void testEdgeEvaluation() throws Exception {
        Case caze = WFNetUtil.createCase();
        caze.declare("SOK", new Boolean(true));
        caze.declare("DIR", new Boolean(false));
        caze.declare("AVL", new Integer(3));

        Edge e1 = Edge.newOutput(null, "AVL * SOK || DIR");
        Edge e2 = Edge.newInput(null, "AVL * SOK && DIR");

        assertEquals(3, e1.eval(caze));
        assertEquals(0, e2.eval(caze));

        e1 = Edge.newInput(null, "AVL * XXX && DIR");
        try {
            e1.eval(caze);
            fail("Undetected undeclared attribute.");
        } catch (EvaluationException e) {
        }
    }

    public void testEdgeOrientation() throws Exception {
        Case caze = WFNetUtil.createCase();

        Edge output = Edge.newOutput(null, "1");
        Edge input = Edge.newInput(null, "2");

        assertEquals(1, output.output(caze));
        assertEquals(2, input.input(caze));
        assertEquals(0, output.input(caze));
        assertEquals(0, input.output(caze));
    }

    public void testMachineGun() throws Exception {
        Case caze = WFNetUtil.createCase();
        HashMap attributes = new HashMap();
	attributes.put("SOK", new Boolean(true));
        attributes.put("DIR", new Boolean(true));
        attributes.put("ADIR", "OK");

        Map expected = new HashMap();
        expected.put("G", new Integer(1));

        assertTrue(WFNetUtil.fire(caze, "a", attributes));
        assertTrue(WFNetUtil.fire(caze, "b", null));
        assertTrue(WFNetUtil.fire(caze, "c", null));
        assertTrue(WFNetUtil.fire(caze, "d", null));
        assertTrue(WFNetUtil.fire(caze, "e", null));

        sameState(expected, caze.getState());
    }

    public void testSelfLoop() throws Exception {
        CaseType caseType = new CaseType("selfloop");
        Place A = caseType.registerPlace("A", 1);
        Place B = caseType.registerPlace("B");
        Transition a = caseType.registerTransition("a", "boss");
        a.input(A,  "1");
        a.output(B, "1");
        a.output(A, "1");
        caseType.buildTemplate(null);
        Case caze = caseType.openCaseImpl(null);

        Map expected = new HashMap();
        expected.put("A", new Integer(1));
        expected.put("B", new Integer(1));

        assertTrue(WFNetUtil.fire(caze, "a", null));

        sameState(expected, caze.getState());
    }

    public void testZeroTimeout() throws Exception {
        Case caze = WFNetUtil.createAutoFireCase();

        Map expected = new HashMap();
        expected.put("A", new Integer(1));
        expected.put("B", new Integer(0));
        expected.put("C", new Integer(0));
        sameState(expected, caze.getState());

        expected = new HashMap();
        expected.put("A", new Integer(0));
        expected.put("B", new Integer(0));
        expected.put("C", new Integer(1));
        assertTrue(WFNetUtil.fire(caze, "a", null));
        sameState(expected, caze.getState());

        /* Add a test to stress nested auto firing going beyond case closing. */
        assertEquals(0, caze.getCaseType().getCases().size());
    }

    public void testSetState() throws Exception {
        Case caze = WFNetUtil.createCase();

        Map newState = new HashMap();
        newState.put("A", new Integer(0));
        newState.put("B", new Integer(0));
        newState.put("C", new Integer(0));
        newState.put("D", new Integer(0));
        newState.put("E", new Integer(1));
        newState.put("F", new Integer(0));
        newState.put("G", new Integer(0));
        newState.put("H", new Integer(0));

        caze.setStateImpl(newState);
        CaseTest.sameState(newState, caze.getState());

        assertFalse(caze.getWorkItem("a").isFireable());
        List workItens = caze.getWorkItems();
        assertEquals(1, workItens.size());
        assertEquals("d", ((WorkItem) workItens.get(0)).getId());

        newState = new HashMap();
        newState.put("A", new Integer(1));

        caze.setStateImpl(newState);
        assertTrue(caze.getWorkItem("a").isFireable());
        assertEquals(2, caze.getWorkItems().size());
    }

    public void testSetStateZeroTimeout() throws Exception {
        Case caze = WFNetUtil.createAutoFireCase();

        Map newState = new HashMap();
        newState.put("A", new Integer(0));
        newState.put("B", new Integer(1));
        newState.put("C", new Integer(0));

        Map expected = new HashMap();
        expected.put("A", new Integer(0));
        expected.put("B", new Integer(0));
        expected.put("C", new Integer(1));

        caze.setStateImpl(newState);
        sameState(expected, caze.getState());
    }

    public void testAutomaticCreation() throws Exception {
        CaseType caseType = BossaTestUtil.createCaseType("test");
        WorkItem wi = (WorkItem) caseType.getWorkItems(true).get(0);
        Case template = wi.getCase();

        Activity a1 = template.open(wi, jdoe);
        Case caze = a1.getCase();
        assertEquals(1, caze.getId());

        assertTrue(caze.cancel(a1));

        Activity a2 = caze.open(caze.getWorkItem("a"), jdoe);
        assertEquals(1, a2.getCase().getId());
    }

    public void testWorkItensList() throws Exception {
        Case caze = WFNetUtil.createCase(new int[] {1,1,0,0,0,0,0,0});
        List workItens = caze.getWorkItems();
        assertEquals(2, workItens.size());

        WorkItem w0 = (WorkItem) workItens.get(0);
        WorkItem w1 = (WorkItem) workItens.get(1);
        assertNotSame(w0, w1);
        assertTrue("a".equals(w0.getId()) || "a".equals(w1.getId()));
        assertTrue("b".equals(w0.getId()) || "b".equals(w1.getId()));
    }

    public void testCanPerformWorkItem() throws BossaException {
        CaseType caseType = new CaseType("canPerform");
        Place A = caseType.registerPlace("A", 1);
        Transition a = caseType.registerTransition("a", "bosses");
        Transition b = caseType.registerTransition("b", "");
        Transition c = caseType.registerTransition("c", null);
        a.input(A,  "1");
        b.input(A,  "1");
        c.input(A,  "1");
        caseType.buildTemplate(null);
        Resource mary = ResourceUtil.createResource("mary");
        Resource bosses = (Resource) caseType.getResources().get(0);
        assertEquals("bosses", bosses.getId());
        bosses.includeImpl(jdoe, false);

        Case caze = caseType.openCaseImpl(null);
        assertTrue(caze.getWorkItem("a").canBePerformedBy(jdoe));
        assertFalse(caze.getWorkItem("a").canBePerformedBy(mary));
        assertTrue(caze.getWorkItem("b").canBePerformedBy(jdoe));
        assertTrue(caze.getWorkItem("c").canBePerformedBy(jdoe));
    }

    public void testGetActivity() throws Exception {
        Case caze = WFNetUtil.createCase();
        WorkItem wi = caze.getWorkItem("a");
        caze.open(wi, jdoe);

        Activity act = caze.getActivity(1);
        assertNotNull(act);
        assertEquals("a", act.getWorkItemId());
        assertNull(caze.getActivity(2));
    }

    public void testActivitiesList() throws Exception {
        Case caze = WFNetUtil.createCase(new int[] {2,0,0,0,0,0,0,0});
        WorkItem wi = caze.getWorkItem("a");

        caze.open(wi, jdoe);
        assertEquals(1, caze.getActivities().size());

        caze.open(wi, jdoe);
        List activities = caze.getActivities();
        assertEquals(2, activities.size());

        Activity a0 = (Activity) activities.get(0);
        Activity a1 = (Activity) activities.get(1);
        assertNotSame(a0, a1);
        assertSame(a0.getTransition(), a1.getTransition());
        assertEquals(jdoe, a0.getResource());
    }
}
