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
 * It may be a input or output edge.
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class Edge {
    
    protected String expression = "0";

    Edge() {}

    Edge(String expression) {
	this.expression = expression;
    }

    static Edge newInput(String expression) {
	return new Edge(expression) {

		int weight() {
		    return -eval();
		}

		int input() {
		    return eval();
		}

		public String toString() {
		    return "-" + expression;
		}
	    };
    }

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

    int weight() {
	return 0;
    }

    int input() {
	return 0;
    }

    int output() {
	return 0;
    }

    int eval() {
	return Integer.parseInt(expression);
    }

    public String toString() {
	return expression;
    }

}
