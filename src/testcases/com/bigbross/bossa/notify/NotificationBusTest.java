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

import junit.framework.TestCase;

public class NotificationBusTest extends TestCase {

    private NotificationBus bus;

    public NotificationBusTest(String name) {
	super(name);
    }

    protected void setUp() {
	System.out.println("Setting up a notification bus test.");
        bus = new NotificationBus();
    }

    public void testRegisterRemoveListener() {
        assertTrue(bus.registerListener(new GoodListener("test1", null)));
        assertFalse(bus.registerListener(new GoodListener("test1", null)));
        bus.removeListener("test1");
        assertTrue(bus.registerListener(new GoodListener("test1", null)));
    }

    public void testNotify() {
        assertTrue(bus.registerListener(new GoodListener("The Good", null)));
        assertTrue(bus.registerListener(new BadListener("The Bad", null)));
        HashMap theUgly = new HashMap();
        
        try {
            bus.notifyEvent("event1", theUgly);
        } catch (Exception e) {
            fail("This exception should not propagate here.");
        }
        assertEquals("ok", theUgly.get("status"));
    }
}
