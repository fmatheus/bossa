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

package com.bigbross.bossa.history;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests using History package testcases.
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class HistoryTestSuite extends TestCase {

    /**
     * Constructor.
     *
     * @param name the name.
     */
    public HistoryTestSuite(String name) {
        super(name);
    }

    /**
     * Makes the test suite.
     *
     * @return the suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("History Test Suite");
        /* All tests should be added here. */
        suite.addTest(new TestSuite(HistorianTest.class));
        suite.addTest(new TestSuite(HistoryTest.class));
        suite.addTest(new TestSuite(TransactionsTest.class));
        return suite;
    }
}
