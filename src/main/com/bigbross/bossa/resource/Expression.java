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

package com.bigbross.bossa.resource;

import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * A node of one binary tree representing a compiled resource expression.
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public abstract class Expression implements Container, Serializable {

    public final static char OR  = '+';
    public final static char SUB = '-';
    public final static char AND = '^';
    public final static char LP  = '(';
    public final static char RP  = ')';

    protected final static String DELIM = "" + OR + SUB + AND + LP + RP;

    protected Container left;
    protected Container right;

    protected Expression(Container left, Container right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Compiles a resource expression. <p>
     *
     * @param manager a <code>ResourceManager</code> to link the resources in the expression.
     * @param expression the resource expression to be compiled.
     * @return a <code>Container</code> representing the compiled resource expression.
     */
    public static Container compile(ResourceManager manager, String expression) {
        StringTokenizer expr = new StringTokenizer(expression, DELIM, true);
        return compile(manager, expr, compile(manager, expr, null));
    }

    /**
     * Parses a resource expression building a binary tree. <p>
     *
     * @param manager a <code>ResourceManager</code> to link the resources in the expression.
     * @param expression the remaining resource expression to be compiled.
     * @param node a <code>Container</code> value of the left node.
     * @return a <code>Container</code> node of the compiled resource expression.
     */
    protected static Container compile(ResourceManager manager, StringTokenizer expression, Container node) {
        if (!expression.hasMoreTokens()) {
            return node;
        }

        String tok = expression.nextToken();

        switch (tok.charAt(0)) {

        case OR: // Union node
            return compile(manager, expression, new Expression(node, compile(manager, expression, node)) {

                    public boolean contains(Resource resource) {
                        return left.contains(resource) || right.contains(resource);
                    }

                    public String toString() {
                        return toString(OR);
                    }
                });

        case AND: // Intersection node
            return compile(manager, expression, new Expression(node, compile(manager, expression, node)) {

                    public boolean contains(Resource resource) {
                        return left.contains(resource) && right.contains(resource);
                    }

                    public String toString() {
                        return toString(AND);
                    }
                });

        case SUB: // Subtraction node
            return compile(manager, expression, new Expression(node, compile(manager, expression, node)) {

                    public boolean contains(Resource resource) {
                        return !right.contains(resource) && left.contains(resource);
                    }

                    public String toString() {
                        return toString(SUB);
                    }
                });

        case LP: // Parenthesis node
            return compile(manager, expression, compile(manager, expression, node));

        case RP: // Parenthesis end
            return node;

        default: // Resource reference
            return manager.getResource(tok.trim());

        }

    }

    /**
     * Determines if a resource is contained in this. <p>
     *
     * @param resource the resource to be looked for.
     * @return <code>true</code> if the resource is found, <code>false</code> otherwise.
     */
    public abstract boolean contains(Resource resource);

    public static void main(String[] args) throws Exception {
        ResourceManager manager = new ResourceManager();
        manager.createResourceImpl("A");
        manager.createResourceImpl("B");
        manager.createResourceImpl("C");
        System.out.println(manager.compile(args[0]));
    }

    /**
     * Returns a string with the resource expression. <p>
     *
     * @return a string representation of this resource.
     */
    protected String toString(char op) {
        StringBuffer sb = new StringBuffer();

        if (left instanceof Expression) {
            sb.append("(").append(left).append(")");
        } else {
            sb.append(left);
        }

        sb.append(op);

        if (right instanceof Expression) {
            sb.append("(").append(right).append(")");
        } else {
            sb.append(right);
        }

        return sb.toString();
    }

}
