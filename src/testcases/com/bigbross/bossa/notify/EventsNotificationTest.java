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

package com.bigbross.bossa.notify;

import java.util.List;

import junit.framework.TestCase;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaTestUtil;
import com.bigbross.bossa.resource.ResourceManager;
import com.bigbross.bossa.wfnet.CaseTypeManager;
import com.bigbross.bossa.wfnet.WFNetUtil;

public class EventsNotificationTest extends TestCase {

    private CaseTypeManager caseTypeManager;
    private ResourceManager resourceManager;
    private NotificationBus bus;
    private MemoryListener listener;
    
    public EventsNotificationTest(String name) {
	super(name);
    }

    protected void setUp() throws Exception {
	System.out.println("Setting up an event notification test.");
    
        Bossa bossa = BossaTestUtil.createCompleteTestBossa();
        caseTypeManager = bossa.getCaseTypeManager();
        resourceManager = bossa.getResourceManager();
        bus = bossa.getNotificationBus();
        
        listener = new MemoryListener("test", 0, null);
        bus.registerListener(listener);
    }

    public void testRegisterRemoveCaseType() throws Exception {
        String caseTypeId = "created";
        caseTypeManager.registerCaseType(WFNetUtil.createCaseType(caseTypeId));
        caseTypeManager.removeCaseType(caseTypeId);
        List events = listener.getNotifications();
        assertEquals(2, events.size());
        Event event = (Event) events.get(0);
        assertEquals(Event.WFNET_EVENT, event.getType());
        assertEquals("register_case_type", event.getId());
        assertEquals(caseTypeId, event.getAttributes().get("case_type_id"));
        event = (Event) events.get(1);
        assertEquals(Event.WFNET_EVENT, event.getType());
        assertEquals("remove_case_type", event.getId());
        assertEquals(caseTypeId, event.getAttributes().get("case_type_id"));
    }
}
