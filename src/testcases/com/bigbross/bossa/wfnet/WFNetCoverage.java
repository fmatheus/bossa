/*
 * Bossa Workflow System
 *
 * $Id$
 *
 * Copyright (C) 2004 OpenBR Sistemas S/C Ltda.
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

import java.util.Map;

import junit.framework.TestCase;

public class WFNetCoverage extends TestCase {

    public void check(CaseType caseType) {

        assertEquals("wfnet-coverage", caseType.getId());

        assertEquals(6, caseType.getPlaces().size());
        Place p1 = caseType.getPlace("place1");
        assertNotNull(p1);
        assertEquals(1, p1.getInitialMarking());
        Place p21 = caseType.getPlace("place2.1");
        assertNotNull(p1);
        assertEquals(0, p21.getInitialMarking());
        Place p22 = caseType.getPlace("place2.2");
        assertNotNull(p1);
        assertEquals(0, p22.getInitialMarking());
        Place p3 = caseType.getPlace("place3");
        assertNotNull(p1);
        assertEquals(0, p3.getInitialMarking());
        Place p4 = caseType.getPlace("place4");
        assertNotNull(p1);
        assertEquals(0, p4.getInitialMarking());
        Place p5 = caseType.getPlace("place5");
        assertNotNull(p5);
        assertEquals(0, p5.getInitialMarking());

        assertEquals(5, caseType.getTransitions().size());
        Transition t1 = caseType.getTransition("workitem1");
        assertNotNull(t1);
        assertEquals("starters", t1.getResource().toString());
        assertEquals(-1, t1.getTimeout());
        Transition t21 = caseType.getTransition("workitem2.1");
        assertNotNull(t21);
        assertNull(t21.getResource());
        assertEquals(0, t21.getTimeout());
        Transition t22 = caseType.getTransition("workitem2.2");
        assertNotNull(t22);
        assertEquals("workers", t22.getResource().toString());
        assertEquals(-1, t22.getTimeout());
        Transition t3 = caseType.getTransition("workitem3");
        assertNotNull(t3);
        assertEquals("workers-$workitem2.2", t3.getResource().toString());
        assertEquals(-1, t3.getTimeout());
        Transition t4 = caseType.getTransition("workitem4");
        assertNotNull(t4);
        assertEquals("workers", t4.getResource().toString());
        assertEquals(-1, t4.getTimeout());

        assertEquals(1, t1.getInputEdges().size());
        assertEquals("-1", t1.getInputEdges().get(0).toString());
        assertEquals(2, t1.getOutputEdges().size());
        assertEquals( "1", t1.getOutputEdges().get(0).toString());
        assertEquals( "1", t1.getOutputEdges().get(1).toString());
        assertEquals(1, t21.getInputEdges().size());
        assertEquals("-1", t21.getInputEdges().get(0).toString());
        assertEquals(1, t21.getOutputEdges().size());
        assertEquals( "1", t21.getOutputEdges().get(0).toString());
        assertEquals(1, t22.getInputEdges().size());
        assertEquals("-1", t22.getInputEdges().get(0).toString());
        assertEquals(1, t22.getOutputEdges().size());
        assertEquals( "1", t22.getOutputEdges().get(0).toString());
        assertEquals(1, t3.getInputEdges().size());
        assertEquals("-2", t3.getInputEdges().get(0).toString());
        assertEquals(2, t3.getOutputEdges().size());
        assertEquals( "1", t3.getOutputEdges().get(0).toString());
        assertEquals( "optional", t3.getOutputEdges().get(1).toString());
        assertEquals(1, t4.getInputEdges().size());
        assertEquals("-1", t4.getInputEdges().get(0).toString());
        assertEquals(1, t4.getOutputEdges().size());
        assertEquals( "1", t4.getOutputEdges().get(0).toString());

        Map attributes = caseType.getCase(0).getAttributes();
        assertEquals(2, attributes.size());
        assertEquals(new Integer(0), attributes.get("optional"));
        assertEquals("yes", attributes.get("testStr"));
    }
}
