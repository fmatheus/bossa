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

package com.bigbross.bossa;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.bigbross.bossa.history.HistoryTestSuite;
import com.bigbross.bossa.io.IOTestSuite;
import com.bigbross.bossa.notify.NotifyTestSuite;
import com.bigbross.bossa.resource.ResourceTestSuite;
import com.bigbross.bossa.wfnet.WFNetTestSuite;
import com.bigbross.bossa.work.WorkTestSuite;

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
	TestSuite suite = new TestSuite("Bossa Test Suite");
        /* All tests should be added here. */
        suite.addTest(new TestSuite(TimeSourceTest.class));
	suite.addTest(WFNetTestSuite.suite());
        suite.addTest(ResourceTestSuite.suite());
        suite.addTest(WorkTestSuite.suite());
        suite.addTest(NotifyTestSuite.suite());
        suite.addTest(HistoryTestSuite.suite());
        suite.addTest(IOTestSuite.suite());
	return suite;
    }
}
