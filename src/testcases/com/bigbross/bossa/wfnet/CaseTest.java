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
        return new CaseTypeTest("hohoho").createTestCaseType().
                newCase(new int[] {1,0,0,0,0,0,0,0});
    }

    private boolean fire(Case caze, Transition t) {
	Activity act = caze.getWorkItems()[t.index].open();
	if (act != null) {
	    return act.close();
	}
	return false;
    }

    private boolean sameState(int[] s1, int[] s2) {
        assertEquals(s1.length, s2.length);
        for (int i = 0; i < s1.length; i++) {
            assertEquals(s1[i], s2[i]);
        }
        return true;
    } 

    public void testFirstShot() {
        System.out.println("First Shot:");

        Case caze = newTestCase();

        int[] expected = new int[] {0,1,0,0,0,0,0,0};

        assertTrue(fire(caze, caze.getCaseType().getTransition("a")));

        int[] actual = (int[])caze.marking.clone();

        assertTrue(sameState(expected, actual));
    }

    public void testInvalidShot() {
	System.out.println("Invalid Shot:");

        Case caze = newTestCase();
        
        int[] start = (int[])caze.marking.clone();
        
        assertFalse(fire(caze, caze.getCaseType().getTransition("b")));

        int[] end = (int[])caze.marking.clone();

        assertTrue(sameState(start, end));
    }

    public void testRollback() {
        System.out.println("Rollback:");

        Case caze = newTestCase();
        
        int[] start = (int[])caze.marking.clone();

        int index = caze.getCaseType().getTransition("a").index;
        Activity act = caze.getWorkItems()[index].open();
        assertNotNull(act);
        assertTrue(act.cancel());

        int[] end = (int[])caze.marking.clone();

        assertTrue(sameState(start, end));
    }

    public void testMachineGun() {
	System.out.println("Machine Gun:");

        Case caze = newTestCase();

        int[] expected = new int[] {0,2,0,0,1,0,1,2};

        assertTrue(fire(caze, caze.getCaseType().getTransition("a")));
        assertTrue(fire(caze, caze.getCaseType().getTransition("b")));
        assertTrue(fire(caze, caze.getCaseType().getTransition("c")));
        assertTrue(fire(caze, caze.getCaseType().getTransition("d")));
        assertTrue(fire(caze, caze.getCaseType().getTransition("e")));
        assertTrue(fire(caze, caze.getCaseType().getTransition("f")));

        int[] actual = (int[])caze.marking.clone();

        assertTrue(sameState(expected, actual));
    }

    public void testToString() {

        String expected = "\t1\t0\t0\t0\t0\t0\t0\t0\t";
        
        String result = newTestCase().toString();

        assertEquals(expected, result);
    }
}
