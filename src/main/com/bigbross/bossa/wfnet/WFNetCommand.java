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

import java.io.Serializable;

import org.prevayler.Command;
import org.prevayler.PrevalentSystem;

import com.bigbross.bossa.Bossa;


/**
 * This class represents all commands applied to the WFNet persistent
 * objects. <p>
 * 
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
abstract class WFNetCommand implements Command {

    /**
     * Executes a command in a prevalent system. <p>
     * 
     * @see org.prevayler.Command#execute(PrevalentSystem)
     */
    public Serializable execute(PrevalentSystem system) throws Exception {
        return execute(((Bossa) system).getCaseTypeManager());
    }

    /**
     * Executes a command in the object tree rooted at CaseTypeManager. <p>
     */
    protected abstract Serializable execute(CaseTypeManager caseTypeManager)
        throws Exception;
}
