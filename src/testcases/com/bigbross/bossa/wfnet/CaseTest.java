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

    static boolean sameState(int[] s1, int[] s2) {
        assertEquals(s1.length, s2.length);
        for (int i = 0; i < s1.length; i++) {
            assertEquals(s1[i], s2[i]);
        }
        return true;
    } 

    public void testGetWorkItem() throws Exception {
        Case caze = WFNetUtil.createCase();
        
        WorkItem wi = caze.getWorkItem("a");
        assertNotNull(wi);
        assertEquals("a", wi.getId());
        assertNull(caze.getWorkItem("invalid id"));
    }

    public void testOpenClose() throws Exception {
        Case caze = WFNetUtil.createCase();

        int[] expected = {0,1,0,0,0,0,0,0};

        assertTrue(WFNetUtil.fire(caze, "a", null));

        int[] actual = caze.getMarking();

        assertTrue(CaseTest.sameState(expected, actual));
    }

    public void testInvalidOpen() throws Exception {
        Case caze = WFNetUtil.createCase();
        
        int[] start = caze.getMarking();
        
        assertFalse(WFNetUtil.fire(caze, "b", null));

        int[] end = caze.getMarking();

        assertTrue(CaseTest.sameState(start, end));
    }

    public void testOpenCancel() throws Exception {
        Case caze = WFNetUtil.createCase();
        
        int[] start = caze.getMarking();

        Activity act = caze.open(caze.getWorkItem("a"), jdoe);
        assertNotNull(act);
        assertTrue(caze.cancel(act));

        int[] end = caze.getMarking();

        assertTrue(CaseTest.sameState(start, end));
    }
    
    public void testInvalidCloseCancel() throws Exception {
        Case caze = WFNetUtil.createCase();
        
        int[] expected = {0,1,0,0,0,0,0,0};
        Activity act = caze.open(caze.getWorkItem("a"), jdoe);
        assertNotNull(act);
        assertTrue(caze.close(act, null));
        
        int[] actual = caze.getMarking();
        assertTrue(CaseTest.sameState(expected, actual));
        
        assertFalse(caze.close(act, null));
        actual = caze.getMarking();
        assertTrue(CaseTest.sameState(expected, actual));
        
        assertFalse(caze.cancel(act));
        actual = caze.getMarking();
        assertTrue(CaseTest.sameState(expected, actual));
    }

    public void testMachineGun() throws Exception {
        Case caze = WFNetUtil.createCase();
        HashMap attributes = new HashMap();
	attributes.put("SOK", new Boolean(true));
        attributes.put("DIR", new Boolean(true));
        attributes.put("ADIR", "OK");

        int[] expected = {0,0,0,0,0,0,1,0};

        assertTrue(WFNetUtil.fire(caze, "a", attributes));
        assertTrue(WFNetUtil.fire(caze, "b", null));
        assertTrue(WFNetUtil.fire(caze, "c", null));
        assertTrue(WFNetUtil.fire(caze, "d", null));
        assertTrue(WFNetUtil.fire(caze, "e", null));

        int[] actual = caze.getMarking();

        assertTrue(sameState(expected, actual));
    }

    public void testSelfLoop() throws Exception {
        CaseType caseType = new CaseType("selfloop");
        Place A = caseType.registerPlace("A", 1);
        Place B = caseType.registerPlace("B");
        Transition a = caseType.registerTransition("a", "joedoe");
        a.input(A,  "1");
        a.output(B, "1");
        a.output(A, "1");
        caseType.buildTemplate(null);
        Case caze = caseType.openCase();
        
        assertTrue(WFNetUtil.fire(caze, "a", null));
        assertTrue(sameState(new int[] {1,1}, caze.getMarking()));
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
        assertNotNull(caze.getWorkItem("a"));
        assertNotNull(caze.getWorkItem("b"));
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
        
        Case caze = caseType.openCase();
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

    public void testToString() throws Exception {

        String expected = "\t1\t0\t0\t0\t0\t0\t0\t0\t";
        
        String result = WFNetUtil.createCase().toString();

        assertEquals(expected, result);
    }
}
