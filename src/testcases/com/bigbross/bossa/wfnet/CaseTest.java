/*
 * Bossa Workflow System
 *
 * $Id$
 *
 * Copyright (C) 2002 OpenBR Sistemas S/C Ltda.
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
import com.bigbross.bossa.wfnet.Place;
import com.bigbross.bossa.wfnet.Transition;

public class CaseTest extends TestCase {

    private CaseType caseType = new CaseType();
    private Case caze;

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

    {
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
    }

    public CaseTest(String name) {
	super(name);
    }

    protected void setUp() {
	System.out.println("Setting up a case type test.");

	caze = caseType.getCase(new int[] {1,0,0,0,0,0,0,0});
    }

    protected boolean fire(Case caze, Transition t) {
	Activity act = caze.getWorkItems()[t.index].open();
	if (act != null) {
	    System.out.println(act.toString());
	    return act.close();
	}
	return false;
    }

    public void testToString() {
	String result;

	result = caseType.toString();

	/*
	 * FIXME: Maybe we should generate a string with the right
	 * answer and compare against it.
	 */
	assertTrue(result != null);
	System.out.println(result);
    }

    public void testBurnProof() {
	System.out.println("Burn Proof:");
	assertTrue(!fire(caze, b));
	System.out.println(caze.toString());
    }

    public void testFirstShoot() {
	System.out.println("First Shoot:");
	System.out.println(caze.toString());
	assertTrue(fire(caze, a));
	System.out.println(caze.toString());
    }

    public void testMachineGun() {
	System.out.println("Machine Gun:");
	System.out.println(caze.toString());
	assertTrue(fire(caze, a));
	System.out.println(caze.toString());
	assertTrue(fire(caze, b));
	System.out.println(caze.toString());
	assertTrue(fire(caze, c));
	System.out.println(caze.toString());
	assertTrue(fire(caze, d));
	System.out.println(caze.toString());
	assertTrue(fire(caze, e));
	System.out.println(caze.toString());
	assertTrue(fire(caze, f));
	System.out.println(caze.toString());
    }

    public void testRollback() {
	System.out.println("Rollback:");
	System.out.println(caze.toString());
	Activity act = caze.getWorkItems()[a.index].open();
	assertTrue(act != null);
	System.out.println(caze.toString());
	assertTrue(act.cancel());
	System.out.println(caze.toString());
    }

}
