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

package com.bigbross.bossa.history;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import com.bigbross.bossa.notify.Event;

public class HistorianTest extends TestCase {

    private Historian historian;
    private Event e0, e1, e2, e3;
    private static final long t1 = 1069364322000L; 
    private static final long t2 = 1069365822000L; 
    private static final long t3 = 1069366362000L; 
    private static final long t4 = 1069367322000L; 

    public HistorianTest(String name) {
	super(name);
    }

    protected void setUp() {
        historian = new Historian();
        Date aTime = new Date();
        aTime.setTime(t1);
        e0 = new Event("teste0", Event.WFNET_EVENT, new HashMap(), aTime);
        aTime = new Date();
        aTime.setTime(t2);
        e1 = new Event("teste1", Event.WFNET_EVENT, new HashMap(), aTime);
        aTime = new Date();
        aTime.setTime(t3);
        e2 = new Event("teste2", Event.RESOURCE_EVENT, new HashMap(), aTime);
        aTime = new Date();
        aTime.setTime(t4);
        e3 = new Event("teste3", Event.WFNET_EVENT, new HashMap(), aTime);
    }

    public void testNewEvents() {
        historian.newEvent(e0);
        historian.newEvent(e1);
        historian.newEvent(e2);
        historian.newEvent(e3);
        
        List events = historian.getHistory();
        assertEquals(4, events.size());
        assertSame(e0, events.get(0));
        assertSame(e1, events.get(1));
        assertSame(e2, events.get(2));
        assertSame(e3, events.get(3));
    }
    
    public void testOutOfOrderEvents() {
        historian.newEvent(e0);
        historian.newEvent(e2);
        historian.newEvent(e3);
        historian.newEvent(e1);
        
        List events = historian.getHistory();
        assertEquals(4, events.size());
        assertSame(e0, events.get(0));
        assertSame(e1, events.get(1));
        assertSame(e2, events.get(2));
        assertSame(e3, events.get(3));
    }
    
    public void testOpenRange() {
        historian.newEvent(e0);
        historian.newEvent(e1);
        historian.newEvent(e2);
        historian.newEvent(e3);
        
        Date start = new Date();
        start.setTime(t3);

        List events = historian.getHistory(start);
        assertEquals(2, events.size());
        assertSame(e2, events.get(0));
        assertSame(e3, events.get(1));
    }
    
    public void testClosedRange() {
        historian.newEvent(e0);
        historian.newEvent(e1);
        historian.newEvent(e2);
        historian.newEvent(e3);
        
        Date start = new Date();
        start.setTime(t2);
        Date end = new Date();
        end.setTime(t4);

        List events = historian.getHistory(start, end);
        assertEquals(2, events.size());
        assertSame(e1, events.get(0));
        assertSame(e2, events.get(1));
        
        assertEquals(0, historian.getHistory(end, start).size());
    }
}
