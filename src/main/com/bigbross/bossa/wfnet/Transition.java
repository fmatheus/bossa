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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bigbross.bossa.resource.Expression;

/**
 * This class represents the definition of a transition.
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Transition implements Serializable {

    private CaseType caseType;

    private String id;

    private Expression resource;
    
    private ArrayList inputs;
    
    private ArrayList outputs;

    /**
     * Creates a new transition. <p>
     * 
     * @param caseType the case type this transition is contained.
     * @param id the id of this transition.
     * @param resource the expression to select the resource responsible by
     *                 this transition.
     */
    Transition(CaseType caseType, String id, String resource) {
	this.caseType = caseType;
	this.id = id;
	this.resource = resource == null ?
                            null :
                            caseType.getResourceRegistry().compile(resource);
        this.inputs = new ArrayList();
        this.outputs = new ArrayList();
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
    Expression getResource() {
        return resource;
    }

    /**
     * Returns all the input edges of this transition. <p>
     * 
     * @return a list of all the input edges of this transition.
     */
    List getInputEdges() {
	return Collections.unmodifiableList(inputs);
    }

    /**
     * Returns all the output edges of this transition. <p>
     * 
     * @return a list of all the output edges of this transition.
     */
    List getOutputEdges() {
        return Collections.unmodifiableList(outputs);
    }

    /**
     * Creates an input edge connecting a place to this transition.
     * An input edge ties the firing of this transition to the
     * availability in the place of a number of tokens given by an integer
     * weight expression. <p>
     * 
     * @param p the place.
     * @param expression the weight expression.
     */
    public void input(Place p, String expression) {
	inputs.add(Edge.newInput(p, expression));
    }

    /**
     * Creates an output edge connecting this transition to a place.
     * An output edge produces in the place, after the firing of this
     * transition, a number of tokens given by an integer
     * weight expression. <p>
     * 
     * @param p the place.
     * @param expression the weight expression.
     */
    public void output(Place p, String expression) {
        outputs.add(Edge.newOutput(p, expression));
    }
}
