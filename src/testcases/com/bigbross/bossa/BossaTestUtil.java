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

package com.bigbross.bossa;

import java.util.List;

import com.bigbross.bossa.resource.Resource;
import com.bigbross.bossa.resource.ResourceManager;
import com.bigbross.bossa.wfnet.CaseTypeManager;
import com.bigbross.bossa.wfnet.WFNetUtil;

public class BossaTestUtil {

    public static Bossa createCompleteTestBossa() throws Exception {
        Bossa bossa = Bossa.createBossa(null);
        CaseTypeManager caseTypeManager = bossa.getCaseTypeManager();
        ResourceManager resourceManager = bossa.getResourceManager();
        
        caseTypeManager.registerCaseType(WFNetUtil.createCaseType("test"));
        
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
        
        return bossa;
    }
}
