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

import java.util.Date;

import junit.framework.TestCase;

import com.bigbross.bossa.history.Historian;
import com.bigbross.bossa.notify.Event;
import com.bigbross.bossa.wfnet.CaseTypeManager;

public class TimeSourceTest extends TestCase {

    private Date d1, d2;
    private DeterministicTimeSource source;

    public TimeSourceTest(String name) {
	super(name);
    }

    protected void setUp() {
        d1 = new Date(1069365822000L);
        d2 = new Date(1069365933000L);
        source = new DeterministicTimeSource();
    }

    public void testSource() {
        assertNull(source.getTime());
        source.setTime(d1);
        assertEquals(d1, source.getTime());
        source.setTime(d2);
        assertEquals(d2, source.getTime());
        assertEquals(d2, source.getTime());
    }

    public void testConstantTime() {
        source.setTime(d2);
        assertEquals(d2, source.getTime());
        d1 = (Date) d2.clone();
        d2.setTime(1069365967000L);
        assertEquals(d1, source.getTime());
    }
    
    public void testExternalTimeSource() throws Exception {
        BossaFactory factory = new BossaFactory();
        factory.setTransientBossa(true);
        factory.setTimeSource(source);
        Bossa bossa = factory.createBossa();
        CaseTypeManager caseTypeManager = bossa.getCaseTypeManager();
        Historian historian = bossa.getHistorian();
        
        source.setTime(d1);
        caseTypeManager.registerCaseType(BossaTestUtil.createCaseType("foo"));
        assertEquals(d1, ((Event) historian.getHistory().get(0)).getTime());
        source.setTime(d2);
        caseTypeManager.registerCaseType(BossaTestUtil.createCaseType("bar"));
        assertEquals(d2, ((Event) historian.getHistory().get(1)).getTime());
    }
}
