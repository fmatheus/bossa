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

import com.bigbross.bossa.wfnet.WFNetTestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests using all testcases.
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 **/
public class BossaTestSuite extends TestCase {

    /**
     * Constructor.
     *
     * @param name The name.
     **/
    public BossaTestSuite(String name) {
	super(name);
    }

    /**
     * Makes the suite of tests.
     *
     * @return The suite.
     **/
    public static Test suite() {
	// All tests should be added here
	TestSuite suite = new TestSuite("Bossa Test Suite");
	suite.addTest(WFNetTestSuite.suite());
	return suite;
    }
}