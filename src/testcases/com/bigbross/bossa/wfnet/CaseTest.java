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

    private boolean fire(Case caze, String workItemId) {
	Activity act = caze.open(caze.getWorkItem(workItemId));
	if (act != null) {
	    return caze.close(act);
	}
	return false;
    }

    public void testFirstShot() {
        Case caze = newTestCase();

        int[] expected = new int[] {0,1,0,0,0,0,0,0};

        assertTrue(fire(caze, "a"));

        int[] actual = caze.getMarking();

        assertTrue(CaseTest.sameState(expected, actual));
    }

    public void testInvalidShot() {
        Case caze = newTestCase();
        
        int[] start = caze.getMarking();
        
        assertFalse(fire(caze, "b"));

        int[] end = caze.getMarking();

        assertTrue(CaseTest.sameState(start, end));
    }

    public void testRollback() {
        Case caze = newTestCase();
        
        int[] start = caze.getMarking();

        Activity act = caze.open(caze.getWorkItem("a"));
        assertNotNull(act);
        assertTrue(caze.cancel(act));

        int[] end = caze.getMarking();

        assertTrue(CaseTest.sameState(start, end));
    }

    public void testMachineGun() {
        Case caze = newTestCase();

        int[] expected = new int[] {0,2,0,0,1,0,1,2};

        assertTrue(fire(caze, "a"));
        assertTrue(fire(caze, "b"));
        assertTrue(fire(caze, "c"));
        assertTrue(fire(caze, "d"));
        assertTrue(fire(caze, "e"));
        assertTrue(fire(caze, "f"));

        int[] actual = caze.getMarking();

        assertTrue(CaseTest.sameState(expected, actual));
    }

    public void testAutomaticCreation() {
	Case template = CaseTypeTest.createTestCaseType().getTemplate();

	Activity a1 = template.open(template.getWorkItem("a"));
	Case caze = a1.getCase();
	assertEquals(1, caze.getId());

        assertTrue(caze.cancel(a1));

	Activity a2 = caze.open(caze.getWorkItem("a"));
	assertEquals(1, a2.getCase().getId());
    }

    /**
     * This test checks if the list of work itens is correct. <p>
     */
    public void testWorkItensList() {

        Case caze = newTestCase();

        assertTrue(fire(caze, "a"));
        assertTrue(fire(caze, "b"));

        WorkItem[] workItens = caze.getWorkItems();
        
        assertEquals(3, workItens.length);
        Transition t0 = workItens[0].getTransition();
        Transition t1 = workItens[1].getTransition();
        Transition t2 = workItens[2].getTransition();
        assertFalse(t0.getId().equals(t1.getId()));
        assertFalse(t0.getId().equals(t2.getId()));
        assertFalse(t1.getId().equals(t2.getId()));
        assertTrue(t0.getId().equals("c") || 
                   t0.getId().equals("d") ||
                   t0.getId().equals("f"));
        assertTrue(t1.getId().equals("c") || 
                   t1.getId().equals("d") ||
                   t1.getId().equals("f"));
        assertTrue(t2.getId().equals("c") || 
                   t2.getId().equals("d") ||
                   t2.getId().equals("f"));
    }

    public void testToString() {

        String expected = "\t1\t0\t0\t0\t0\t0\t0\t0\t";
        
        String result = newTestCase().toString();

        assertEquals(expected, result);
    }
}
