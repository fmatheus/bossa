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

import java.util.Date;

/**
 * This class implements a real time time source. Use it if you want a
 * transient Bossa that gets the time from the system clock. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class RealTimeSource implements TimeSource {

    /**
     * @see com.bigbross.bossa.TimeSource#getTime()
     */
    public Date getTime() {
        return new Date();
    }
}
