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

package com.bigbross.bossa;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * This class represents an exception of the Bossa system. <p>
 * 
 * This exception also implements the behaviour of a nested (or chained)
 * exception, such as the <code>Throwable</code> class in Java 1.4.
 * This is an exception which may contain another throwable which
 * caused it to get thrown. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class BossaException extends Exception {

    /**
     * The nested throwable. <p>
     */
    private Throwable cause;

    /**
     * Constructs a new <code>BossaException</code> object with
     * <code>null</code> as its detail message. <p>
     */
    public BossaException() {
        super();
        this.cause = null;
    }

    /**
     * Constructs a new <code>BossaException</code> object with the
     * specified detail message. <p>
     *
     * @param message the detail message.
     */
    public BossaException(String message) {
        super(message);
        this.cause = null;
    }

    /**
     * Constructs a new <code>BossaException</code> object with the
     * specified detail message and cause. <p>
     *
     * @param message the detail message.
     * @param cause the cause.
     */
    public BossaException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

    /**
     * Constructs a new <code>BossaException</code> object with the
     * specified cause and a detail message of
     * <code>cause.toString()</code>. <p>
     *
     * @param cause the cause.
     */
    public BossaException(Throwable cause) {
        super(cause.toString());
        this.cause = cause;
    }

    /**
     * Returns the cause of this <code>BossaException</code>. The
     * cause is the throwable that caused this
     * <code>BossaException</code> object to get thrown. <p>
     *
     * @return The cause.
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * Initializes the cause of this <code>BossaException</code>
     * object to the specified value. The cause is the throwable that
     * caused this <code>BossaException</code> object to get
     * thrown. <p>
     *
     * @param cause the cause.
     * @return A reference to this <code>BossaException</code>.
     */
    public Throwable initCause(Throwable cause) {
        this.cause = cause;
        return this;
    }

    /**
     * Prints this <code>BossaException</code> object and its
     * backtrace to the standard error stream. <p>
     */
    public void printStackTrace() {
        super.printStackTrace();
        if (cause != null) {
            System.err.print("Caused by:");
            cause.printStackTrace();
        }
    }

    /**
     * Prints this <code>BossaException</code> object and its
     * backtrace to the specified print stream. <p>
     *
     * @param ps <code>PrintStream</code> object to use for output.
     */
    public void printStackTrace(PrintStream ps) {
        super.printStackTrace(ps);
        if (cause != null) {
            ps.print("Caused by:");
            cause.printStackTrace(ps);
        }
    }

    /**
     * Prints this <code>BossaException</code> object and its
     * backtrace to the specified print writer. <p>
     *
     * @param pw <code>PrintWriter</code> object to use for output.
     */
    public void printStackTrace(PrintWriter pw) {
        super.printStackTrace(pw);
        if (cause != null) {
            pw.print("Caused by:");
            cause.printStackTrace(pw);
        }
    }
}