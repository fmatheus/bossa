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

package com.bigbross.bossa;

import java.util.HashMap;
import java.util.List;

import com.bigbross.bossa.resource.Resource;
import com.bigbross.bossa.resource.ResourceManager;
import com.bigbross.bossa.wfnet.CaseType;
import com.bigbross.bossa.wfnet.CaseTypeManager;
import com.bigbross.bossa.wfnet.Place;
import com.bigbross.bossa.wfnet.Transition;

public class BossaTestUtil {

    public static CaseType createCaseType(String id) throws Exception {
        return createCaseType(id, new int[] {1,0,0,0,0,0,0,0});
    }

    public static CaseType createCaseType(String id, int[] marking)
        throws Exception {
    
        CaseType caseType = new CaseType(id);
    
        Place A = caseType.registerPlace("A", marking[0]);
        Place B = caseType.registerPlace("B", marking[1]);
        Place C = caseType.registerPlace("C", marking[2]);
        Place D = caseType.registerPlace("D", marking[3]);
        Place E = caseType.registerPlace("E", marking[4]);
        Place F = caseType.registerPlace("F", marking[5]);
        Place G = caseType.registerPlace("G", marking[6]);
        Place H = caseType.registerPlace("H", marking[7]);
    
        Transition a = caseType.registerTransition("a", "requesters");
        Transition b = caseType.registerTransition("b", "sales-$a");
        Transition c = caseType.registerTransition("c", "directors");
        Transition d = caseType.registerTransition("d", "sales");
        Transition e = caseType.registerTransition("e", "sales");
        Transition f = caseType.registerTransition("f", "$a");
    
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
    
        caseType.buildTemplate(attributes);
    
        return caseType;
    }

    public static Bossa createCompleteTestBossa() throws Exception {
        Bossa bossa = BossaFactory.transientBossa();
        setupTestBossa(bossa);
        return bossa;
    }

    public static void setupTestBossa(Bossa bossa) throws Exception {
        CaseTypeManager caseTypeManager = bossa.getCaseTypeManager();
        ResourceManager resourceManager = bossa.getResourceManager();
        
        caseTypeManager.registerCaseType(createCaseType("test"));
        
        Resource frank = resourceManager.createResource("frank");
        Resource sally = resourceManager.createResource("sally");
        Resource jerry = resourceManager.createResource("jerry");
        
        List resources = caseTypeManager.getCaseType("test").getResources();
        for (int i = 0; i < 3; i++) {
            Resource resource = (Resource) resources.get(i);
            if (resource.getId().equals("requesters")) {
                resource.include(frank);
                resource.include(sally);
                resource.include(jerry);
            } else if (resource.getId().equals("sales")) {
                resource.include(frank);
                resource.include(sally);
            } else if (resource.getId().equals("directors")) {
                resource.include(jerry);
            } else {
                throw new BossaException("This should not happen.");
            }
        }
    }
}
