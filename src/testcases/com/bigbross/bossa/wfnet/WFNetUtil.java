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

package com.bigbross.bossa.wfnet;

import java.util.Map;

import com.bigbross.bossa.BossaException;
import com.bigbross.bossa.BossaTestUtil;
import com.bigbross.bossa.resource.Resource;
import com.bigbross.bossa.resource.ResourceUtil;

public class WFNetUtil {

    static Case createCase() throws Exception {
        return BossaTestUtil.createCaseType("test").openCaseImpl(null);
    }

    static Case createCase(int[] marking) throws Exception {
        return BossaTestUtil.createCaseType("test", marking).openCaseImpl(null);
    }

    static Case createAutoFireCase() throws BossaException {
        CaseType caseType = new CaseType("auto-fire");
        Place A = caseType.registerPlace("A", 1);
        Place B = caseType.registerPlace("B");
        Place C = caseType.registerPlace("C");
        Transition a = caseType.registerTransition("a", "boss");
        Transition b = caseType.registerTransition("b", "boss", 0);
        a.input(A,  "1");
        a.output(B, "1");
        b.input(B, "1");
        b.output(C, "1");
        caseType.buildTemplate(null);
        return caseType.openCaseImpl(null);
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
