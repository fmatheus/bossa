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

import java.io.IOException;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

import com.bigbross.bossa.notify.NotificationBus;

/**
 * This class handles the creation of a Bossa instance. <p>
 * 
 * To use it, create an instance of this factory class, configure the
 * available Bossa options by calling methods of this instance and create
 * the Bossa instance using the <code>createBossa()</code> method. <p>
 * 
 * Alternatively, use one of the provided static methods to get a preconfigured
 * Bossa instance. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class BossaFactory {

    private boolean transientBossa;
    
    private String stateDir;
    
    /**
     * Creates a new Bossa factory with default configuration values. <p>
     */
    public BossaFactory() {
        transientBossa = false;
        stateDir = "BossaState";
    }

    /**
     * Configures if the created Bossa is transient or not. <p>
     * 
     * A transient Bossa won't save its state, all operations
     * will be lost in case of system shutdown. However, it is still
     * serializable, so it can be included in a larger prevalent system. <p>
     * 
     * Default: <code>false</code>.
     * 
     * @param transientBossa <code>true</code> to create a transient Bossa,
     *                       <code>false</code> to create a persistent Bossa. 
     */
    public void setTransientBossa(boolean transientBossa) {
        this.transientBossa = transientBossa;
    }

    /**
     * Configures the directory where Bossa will save its state. <p>
     * 
     * If the provided directory is empty, a new Bossa engine will be created.
     * If the provided directory contains data of an already running Bossa
     * engine, it will be restarted using this data. <p>
     * 
     * Default: <code>"BossaState"</code>
     * 
     * @param dir the directory where Bossa will save its state.
     */
    public void setStateDir(String dir) {
        if (dir != null) { 
            stateDir = dir;
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * Creates a Bossa engine instance using the current configuration
     * of the Bossa factory. <p>
     * 
     * @return the newly created bossa engine.
     * @exception PersistenceException if an error occours starting the
     *            persistence mechanism.
     */
    public Bossa createBossa() throws PersistenceException {
        Bossa newBossa = new Bossa();
        newBossa.setNotificationBus(new NotificationBus(newBossa));
        if (!this.transientBossa && (this.stateDir != null)) {
            try {
                PrevaylerFactory factory = new PrevaylerFactory();
                factory.configurePrevalentSystem(newBossa);
                factory.configurePrevalenceBase(this.stateDir);
                factory.configureTransactionFiltering(false);
                Prevayler prevayler = factory.create();
                newBossa = (Bossa) prevayler.prevalentSystem();
                newBossa.setPrevayler(prevayler);
            } catch (IOException e) {
                throw new PersistenceException("I/O error starting prevayler.",
                                                e);
            } catch (ClassNotFoundException e) {
                throw new PersistenceException("Reflection error in prevayler.",
                                                e);
            }
        }
        return newBossa;
    }
    
    /**
     * Creates a Bossa engine instance with the default configuration
     * values. <p>
     * 
     * @return the newly created bossa engine.
     * @exception PersistenceException if an error occours starting the
     *            persistence mechanism.
     */
    public static Bossa defaultBossa() throws PersistenceException {
        return new BossaFactory().createBossa();
    }

    /**
     * Creates a transient Bossa engine instance with the other default
     * configuration values. <p>
     * 
     * @return the newly created bossa engine.
     * @exception PersistenceException if an error occours starting the
     *            persistence mechanism.
     */
    public static Bossa transientBossa() throws PersistenceException {
        BossaFactory factory = new BossaFactory();
        factory.setTransientBossa(true);
        return factory.createBossa();
    }
}
