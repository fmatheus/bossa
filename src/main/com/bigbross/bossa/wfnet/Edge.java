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

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents one edge of one transition.
 * It may be a input or output edge, mapping the precondition or postcondition of one transition.
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Edge {
    
    /**
     * The condition expression of this edge.
     */
    protected String expression = "0";

    /**
     * Creates a new <code>Edge</code> instance, with no condition (weight 0).
     */
    Edge() {}

    /**
     * Creates a new <code>Edge</code> instance.
     *
     * @param expression the condition expression.
     */
    protected Edge(String expression) {
	this.expression = expression;
    }

    /**
     * Creates a new input edge, with the transition precondition expression. <p>
     *
     * @param expression the expression.
     * @return the input <code>Edge</code>.
     */
    static Edge newInput(String expression) {
	return new Edge(expression) {

		int weight() {
		    return -eval();
		}

		int input() {
		    return eval();
		}

		public String toString() {
		    return "-" + this.expression;
		}
	    };
    }

    /**
     * Creates a new output edge, with the transition postcondition expression. <p>
     *
     * @param expression the expression.
     * @return the output <code>Edge</code>.
     */
    static Edge newOutput(String expression) {
	return new Edge(expression) {

		int weight() {
		    return eval();
		}

		int output() {
		    return eval();
		}
	    };
    }

    /**
     * Returns the weight of this edge. <p>
     *
     * @return a negative, zero, or a positive integer as this edge is an precondition, no-condition, or an postcondition.
     */
    int weight() {
	return 0;
    }

    /**
     * Returns the weight of an precondition edge. <p>
     *
     * @return a positive integer as this edge is an precondition, zero otherwise.
     */
    int input() {
	return 0;
    }

    /**
     * Returns the weight of an postcondition edge. <p>
     *
     * @return a positive integer as this edge is an postcondition, zero otherwise.
     */
    int output() {
	return 0;
    }

    /**
     * Evaluates the expression. <p>
     *
     * @return the expression result.
     */
    protected int eval() {
	return Integer.parseInt(expression);
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
