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

public class CaseTypeTest extends TestCase {

    public CaseTypeTest(String name) {
	super(name);
    }

    protected void setUp() {
    	System.out.println("Setting up a case type test.");
    }

    CaseType createTestCaseType() {
     
        CaseType caseType = new CaseType();

        Place A = caseType.registerPlace("A");
        Place B = caseType.registerPlace("B");
        Place C = caseType.registerPlace("C");
        Place D = caseType.registerPlace("D");
        Place E = caseType.registerPlace("E");
        Place F = caseType.registerPlace("F");
        Place G = caseType.registerPlace("G");
        Place H = caseType.registerPlace("H");

        Transition a = caseType.registerTransition("a", "x");
        Transition b = caseType.registerTransition("b", "y");
        Transition c = caseType.registerTransition("c", "z");
        Transition d = caseType.registerTransition("d", "y");
        Transition e = caseType.registerTransition("e", "y");
        Transition f = caseType.registerTransition("f", "x");

        a.input(A, 1);
        a.output(B, 1);

        b.input(B, 1);
        b.output(C, 1);
        b.output(D, 1);
        b.output(E, 1);

        c.input(D, 1);
        c.output(B, 1);
        c.output(E, 1);
        c.output(H, 1);

        d.input(E, 1);
        d.output(F, 1);

        e.input(F, 1);
        e.output(G, 1);

        f.input(C, 1);
        f.output(B, 1);
        f.output(H, 1);
        
        return caseType;
    }

    public void testCreation() {
        assertNotNull(createTestCaseType());
    }

    public void testToString() {
        
        String expected = "\tA\tB\tC\tD\tE\tF\tG\tH\t\n" +
                          "a\t-1\t1\t0\t0\t0\t0\t0\t0\tx\n" +
                          "b\t0\t-1\t1\t1\t1\t0\t0\t0\ty\n" +
                          "c\t0\t1\t0\t-1\t1\t0\t0\t1\tz\n" +
                          "d\t0\t0\t0\t0\t-1\t1\t0\t0\ty\n" +
                          "e\t0\t0\t0\t0\t0\t-1\t1\t0\ty\n" +
                          "f\t0\t1\t-1\t0\t0\t0\t0\t1\tx\n";

        String result = createTestCaseType().toString();

        assertEquals(expected, result);
    }

    public void testWeights() {

        CaseType caseType = createTestCaseType();
        Transition a = caseType.getTransition("a");
        Transition b = caseType.getTransition("b");
        Place A = caseType.getPlace("A");
        Place B = caseType.getPlace("B");
        Place C = caseType.getPlace("C");
        Place D = caseType.getPlace("D");
        Place E = caseType.getPlace("E");

        assertEquals(caseType.getWeight(a, A), -1);
        assertEquals(caseType.getWeight(a, B), 1);
        
        assertEquals(caseType.getWeight(b, B), -1);
        assertEquals(caseType.getWeight(b, C), 1);
        assertEquals(caseType.getWeight(b, D), 1);
        assertEquals(caseType.getWeight(b, E), 1);
    }

    public void testNewCase() {
        assertNotNull(createTestCaseType().getCase());
    }
}
