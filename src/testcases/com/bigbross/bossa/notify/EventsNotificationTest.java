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

import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaTestUtil;
import com.bigbross.bossa.resource.Resource;
import com.bigbross.bossa.resource.ResourceManager;
import com.bigbross.bossa.wfnet.Activity;
import com.bigbross.bossa.wfnet.CaseTypeManager;
import com.bigbross.bossa.wfnet.WFNetEvents;
import com.bigbross.bossa.wfnet.WFNetUtil;
import com.bigbross.bossa.wfnet.WorkItem;
import com.bigbross.bossa.work.WorkManager;

public class EventsNotificationTest extends TestCase {

    private CaseTypeManager caseTypeManager;
    private ResourceManager resourceManager;
    private WorkManager workManager;
    private Resource frank, sally;
    private MemoryListener listener;
    
    public EventsNotificationTest(String name) {
	super(name);
    }

    protected void setUp() throws Exception {
        Bossa bossa = BossaTestUtil.createCompleteTestBossa();
        caseTypeManager = bossa.getCaseTypeManager();
        resourceManager = bossa.getResourceManager();
        frank = resourceManager.getResource("frank");
        sally = resourceManager.getResource("sally");
        workManager = bossa.getWorkManager();
        
        listener = new MemoryListener("test", 0, null);
        bossa.getNotificationBus().registerListener(listener);
    }

    public void testLogRegisterRemoveCaseType() throws Exception {
        String caseTypeId = "created";
        caseTypeManager.registerCaseType(WFNetUtil.createCaseType(caseTypeId));
        caseTypeManager.removeCaseType(caseTypeId);

        List events = listener.getNotifications();
        assertEquals(2, events.size());
        Event event = (Event) events.get(0);
        assertEquals(Event.WFNET_EVENT, event.getType());
        assertEquals(WFNetEvents.ID_REGISTER_CASE_TYPE, event.getId());
        assertEquals(caseTypeId,
            event.getAttributes().get(WFNetEvents.ATTRIB_CASE_TYPE_ID));
        event = (Event) events.get(1);
        assertEquals(Event.WFNET_EVENT, event.getType());
        assertEquals(WFNetEvents.ID_REMOVE_CASE_TYPE, event.getId());
        assertEquals(caseTypeId,
            event.getAttributes().get(WFNetEvents.ATTRIB_CASE_TYPE_ID));
    }

    public void testLogOpenCase() throws Exception {
        WorkItem wi = (WorkItem) workManager.getWorkItems(frank, true).get(0);
        wi.open(frank);

        List events = listener.getNotifications();
        assertEquals(2, events.size());
        Event event = (Event) events.get(0);
        assertEquals(Event.WFNET_EVENT, event.getType());
        assertEquals(WFNetEvents.ID_OPEN_CASE, event.getId());
        /* Starting work item will create a new case, so we have "+ 1". */
        assertEquals(new Integer(wi.getCase().getId() + 1),
            event.getAttributes().get(WFNetEvents.ATTRIB_CASE_ID));
        assertEquals(wi.getCaseType().getId(),
            event.getAttributes().get(WFNetEvents.ATTRIB_CASE_TYPE_ID));
    }

    public void testLogCloseCase() throws Exception {
        WorkItem wi = (WorkItem) workManager.getWorkItems(frank, true).get(0);
        wi.open(frank).close();
        wi = (WorkItem) workManager.getWorkItems(sally).get(0);
        wi.open(sally).close();
        wi = (WorkItem) workManager.getWorkItems(frank).get(0);
        wi.open(frank).close();
        

        List events = listener.getNotifications();
        assertEquals(8, events.size());
        Event event = (Event) events.get(7);
        assertEquals(Event.WFNET_EVENT, event.getType());
        assertEquals(WFNetEvents.ID_CLOSE_CASE, event.getId());
        assertEquals(new Integer(wi.getCase().getId()),
            event.getAttributes().get(WFNetEvents.ATTRIB_CASE_ID));
        assertEquals(wi.getCaseType().getId(),
            event.getAttributes().get(WFNetEvents.ATTRIB_CASE_TYPE_ID));
    }

    public void testLogOpenWorkItem() throws Exception {
        WorkItem wi = (WorkItem) workManager.getWorkItems(frank, true).get(0);
        wi.open(frank);

        List events = listener.getNotifications();
        assertEquals(2, events.size());
        Event event = (Event) events.get(1);
        assertEquals(Event.WFNET_EVENT, event.getType());
        assertEquals(WFNetEvents.ID_OPEN_WORK_ITEM, event.getId());
        assertEquals(wi.getId(),
            event.getAttributes().get(WFNetEvents.ATTRIB_WORK_ITEM_ID));
        /* Starting work item will create a new case, so we have "+ 1". */
        assertEquals(new Integer(wi.getCase().getId() + 1),
            event.getAttributes().get(WFNetEvents.ATTRIB_CASE_ID));
        assertEquals(wi.getCaseType().getId(),
            event.getAttributes().get(WFNetEvents.ATTRIB_CASE_TYPE_ID));
        assertEquals(frank.getId(),
            event.getAttributes().get(WFNetEvents.ATTRIB_RESOURCE_ID));
    }

    public void testLogCloseActivity() throws Exception {
        WorkItem wi = (WorkItem) workManager.getWorkItems(frank, true).get(0);
        Activity act = wi.open(frank);
        HashMap newAttrib = new HashMap();
        act.close(newAttrib);

        List events = listener.getNotifications();
        assertEquals(3, events.size());
        Event event = (Event) events.get(2);
        assertEquals(Event.WFNET_EVENT, event.getType());
        assertEquals(WFNetEvents.ID_CLOSE_ACTIVITY, event.getId());
        assertEquals(new Integer(act.getId()),
            event.getAttributes().get(WFNetEvents.ATTRIB_ACTIVITY_ID));
        assertEquals(act.getWorkItemId(),
            event.getAttributes().get(WFNetEvents.ATTRIB_ACTIVITY_WI_ID));
        assertEquals(new Integer(act.getCase().getId()),
            event.getAttributes().get(WFNetEvents.ATTRIB_CASE_ID));
        assertEquals(act.getCaseType().getId(),
            event.getAttributes().get(WFNetEvents.ATTRIB_CASE_TYPE_ID));
        assertEquals(frank.getId(),
            event.getAttributes().get(WFNetEvents.ATTRIB_RESOURCE_ID));
        assertEquals(newAttrib,
            event.getAttributes().get(WFNetEvents.ATTRIB_NEW_CASE_ATTRIBUTES));
    }

    public void testLogCancelActivity() throws Exception {
        WorkItem wi = (WorkItem) workManager.getWorkItems(frank, true).get(0);
        Activity act = wi.open(frank);
        act.cancel();

        List events = listener.getNotifications();
        assertEquals(3, events.size());
        Event event = (Event) events.get(2);
        assertEquals(Event.WFNET_EVENT, event.getType());
        assertEquals(WFNetEvents.ID_CANCEL_ACTIVITY, event.getId());
        assertEquals(new Integer(act.getId()),
            event.getAttributes().get(WFNetEvents.ATTRIB_ACTIVITY_ID));
        assertEquals(act.getWorkItemId(),
            event.getAttributes().get(WFNetEvents.ATTRIB_ACTIVITY_WI_ID));
        assertEquals(new Integer(act.getCase().getId()),
            event.getAttributes().get(WFNetEvents.ATTRIB_CASE_ID));
        assertEquals(act.getCaseType().getId(),
            event.getAttributes().get(WFNetEvents.ATTRIB_CASE_TYPE_ID));
        assertEquals(frank.getId(),
            event.getAttributes().get(WFNetEvents.ATTRIB_RESOURCE_ID));
    }
}
