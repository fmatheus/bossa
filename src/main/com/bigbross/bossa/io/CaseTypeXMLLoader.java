/*
 * Bossa Workflow System
 *
 * $Id$
 *
 * Copyright (C) 2004 OpenBR Sistemas S/C Ltda.
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

package com.bigbross.bossa.io;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.bigbross.bossa.BossaException;
import com.bigbross.bossa.wfnet.CaseType;
import com.bigbross.bossa.wfnet.Place;
import com.bigbross.bossa.wfnet.Transition;

/**
 * This class loads an external XML case type definition. <p>
 * 
 * The XML representation of a case type is a PNML file, as created
 * by the PNK using the WFNet net type. <p>
 * 
 * FIXME: Document JDOM dependecy. JAXP 1.1.
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class CaseTypeXMLLoader {
    
    private Document document;

    /**
     * Creates a new loader for the provided PNML file. <p>
     * 
     * @param file the PNML file to open.
     * @throws DataTransferException if an error happens reading or parsing
     *                               the PNML file.
     */
    public CaseTypeXMLLoader(String file) throws DataTransferException {
        try {
            SAXBuilder builder = new SAXBuilder();
            document = builder.build(new File(file));
        } catch (IOException e) {
            throw new DataTransferException("I/O error.", e);
        } catch (JDOMException e) {
            throw new DataTransferException("Error parsing XML.", e);
        }
    }
    
    /**
     * Creates the case type from the PNML file loaded. <p>
     * 
     * FIXME: Add behaviour??? At least attributes.
     * 
     * @return the case type created.
     * @exception SetAttributeException if the underlying expression
     *            evaluation system has problems setting an attribute.
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     */
    public CaseType createCaseType() throws BossaException {
        
        HashMap places = new HashMap();
        HashMap transitions = new HashMap();
        Element wfnet = document.getRootElement().getChild("net");

        String caseTypeId = wfnet.getChild("id").getChildText("value");
        CaseType caseType = new CaseType(caseTypeId);
        
        Iterator i = wfnet.getChildren("place").iterator();
        while (i.hasNext()) {
            Element place = (Element) i.next();
            String pnmlId = place.getAttributeValue("id");
            String id = place.getChild("id").getChildText("value");
            String initialMarking = 
                place.getChild("initialMarking").getChildText("value");
            initialMarking = "".equals(initialMarking) ? "0" : initialMarking;
            
            places.put(pnmlId, 
                caseType.registerPlace(id, Integer.parseInt(initialMarking)));
        }
        
        i = wfnet.getChildren("transition").iterator();
        while (i.hasNext()) {
            Element transition = (Element) i.next();
            String pnmlId = transition.getAttributeValue("id");
            String id = transition.getChild("id").getChildText("value");
            String resource =
                transition.getChild("resource").getChildText("value");
            String timeout =
                transition.getChild("timeout").getChildText("value");
            timeout = "".equals(timeout) ? "-1" : timeout;
            
            transitions.put(pnmlId, caseType.registerTransition(id,
                resource, Long.parseLong(timeout)));
        }
        
        i = wfnet.getChildren("arc").iterator();
        while (i.hasNext()) {
            Element arc = (Element) i.next();
            String source = arc.getAttributeValue("source");
            String target = arc.getAttributeValue("target");
            String weight = arc.getChild("weight").getChildText("value");

            if (source.charAt(0) == 'p') {
                Transition t = (Transition) transitions.get(target);
                t.input((Place) places.get(source), weight);
            } else {
                Transition t = (Transition) transitions.get(source);
                t.output((Place) places.get(target), weight);
            }
        }
        
        // Create attributes.
        
        //caseType.buildTemplate(new HashMap());
        
        return caseType;
    }
}
