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

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

public class CaseTypeTest extends TestCase {

    public CaseTypeTest(String name) {
	super(name);
    }

    protected void setUp() {
    	System.out.println("Setting up a case type test.");
    }

    static CaseType createTestCaseType() {
        return createTestCaseType("Testing 1,2,3...");
    }

    static CaseType createTestCaseType(String id) {
     
        CaseType caseType = new CaseType(id);

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

	caseType.buildMap();

        a.input(A,  "1");
        a.output(B, "1");

        b.input(B,  "1");
        b.output(C, "1");
        b.output(D, "1");
        b.output(E, "1");

        c.input(D,  "1");
        c.output(B, "1");
        c.output(E, "1");
        c.output(H, "1");

        d.input(E,  "1");
        d.output(F, "1");

        e.input(F,  "1");
        e.output(G, "1");

        f.input(C,  "1");
        f.output(B, "1");
        f.output(H, "1");
        
        caseType.buildTemplate(new int[] {1,0,0,0,0,0,0,0});

        return caseType;
    }

    public void testCreation() {
        CaseType caseType = createTestCaseType();
        assertNotNull(caseType);
        
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

        assertEquals(-1, a.getEdge(A).weight());
        assertEquals( 1, a.getEdge(B).weight());

        assertEquals(-1, b.getEdge(B).weight());
        assertEquals( 1, b.getEdge(C).weight());
        assertEquals( 1, b.getEdge(D).weight());
        assertEquals( 1, b.getEdge(E).weight());
    }

    public void testTemplate() {
        CaseType caseType = createTestCaseType();
        Case template = caseType.getTemplate();
        assertNotNull(template);
        assertEquals(0, template.getId());
        assertEquals(1, template.getWorkItems().size());
        assertEquals("a", 
                     ((WorkItem) template.getWorkItems().get(0)).getId());
    }

    public void testNewCase() {
        assertNotNull(createTestCaseType().newCase(new int[] {1,0,0,0,0,0,0,0}));
    }
    
    public void testGetCase() {
        CaseType caseType = createTestCaseType();
        Case caze = caseType.newCase(new int[] {1,0,0,0,0,0,0,0});
        assertSame(caze, caseType.getCase(caze.getId()));
    }        

    public void testGetAllCases() {
        CaseType caseType = createTestCaseType();
        Case c1 = caseType.newCase(new int[] {1,0,0,0,0,0,0,0});
        Case c2 = caseType.newCase(new int[] {1,0,0,0,0,0,0,0});
        assertNotNull(c1);
        assertNotNull(c2);
        
        Iterator i = caseType.getCases();
        int id = ((Case) i.next()).getId();
        assertTrue(id == 1 || id == 2);
        id = ((Case) i.next()).getId();
        assertTrue(id == 1 || id == 2);
        assertFalse(i.hasNext());
    }
    
    public void testGetWorkItems() {
        CaseType caseType = createTestCaseType();
        Case c1 = caseType.newCase(new int[] {1,0,0,0,0,0,0,0});
        Case c2 = caseType.newCase(new int[] {1,0,0,0,0,0,0,0});
        assertNotNull(c1);
        assertNotNull(c2);
        
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
}
