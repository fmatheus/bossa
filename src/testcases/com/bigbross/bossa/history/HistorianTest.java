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
import com.bigbross.bossa.resource.ResourceEvents;
import com.bigbross.bossa.wfnet.WFNetEvents;

public class HistorianTest extends TestCase {

    private Historian historian;
    private Event e0, e1, e2, e3;
    public static final long t1 = 1069364322000L; 
    public static final long t2 = 1069365822000L; 
    public static final long t3 = 1069366362000L; 
    public static final long t4 = 1069367322000L; 

    public HistorianTest(String name) {
	super(name);
    }

    protected void setUp() {
        historian = new Historian();
        HashMap attributes = new HashMap();
        attributes.put(WFNetEvents.ATTRIB_CASE_TYPE_ID, "casetype1");
        attributes.put(WFNetEvents.ATTRIB_CASE_ID, new Integer(1));
        attributes.put(WFNetEvents.ATTRIB_RESOURCE_ID, "resource1");
        Date aTime = new Date();
        aTime.setTime(t1);
        e0 = new Event("teste0", Event.WFNET_EVENT, attributes, aTime);
        attributes = new HashMap();
        attributes.put(WFNetEvents.ATTRIB_CASE_TYPE_ID, "casetype2");
        attributes.put(WFNetEvents.ATTRIB_CASE_ID, new Integer(1));
        attributes.put(WFNetEvents.ATTRIB_RESOURCE_ID, "resource2");
        aTime = new Date();
        aTime.setTime(t2);
        e1 = new Event("teste1", Event.WFNET_EVENT, attributes, aTime);
        attributes = new HashMap();
        attributes.put(ResourceEvents.ATTRIB_HOST_RESOURCE_ID, "hostresource");
        attributes.put(ResourceEvents.ATTRIB_RESOURCE_ID, "resource1");
        aTime = new Date();
        aTime.setTime(t3);
        e2 = new Event("teste2", Event.RESOURCE_EVENT, attributes, aTime);
        attributes = new HashMap();
        attributes.put(WFNetEvents.ATTRIB_CASE_TYPE_ID, "casetype1");
        attributes.put(WFNetEvents.ATTRIB_CASE_ID, new Integer(2));
        attributes.put(WFNetEvents.ATTRIB_RESOURCE_ID, "resource3");
        aTime = new Date();
        aTime.setTime(t4);
        e3 = new Event("teste3", Event.WFNET_EVENT, attributes, aTime);
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
    
    public void testCaseTypeHistory() {
        historian.newEvent(e0);
        historian.newEvent(e1);
        historian.newEvent(e2);
        historian.newEvent(e3);
        
        List events = historian.getCaseTypeHistory("casetype1");
        assertEquals(2, events.size());
        assertSame(e0, events.get(0));
        assertSame(e3, events.get(1));
        
        assertEquals(0, historian.getCaseTypeHistory("foo").size());
    }

    public void testCaseHistory() {
        historian.newEvent(e0);
        historian.newEvent(e1);
        historian.newEvent(e2);
        historian.newEvent(e3);
        
        List events = historian.getCaseHistory("casetype1", 2);
        assertEquals(1, events.size());
        assertSame(e3, events.get(0));
        
        assertEquals(0, historian.getCaseHistory("casetype1", 4).size());
    }

    public void testResourceHistory() {
        historian.newEvent(e0);
        historian.newEvent(e1);
        historian.newEvent(e2);
        historian.newEvent(e3);
        
        List events = historian.getResourceHistory("resource1");
        assertEquals(2, events.size());
        assertSame(e0, events.get(0));
        assertSame(e2, events.get(1));
        
        assertEquals(0, historian.getCaseTypeHistory("bar").size());
    }
    
    public void testPurgeHistory() {
        historian.newEvent(e0);
        historian.newEvent(e1);
        historian.newEvent(e2);
        historian.newEvent(e3);
        
        Date end = new Date();
        end.setTime(t3);

        historian.purgeHistoryImpl(end);
        List events = historian.getHistory();
        assertEquals(2, events.size());
        assertSame(e2, events.get(0));
        assertSame(e3, events.get(1));
        
        end.setTime(0);
        historian.purgeHistoryImpl(end);
        events = historian.getHistory();
        assertEquals(2, events.size());

        end = new Date();
        historian.purgeHistoryImpl(end);
        events = historian.getHistory();
        assertEquals(0, events.size());
    }
}
