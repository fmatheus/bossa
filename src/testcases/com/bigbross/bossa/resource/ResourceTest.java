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

package com.bigbross.bossa.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class ResourceTest extends TestCase {

    Resource r0, r1, r2;

    public ResourceTest(String name) {
	super(name);
    }

    protected void setUp() {
	System.out.println("Setting up a resource test.");
        r0 = new Resource("r0");
        r1 = new Resource("r1");
        r2 = new Resource("r2");
    }

    public void testGroupId() {
        assertEquals("r0", r0.getId());
    }

    public void testInclude() {
        assertTrue(r0.include(r1));
        assertFalse(r0.contains(r2));

        assertTrue(r1.include(r2));
        assertTrue(r0.contains(r2));
    }

    public void testIncludeCircle() {
        assertFalse(r0.include(r0));

        assertTrue(r0.include(r1));
        assertTrue(r1.include(r2));

        assertFalse(r2.include(r1));
        assertFalse(r2.include(r0));
    }

    public void testExclude() {
        assertTrue(r0.include(r1));
        assertTrue(r1.include(r2));
        assertTrue(r0.contains(r2));

        assertTrue(r0.exclude(r2));
        assertFalse(r0.contains(r2));
    }

    public void testExcludeCircle() {
        assertFalse(r0.exclude(r0));

        assertTrue(r0.exclude(r1));
        assertTrue(r1.exclude(r2));

        assertFalse(r1.exclude(r0));
        assertFalse(r2.exclude(r0));

        assertFalse(r0.contains(r2));
        assertFalse(r2.contains(r0));
    }

    public void testRemove() {
        assertTrue(r0.include(r1));
        assertTrue(r1.include(r2));
        assertTrue(r0.contains(r2));

        assertTrue(r0.exclude(r2));
        assertFalse(r0.contains(r2));

        r0.remove(r2);
        assertTrue(r0.contains(r2));

        r0.remove(r1);
        assertFalse(r0.contains(r2));
    }

}
