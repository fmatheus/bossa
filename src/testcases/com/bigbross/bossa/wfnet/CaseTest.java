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

package com.bigbross.bossa.wfnet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class CaseTest extends TestCase {

    public CaseTest(String name) {
	super(name);
    }

    protected void setUp() {
	System.out.println("Setting up a case test.");
    }

    Case newTestCase() {
        return CaseTypeTest.createTestCaseType().
	    newCase(new int[] {1,0,0,0,0,0,0,0});
    }

    static boolean sameState(int[] s1, int[] s2) {
        assertEquals(s1.length, s2.length);
        for (int i = 0; i < s1.length; i++) {
            assertEquals(s1[i], s2[i]);
        }
        return true;
    } 

    private boolean fire(Case caze, String workItemId, Map attributes) {
	Activity act = caze.open(caze.getWorkItem(workItemId), "jdoe");
	if (act != null) {
	    return caze.close(act, attributes);
	}
	return false;
    }

    public void testFirstShot() {
        Case caze = newTestCase();

        int[] expected = new int[] {0,1,0,0,0,0,0,0};

        assertTrue(fire(caze, "a", null));

        int[] actual = caze.getMarking();

        assertTrue(CaseTest.sameState(expected, actual));
    }

    public void testInvalidShot() {
        Case caze = newTestCase();
        
        int[] start = caze.getMarking();
        
        assertFalse(fire(caze, "b", null));

        int[] end = caze.getMarking();

        assertTrue(CaseTest.sameState(start, end));
    }

    public void testRollback() {
        Case caze = newTestCase();
        
        int[] start = caze.getMarking();

        Activity act = caze.open(caze.getWorkItem("a"), "jdoe");
        assertNotNull(act);
        assertTrue(caze.cancel(act));

        int[] end = caze.getMarking();

        assertTrue(CaseTest.sameState(start, end));
    }

    public void testMachineGun() {
        Case caze = newTestCase();
        HashMap attributes = new HashMap();
	attributes.put("SOK", new Boolean(true));
        attributes.put("DIR", new Boolean(true));
        attributes.put("ADIR", "OK");

        int[] expected = new int[] {0,0,0,0,0,0,1,0};

        assertTrue(fire(caze, "a", attributes));
        assertTrue(fire(caze, "b", null));
        assertTrue(fire(caze, "c", null));
        assertTrue(fire(caze, "d", null));
        assertTrue(fire(caze, "e", null));

        int[] actual = caze.getMarking();

        assertTrue(CaseTest.sameState(expected, actual));
    }

    public void testAutomaticCreation() {
	Case template = CaseTypeTest.createTestCaseType().getTemplate();

	Activity a1 = template.open(template.getWorkItem("a"), "jdoe");
	Case caze = a1.getCase();
	assertEquals(1, caze.getId());

        assertTrue(caze.cancel(a1));

	Activity a2 = caze.open(caze.getWorkItem("a"), "jdoe");
	assertEquals(1, a2.getCase().getId());
    }

    /**
     * This test checks if the list of work itens is correct. <p>
     */
    public void testWorkItensList() {
        Case caze = CaseTypeTest.createTestCaseType().newCase(new int[] {1,1,0,0,0,0,0,0});
        List workItens = caze.getWorkItems();
        assertEquals(2, workItens.size());

        WorkItem w0 = (WorkItem) workItens.get(0);
        WorkItem w1 = (WorkItem) workItens.get(1);
        assertNotSame(w0, w1);
        assertNotNull(caze.getWorkItem("a"));
        assertNotNull(caze.getWorkItem("b"));
    }

    public void testActivitiesList() {
        Case caze = CaseTypeTest.createTestCaseType().newCase(new int[] {2,0,0,0,0,0,0,0});
        WorkItem wi = caze.getWorkItem("a");

        caze.open(wi, "jdoe");
        assertEquals(1, caze.getActivities().size());

        caze.open(wi, "jdoe");
        List activities = caze.getActivities();
        assertEquals(2, activities.size());

        Activity a0 = (Activity) activities.get(0);
        Activity a1 = (Activity) activities.get(1);
        assertNotSame(a0, a1);
        assertSame(a0.getTransition(), a1.getTransition());
        assertEquals("jdoe", a0.getResource());
    }

    public void testEdgeEvaluation() {
        Case caze = newTestCase();
	caze.declare("SOK", new Boolean(true));
	caze.declare("DIR", new Boolean(false));
	caze.declare("AVL", new Integer(3));

	Edge e1 = Edge.newOutput("AVL * SOK || DIR");
	Edge e2 = Edge.newOutput("AVL * SOK && DIR");

	assertEquals(3, e1.eval(caze));
	assertEquals(0, e2.eval(caze));
    }

    public void testToString() {

        String expected = "\t1\t0\t0\t0\t0\t0\t0\t0\t";
        
        String result = newTestCase().toString();

        assertEquals(expected, result);
    }
}
