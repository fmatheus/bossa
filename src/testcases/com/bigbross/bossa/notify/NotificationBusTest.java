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

package com.bigbross.bossa.notify;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaFactory;
import com.bigbross.bossa.PersistenceException;
import com.bigbross.bossa.RealTimeSource;
import com.bigbross.bossa.resource.Resource;
import com.bigbross.bossa.resource.ResourceManager;
import com.bigbross.bossa.wfnet.WFNetEvents;

public class NotificationBusTest extends TestCase {

    private NotificationBus bus;

    public NotificationBusTest(String name) {
	super(name);
    }

    protected void setUp() {
        List persistent = new ArrayList();
        persistent.add(new GoodListener("persist", 0, null));
        bus = new NotificationBus(null, persistent);
    }

    public void testRegisterRemoveListener() {
        assertTrue(bus.registerListener(new GoodListener("test1", 0, null)));
        assertFalse(bus.registerListener(new GoodListener("test1", 0, null)));
        bus.removeListener("test1");
        assertTrue(bus.registerListener(new GoodListener("test1", 0, null)));
        assertTrue(bus.registerListener(new GoodListener("persist", 0, null)));
    }

    public void testNotify() {
        TestListener theGood = new GoodListener("The Good", 0, null);
        TestListener theBad = new BadListener("The Bad", 0, null);
        assertTrue(bus.registerListener(theGood));
        assertTrue(bus.registerListener(theBad));

        try {
            bus.notifyEvent(new Event("event1", Event.WFNET_EVENT,
                                      new HashMap(), new Date()));
        } catch (Exception e) {
            fail("This exception should not propagate here.");
        }
        assertEquals(1, theGood.runs());
        assertEquals(1, theBad.runs());
    }
    

    public void testImutableEvent() {
        BadListener theBad = new BadListener("The Bad", 0, null);
        assertTrue(bus.registerListener(theBad));
        Event event = new Event("event1", Event.WFNET_EVENT,
                                new HashMap(), new Date());
        
        bus.notifyEvent(event);
        assertEquals(1, theBad.runs());
        assertNull(event.getAttributes().get("bad"));
        assertTrue(theBad.wrongTime() != event.getTime().getTime());
    }

    public void testFilterByType() {
        TestListener theGood =
            new GoodListener("test1", Event.RESOURCE_EVENT, null); 
        assertTrue(bus.registerListener(theGood));

        bus.notifyEvent(new Event("event1", Event.WFNET_EVENT,
                                  new HashMap(), new Date()));
        assertEquals(0, theGood.runs());
    }
    
    public void testFilterByResource() throws Exception {
        Bossa bossa = BossaFactory.transientBossa(new RealTimeSource());
        NotificationBus bus = bossa.getNotificationBus(); 
        ResourceManager resourceManager = bossa.getResourceManager();
        
        Resource trumps = resourceManager.createResource("trumps");
        Resource jdoe = resourceManager.createResource("joedoe");
        Resource mdoe = resourceManager.createResource("marydoe");
        trumps.include(jdoe);
        trumps.include(mdoe);

        TestListener theGood = new GoodListener("test1", 0, jdoe);
        assertTrue(bus.registerListener(theGood));
        HashMap attrib = new HashMap();

        attrib.put(WFNetEvents.ATTRIB_RESOURCE_ID, trumps.getId());
        bus.notifyEvent(new Event("event1", Event.WFNET_EVENT,
                                  attrib, new Date()));
        assertEquals(1, theGood.runs());

        attrib.put(WFNetEvents.ATTRIB_RESOURCE_ID, jdoe.getId());
        bus.notifyEvent(new Event("event2", Event.WFNET_EVENT,
                                  attrib, new Date()));
        assertEquals(2, theGood.runs());

        attrib.put(WFNetEvents.ATTRIB_RESOURCE_ID, mdoe.getId());
        bus.notifyEvent(new Event("event3", Event.WFNET_EVENT,
                                  attrib, new Date()));
        assertEquals(2, theGood.runs());
    }
    
    public void testNotificationQueue() throws PersistenceException {
        Bossa bossa = BossaFactory.transientBossa(new RealTimeSource());
        TestListener theGood = new GoodListener("test", 0, null);
        assertTrue(bossa.getNotificationBus().registerListener(theGood));

        /* Creates a concrete anonymous inner class */ 
        NotificationQueue queue = new NotificationQueue() {};
        queue.addEvent(new Event("event1", Event.WFNET_EVENT,
                                 new HashMap(), new Date()));
        queue.addEvent(new Event("event2", Event.WFNET_EVENT,
                                 new HashMap(), new Date()));
        queue.notifyAll(bossa);
        assertEquals(2, theGood.runs());
        queue.notifyAll(bossa);
        assertEquals(2, theGood.runs());
    }
}
