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

/**
 * This class represents one edge of one transition.  It may be an input or
 * output edge, mapping the precondition or postcondition of one transition. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public abstract class Edge implements Serializable {

    protected static final Integer INACTIVE = new Integer(0);

    protected Object expression = INACTIVE;
    
    private Place place;

    /**
     * Creates a new edge, with the provided weight expression and
     * connected place. For an input edge the connected place will be a
     * source place, and for an output edge it will be a sink place. <p>
     *
     * @param place the connected place.
     * @param expression the weight expression.
     */
    protected Edge(Place place, String expression) {
        this.place = place;
        try {
            this.expression = Integer.valueOf(expression);
        } catch (NumberFormatException e) {
            this.expression = expression;
        }
    }

    /**
     * Returns the place this edge is connected to. For an input edge the
     * connected place will be a source place, and for an output edge it
     * will be a sink place. <p>
     * 
     * @return the place this edge is connected to.
     */
    Place getPlace() {
        return this.place;
    }

    /**
     * Returns the weight of this edge. <p>
     *
     * @return A negative, zero, or a positive integer as this edge is a
     *         precondition, no-condition, or a postcondition.
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     */
    int weight(Case caze) throws EvaluationException {
	return INACTIVE.intValue();
    }

    /**
     * Returns the weight of a precondition edge. <p>
     *
     * @return A positive integer if this edge is a precondition,
     *         zero otherwise.
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     */
    int input(Case caze) throws EvaluationException {
	return INACTIVE.intValue();
    }

    /**
     * Returns the weight of a postcondition edge. <p>
     *
     * @return A positive integer if this edge is a precondition,
     *         zero otherwise.
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     */
    int output(Case caze) throws EvaluationException {
	return INACTIVE.intValue();
    }

    /**
     * Evaluates an integer expression representing the edge weight
     * using the case attributes. <p>
     * 
     * @param caze the case that contains the attributes.
     * @return The value of the expression, a positive integer.
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     */
    protected int eval(Case caze) throws EvaluationException {
        int result;

        if (expression instanceof Integer) {
            result = ((Integer) expression).intValue();
        } else {
            result = caze.eval((String) expression);
        }

        if (result < 0) {
            throw new EvaluationException("The edge absolute weight must " +
                                          "be positive. Was: " + result);
        }

        return result;
    }

    /**
     * Creates a new input edge, with the transition precondition
     * weight expression and source place. <p>
     *
     * @param place the connected place.
     * @param expression the weight expression.
     * @return the input edge.
     */
    static Edge newInput(Place place, String expression) {
        return new Edge(place, expression) {
            int weight(Case caze) throws EvaluationException {
                return - this.eval(caze);
            }
            int input(Case caze) throws EvaluationException {
                return this.eval(caze);
            }
            public String toString() {
                return "-" + this.expression;
            }
        };
    }

    /**
     * Creates a new output edge, with the transition postcondition           
     * weight expression and sink place. <p>
     *
     * @param place the connected place.
     * @param expression the expression.
     * @return the output edge.
     */
    static Edge newOutput(Place place, String expression) {
        return new Edge(place, expression) {
            int weight(Case caze) throws EvaluationException {
                return this.eval(caze);
            }
            int output(Case caze) throws EvaluationException {
                return this.eval(caze);
            }
        };
    }

    /**
     * Returns a string with the condition expression. <p>
     *
     * @return a string representation of this edge.
     */
    public String toString() {
	return expression.toString();
    }
}
