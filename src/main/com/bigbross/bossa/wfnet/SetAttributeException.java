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

package com.bigbross.bossa.wfnet;

import com.bigbross.bossa.BossaException;

/**
 * This exception is thrown if the underlying expression evaluation system
 * has problems setting an attribute. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class SetAttributeException extends BossaException {

    /**
     * See <code>BossaException.BossaException()</code>.
     *
     * @see com.bigbross.bossa.BossaException#BossaException()
     */
    public SetAttributeException() {
        super();
    }

    /**
     * See <code>BossaException.BossaException(String)</code>.
     *
     * @see com.bigbross.bossa.BossaException#BossaException(String)
     */
    public SetAttributeException(String message) {
        super(message);
    }

    /**
     * See <code>BossaException.BossaException(String, Throwable)</code>.
     *
     * @see com.bigbross.bossa.BossaException#BossaException(String,
     *                                                       Throwable)
     */
    public SetAttributeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * See <code>BossaException.BossaException(Throwable)</code>.
     *
     * @see com.bigbross.bossa.BossaException#BossaException(Throwable)
     */
    public SetAttributeException(Throwable cause) {
        super(cause);
    }
}
