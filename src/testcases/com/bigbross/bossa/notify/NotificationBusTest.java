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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import com.bigbross.bossa.resource.Resource;
import com.bigbross.bossa.resource.ResourceUtil;

public class NotificationBusTest extends TestCase {

    private NotificationBus bus;

    public NotificationBusTest(String name) {
	super(name);
    }

    protected void setUp() {
	System.out.println("Setting up a notification bus test.");
        List persistent = new ArrayList();
        persistent.add(new GoodListener("persist", 0, null));
        bus = new NotificationBus(persistent);
    }

    public void testRegisterRemoveListener() {
        assertTrue(bus.registerListener(new GoodListener("test1", 0, null)));
        assertFalse(bus.registerListener(new GoodListener("test1", 0, null)));
        bus.removeListener("test1");
        assertTrue(bus.registerListener(new GoodListener("test1", 0, null)));
        assertTrue(bus.registerListener(new GoodListener("persist", 0, null)));
    }

    public void testNotify() {
        assertTrue(bus.registerListener(new GoodListener("The Good", 0, null)));
        assertTrue(bus.registerListener(new BadListener("The Bad", 0, null)));
        HashMap theUgly = new HashMap();
        
        try {
            bus.notifyEvent(new Event("event1", Event.WFNET_EVENT, theUgly));
        } catch (Exception e) {
            fail("This exception should not propagate here.");
        }
        assertEquals("ok ok", theUgly.get("status"));
        assertEquals("run", theUgly.get("bad"));
    }
    
    public void testFilterByType() {
        assertTrue(bus.registerListener(
                   new GoodListener("test1", Event.RESOURCE_EVENT, null)));
        HashMap attrib = new HashMap();

        bus.notifyEvent(new Event("event1", Event.WFNET_EVENT, attrib));
        assertEquals("ok", attrib.get("status"));
    }
    
    public void testFilterByResource() {
        Resource trumps = ResourceUtil.createResource("trumps");
        Resource jdoe = ResourceUtil.createResource("joedoe");
        Resource mdoe = ResourceUtil.createResource("marydoe");
        trumps.includeImpl(jdoe);
        trumps.includeImpl(mdoe);
        HashMap attrib;

        assertTrue(bus.registerListener(new GoodListener("test1", 0, jdoe)));

        attrib = new HashMap();
        attrib.put("resource", trumps);
        bus.notifyEvent(new Event("event1", Event.WFNET_EVENT, attrib));
        assertEquals("ok ok", attrib.get("status"));

        attrib = new HashMap();
        attrib.put("resource", jdoe);
        bus.notifyEvent(new Event("event2", Event.WFNET_EVENT, attrib));
        assertEquals("ok ok", attrib.get("status"));

        attrib = new HashMap();
        attrib.put("resource", mdoe);
        bus.notifyEvent(new Event("event3", Event.WFNET_EVENT, attrib));
        assertEquals("ok", attrib.get("status"));
    }
}
