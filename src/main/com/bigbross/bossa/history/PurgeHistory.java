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

package com.bigbross.bossa.history;

import java.util.Date;


/**
 * This class implements the purge history operation of
 * <code>Historian</code> through the prevalence subsystem. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 * @see Historian#purgeHistory(Date)
 */
class PurgeHistory extends HistorianTransaction {

    private Date end;
    
    /**
     * Creates a new purge history operation. <p>
     * 
     * @param end the end date.
     */    
    PurgeHistory(Date end) {
        this.end = end;
    }

    /**
     * @see HistorianTransaction#execute(Historian)
     */
    protected Object execute(Historian historian) {
        historian.purgeHistoryImpl(end);
        return null;
    }
}
