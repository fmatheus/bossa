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

    public static CaseType createTestCaseType() {
        return createTestCaseType("Testing 1,2,3...");
    }

    public static CaseType createTestCaseType(String id) {
     
        CaseType caseType = new CaseType(id);

        Place A = caseType.registerPlace("A");
        Place B = caseType.registerPlace("B");
        Place C = caseType.registerPlace("C");
        Place D = caseType.registerPlace("D");
        Place E = caseType.registerPlace("E");
        Place F = caseType.registerPlace("F");
        Place G = caseType.registerPlace("G");
        Place H = caseType.registerPlace("H");

        Transition a = caseType.registerTransition("a", "requesters");
        Transition b = caseType.registerTransition("b", "sales");
        Transition c = caseType.registerTransition("c", "direstors");
        Transition d = caseType.registerTransition("d", "sales");
        Transition e = caseType.registerTransition("e", "sales");
        Transition f = caseType.registerTransition("f", "requesters");

	caseType.buildMap();

        a.input(A,  "1");
        a.output(B, "1");

        b.input(B,  "1");
        b.output(C, "!SOK");
        b.output(D, "SOK && DIR");
        b.output(E, "SOK && !DIR");

        c.input(D,  "1");
        c.output(B, "ADIR == 'BACK'");
        c.output(E, "ADIR == 'OK'");
        c.output(H, "ADIR == 'CANCEL'");

        d.input(E,  "1");
        d.output(F, "1");

        e.input(F,  "1");
        e.output(G, "1");

        f.input(C,  "1");
        f.output(B, "1");
        f.output(H, "1");

        try {        
            caseType.buildTemplate(new int[] {1,0,0,0,0,0,0,0});
        } catch (EvaluationException exception) {
            exception.printStackTrace();
            fail(exception.toString());
        }

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

        assertEquals("-1", a.getEdge(A).toString());
	assertEquals( "1", a.getEdge(B).toString());

        assertEquals("-1", b.getEdge(B).toString());
	assertEquals("!SOK", b.getEdge(C).toString());
        assertEquals( "SOK && DIR", b.getEdge(D).toString());
        assertEquals( "SOK && !DIR", b.getEdge(E).toString());
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
        Case caze = null;
        try {
            caze = createTestCaseType().newCase(new int[] {1,0,0,0,0,0,0,0});
        } catch (EvaluationException e) {
            e.printStackTrace();
            fail(e.toString());
        }
        assertNotNull(caze);
    }
    
    public void testGetCase() {
        CaseType caseType = createTestCaseType();
        Case caze = null;
        try {
            caze = caseType.newCase(new int[] {1,0,0,0,0,0,0,0});
        } catch (EvaluationException e) {
            e.printStackTrace();
            fail(e.toString());
        }
        assertSame(caze, caseType.getCase(caze.getId()));
    }        

    public void testGetAllCases() {
        CaseType caseType = createTestCaseType();
        try {
            caseType.newCase(new int[] {1,0,0,0,0,0,0,0});
            caseType.newCase(new int[] {1,0,0,0,0,0,0,0});
        } catch (EvaluationException e) {
            e.printStackTrace();
            fail(e.toString());
        }
        
        List l = caseType.getCases();
        assertEquals(2, l.size());
        int id = ((Case) l.get(0)).getId();
        assertTrue(id == 1 || id == 2);
        id = ((Case) l.get(1)).getId();
        assertTrue(id == 1 || id == 2);
    }
    
    public void testGetWorkItems() {
        CaseType caseType = createTestCaseType();
        Case c1 = null;
        Case c2 = null;
        try {
            c1 = caseType.newCase(new int[] {1,0,0,0,0,0,0,0});
            c2 = caseType.newCase(new int[] {1,0,0,0,0,0,0,0});
        } catch (EvaluationException e) {
            e.printStackTrace();
            fail(e.toString());
        }
        
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

    public void testGetActivities() {
        CaseType caseType = createTestCaseType();
        Case c1 = null;
        Case c2 = null;
        try {
            c1 = caseType.newCase(new int[] {1,0,0,0,0,0,0,0});
            c2 = caseType.newCase(new int[] {1,0,0,0,0,0,0,0});
            c1.open(c1.getWorkItem("a"), "jdoe");
            c2.open(c2.getWorkItem("a"), "jdoe");
        } catch (EvaluationException e) {
            e.printStackTrace();
            fail(e.toString());
        }

        List activities = caseType.getActivities();
        assertEquals(2, activities.size());
        Activity a1 = (Activity) activities.get(0);
        Activity a2 = (Activity) activities.get(1);
        assertFalse(a1.getCase().getId() == a2.getCase().getId());
        assertTrue(c1.getId() == a1.getCase().getId() ||
                   c1.getId() == a2.getCase().getId());
        assertTrue(c2.getId() == a1.getCase().getId() ||
                   c2.getId() == a2.getCase().getId());
        assertEquals("a", a1.getTransition().getId());
        assertEquals("a", a2.getTransition().getId());
    }
}
