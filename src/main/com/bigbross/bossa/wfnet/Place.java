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

package com.bigbross.bossa.wfnet;

import java.io.Serializable;

/**
 * This class represents a place. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Place implements Serializable {

    private int index;

    private String id;
    
    private int initialMarking;

    /**
     * Creates a new place. <p>
     * 
     * @param index the index of this place in the marking array.
     * @param id the id of this place.
     * @param initialMarking the initial marking, the number of tokens in
     *                       this place when a new case starts.
     */
    Place(int index, String id, int initialMarking) {
	this.index = index;
	this.id = id;
        this.initialMarking = initialMarking;
    }
    
    /**
     * Returns the index of this place. <p>
     * 
     * @return the index of this place.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the id of this place. <p>
     * 
     * @return the id of this place.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the initial marking of this place. <p>
     * 
     * @return the initial marking of this place.
     */
    public int getInitialMarking() {
        return initialMarking;
    }
}
