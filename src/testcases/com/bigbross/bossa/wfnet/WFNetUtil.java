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
import java.util.Map;

import com.bigbross.bossa.resource.Resource;
import com.bigbross.bossa.resource.ResourceUtil;

public class WFNetUtil {

    public static CaseType createCaseType(String id) throws Exception {
        return createCaseType(id, new int[] {1,0,0,0,0,0,0,0});
    }

    static CaseType createCaseType(String id, int[] marking)
        throws Exception {
     
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
        Transition b = caseType.registerTransition("b", "sales-$a");
        Transition c = caseType.registerTransition("c", "directors");
        Transition d = caseType.registerTransition("d", "sales");
        Transition e = caseType.registerTransition("e", "sales");
        Transition f = caseType.registerTransition("f", "$a");

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
        f.output(B, "OK");
        f.output(H, "!OK");

        HashMap attributes = new HashMap();
        attributes.put("SOK", new Boolean(false));
        attributes.put("DIR", new Boolean(false));
        attributes.put("ADIR", "");
        attributes.put("OK", new Boolean(false));

        caseType.buildTemplate(marking, attributes);

        return caseType;
    }

    static Case createCase() throws Exception {
        return createCaseType("test").openCase();
    }

    static Case createCase(int[] marking) throws Exception {
        return createCaseType("test", marking).openCase();
    }

    static boolean fire(Case caze, String workItemId, Map attributes)
        throws Exception  {
        return fire(caze, workItemId, attributes, 
                    ResourceUtil.createResource("jdoe"));
    }

    static boolean fire(Case caze, String workItemId, Map attributes,
                        Resource resource) throws Exception {
        Activity act = caze.open(caze.getWorkItem(workItemId), resource);
        if (act != null) {
            return act.getCase().close(act, attributes);
        }
	return false;
    }
}
