/*
 * Bossa Workflow System
 *
 * $Id$
 *
 * Copyright (C) 2004 OpenBR Sistemas S/C Ltda.
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

package com.bigbross.bossa.io;

import java.io.File;

public class IOUtil {

    private static final File testDir = new File("build/TestDir");

    public static String testDirName() {
        return testDir.getPath();
    }

    public static boolean createTestDir() {
        return testDir.mkdir();
    }

    public static boolean removeTestDir() {
        if (testDir.exists()) {
            File[] contents = testDir.listFiles();
            for (int i = 0; i < contents.length; i++) {
                if (!contents[i].delete()) {
                    return false;
                }
            }
            return testDir.delete();
        } else {
            return true;
        }
    }
}
