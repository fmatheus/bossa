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

package com.bigbross.bossa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

import com.bigbross.bossa.history.HistoryListener;
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
    
    private TimeSource timeSource;
    
    private String stateDir;
    
    private boolean activeHistorian;
    
    /**
     * Creates a new Bossa factory with default configuration values. <p>
     */
    public BossaFactory() {
        transientBossa = false;
        timeSource =  null;
        stateDir = "BossaState";
        activeHistorian = true;
    }

    /**
     * Configures if the created Bossa is transient or not. <p>
     * 
     * A transient Bossa won't save its state, all operations
     * will be lost in case of system shutdown. However, it is still
     * serializable, so it can be included in a larger prevalent system
     * (if this is the case, look the <code>setTimeSource</code> method). <p>
     * 
     * Default: <code>false</code>. <p>
     * 
     * @param transientBossa <code>true</code> to create a transient Bossa,
     *                       <code>false</code> to create a persistent Bossa.
     * @see BossaFactory#setTimeSource(TimeSource) 
     */
    public void setTransientBossa(boolean transientBossa) {
        this.transientBossa = transientBossa;
    }

    /**
     * Configures the time source of a transient Bossa engine. See the
     * documentation of the <code>TimeSource</code> interface for more
     * information on time sources. <p>
     * 
     * If you don't care about how time is handled by the Bossa engine,
     * usa an instance of the <code>RealTimeSource</code> class. <p>
     * 
     * If you are trying to embed a transient Bossa engine in a larger
     * prevalent system, use an instance of the
     * <code>DeterministicTimeSource</code> class. Keep a reference to the
     * time source and set the time in it every time a transaction is
     * executed, all Bossa actions will happen using the time set. <p>
     * 
     * Default: <code>new RealTimeSource()</code>. <p>
     * 
     * @param timeSource the time source.
     * @see BossaFactory#setTransientBossa(boolean)
     * @see TimeSource
     * @see RealTimeSource
     * @see DeterministicTimeSource
     */
    public void setTimeSource(TimeSource timeSource) {
        this.timeSource = timeSource;
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
     * Configures if the created Bossa will log its events in the historian
     * or not. <p>
     * 
     * Default: <code>true</code>. <p>
     * 
     * @param activeHistorian <code>true</code> for the historian to log
     *                        events, <code>false</code> for the historian
     *                        to ignore events.
     */
    public void setActiveHistorian(boolean activeHistorian) {
        this.activeHistorian = activeHistorian;
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
        
        List internalListners = new ArrayList();
        if (activeHistorian) {
            internalListners.add(new HistoryListener(newBossa.getHistorian()));
        }
        newBossa.setNotificationBus(new NotificationBus(newBossa,
                                                        internalListners));

        if (this.transientBossa) {
            if (this.timeSource != null) {
                newBossa.setTimeSource(this.timeSource);                   
            } else {
                newBossa.setTimeSource(new RealTimeSource());
            }
        } else if (this.stateDir != null) {
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
     * Creates a transient Bossa engine instance, keeping all other default
     * configuration values. <p>
     * 
     * @return the newly created bossa engine.
     * @exception PersistenceException if an error occours starting the
     *            persistence mechanism.
     */
    public static Bossa transientBossa()
        throws PersistenceException {
        BossaFactory factory = new BossaFactory();
        factory.setTransientBossa(true);
        return factory.createBossa();
    }
}
