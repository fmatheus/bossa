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

/**
 * This interface represents a time source used by the Bossa engine.
 * A suitable implementation of this interface allows Bossa to behave
 * deterministically with respect to time as an external agent. <p>
 * 
 * If an instance of Bossa is persistent, it will use its own internal
 * implementation of this interface in a way that is consistent with the
 * underlaying prevalent mechanism. <p>
 * 
 * If an instance of Bossa is transient, the client can provide an
 * implementation of this interface that is consistent with the persistence
 * mechanism employed, if any. If there is no need for the Bossa engine to
 * treat time in a controlled way (for example, being deterministic), it is
 * not necessary to provide an implementation of this interface. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public interface TimeSource {

    /**
     * Returns the current time. <p>
     * 
     * @return the current time.
     */
    Date getTime();
}
