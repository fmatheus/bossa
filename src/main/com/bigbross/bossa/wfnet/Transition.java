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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.bigbross.bossa.resource.Expression;

/**
 * This class represents the definition of a transition.
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Transition implements Serializable {

    private CaseType caseType;

    private int index;

    private String id;

    private Expression resource;

    /**
     * Creates a new transition. <p>
     * 
     * @param caseType the case type this transition is contained.
     * @param index the index of this transition in the transition map.
     * @param id the id of this transition.
     * @param resource the expression to select the resource responsible by
     *                 this transition.
     */
    Transition(CaseType caseType, int index, String id, String resource) {
	this.caseType = caseType;
	this.index = index;
	this.id = id;
	this.resource = caseType.getResourceRegistry().compile(resource);
    }

    /**
     * Returns the id of this transition. <p>
     * 
     * @return the id of this transition.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the resource of this transition. <p>
     * 
     * @return the resource of this transition.
     */
    public Expression getResource() {
        return resource;
    }

    /**
     * Returns the index of this transition. The index indicates the line
     * that represents this transition in the transition map. <p>
     * 
     * @return the index of this transition.
     */
    int getIndex() {
        return index;
    }

    /**
     * Returns the edge that connects this transition to a place. The edge can
     * be an input, output or inactive edge (if there is no edge connecting
     * this transition to the place). <p>
     * 
     * @param p the place.
     * @return the edge connecting this transition to <code>p</code>.
     */
    Edge getEdge(Place p) {
	return caseType.getEdge(this, p);
    }

    /**
     * Returns all the edges incident (input, output and inactive) to this
     * transition. <p>
     * 
     * @return an array of all edges incident to this transition.
     */
    Edge[] getEdges() {
	return caseType.getEdges(this);
    }

    /**
     * Creates an input edge connecting a place <code>p</code> to this
     * transition. An input edge ties the firing of this transition to the
     * availability of <code>expression</code> tokens in the place
     * <code>p</code>.
     * 
     * @param p the place.
     * @param expression the condition expression.
     */
    public void input(Place p, String expression) {
	caseType.setEdge(index, p.index, Edge.newInput(expression));
    }

    /**
     * Creates an output edge connecting this transition to a place
     * <code>p</code>. An output edge produces, after the firing of this
     * transition, <code>expression</code> tokens in the place
     * <code>p</code>.
     * 
     * @param p the place.
     * @param expression the condition expression.
     */
    public void output(Place p, String expression) {
	caseType.setEdge(index, p.index, Edge.newOutput(expression));
    }

    public String toString() {

	StringBuffer string = new StringBuffer();
	Place[] p = caseType.getPlaces();

	string.append(this.id);
	string.append("\t");
	for (int j = 0; j < p.length; ++j) {
	    string.append(getEdge(p[j]));
	    string.append("\t");
	}
	string.append(this.resource);

	return string.toString();
    }
}
