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
 * This class represents one edge of one transition.  It may be a input or
 * output edge, mapping the precondition or postcondition of one transition. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Edge implements Serializable {

    /**
     * Inactive edge.
     */
    protected final int INACTIVE = 0;

    /**
     * The condition expression of this edge.
     */
    protected String expression = "0";

    /**
     * Creates a new <code>Edge</code> instance, with no condition
     * (weight 0). <p>
     */
    Edge() {}

    /**
     * Creates a new <code>Edge</code> instance. <p>
     *
     * @param expression the condition expression.
     */
    protected Edge(String expression) {
	this.expression = expression;
    }

    /**
     * Creates a new input edge, with the transition precondition
     * expression. <p>
     *
     * @param expression the expression.
     * @return the input <code>Edge</code>.
     */
    static Edge newInput(String expression) {
	return new Edge(expression) {

		protected final int INACTIVE = Integer.MAX_VALUE;

		int weight(Case caze) throws EvaluationException {
		    return - eval(caze);
		}

		int input(Case caze) throws EvaluationException {
		    return eval(caze);
		}

		public String toString() {
		    return "-" + this.expression;
		}
	    };
    }

    /**
     * Creates a new output edge, with the transition postcondition           
     * expression. <p>
     *
     * @param expression the expression.
     * @return the output <code>Edge</code>.
     */
    static Edge newOutput(String expression) {
	return new Edge(expression) {

		int weight(Case caze) throws EvaluationException {
		    return eval(caze);
		}

		int output(Case caze) throws EvaluationException {
		    return eval(caze);
		}
	    };
    }

    /**
     * Returns the weight of this edge. <p>
     *
     * @return A negative, zero, or a positive integer as this edge is a
     * precondition, no-condition, or a postcondition.
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     */
    int weight(Case caze) throws EvaluationException {
	return INACTIVE;
    }

    /**
     * Returns the weight of a precondition edge. <p>
     *
     * @return A positive integer if this edge is a precondition,
     *         <code>INACTIVE</code> otherwise.
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     */
    int input(Case caze) throws EvaluationException {
	return INACTIVE;
    }

    /**
     * Returns the weight of a postcondition edge. <p>
     *
     * @return A positive integer if this edge is a precondition,
     *         <code>INACTIVE</code> otherwise.
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     */
    int output(Case caze) throws EvaluationException {
	return INACTIVE;
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
    int eval(Case caze) throws EvaluationException {
        int result = caze.eval(expression);
        if (result < 0) {
            throw new EvaluationException("The edge absolute weight must " +
                                          "be positive. Was: " + result);
        }
        return result;
    }

    /**
     * Returns a string with the condition expression. <p>
     *
     * @return a string representation of this edge.
     */
    public String toString() {
	return expression;
    }
}
