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
import java.util.Map;
import java.util.StringTokenizer;

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
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class CaseTypeXMLLoader {
    
    private Document document;

    /**
     * Creates a new loader for the provided PNML file. <p>
     * 
     * @param file the PNML file to open.
     * @exception DataTransferException if an error happens reading or parsing
     *                                  the PNML file.
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
     * @return the case type created.
     * @exception SetAttributeException if the underlying expression
     *            evaluation system has problems setting an attribute.
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     */
    public CaseType createCaseType() throws BossaException {
        return setupCaseType(null);
    }

    /**
     * Fills an empty case type with rules from the PNML file loaded. <p>
     * 
     * If no case type is passed as parameter a new one is created using
     * the case type id defined into PNML file. Otherwise it is ignored
     * since it should be already defined.
     *
     * @param caseType an empty case type or null to create a new one.
     * @return the case type filled.
     * @exception SetAttributeException if the underlying expression
     *            evaluation system has problems setting an attribute.
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     */
    public CaseType setupCaseType(CaseType caseType) throws BossaException {
        HashMap places = new HashMap();
        HashMap transitions = new HashMap();
        Element wfnet = document.getRootElement().getChild("net");

        if (caseType == null) {
            String caseTypeId = wfnet.getChild("id").getChildText("value");
            caseType = new CaseType(caseTypeId);
        }
        
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
        
        Map attributes = parseAttributes(
            wfnet.getChild("initialAttributes").getChildText("value"));
        
        caseType.buildTemplate(attributes);
        
        return caseType;
    }

    /**
     * Parses the initial attributes from the text present in the PNML
     * file. <p>
     * 
     * @param attributesText the text encoding the initial attributes. 
     * @return a map with the initial attributes.
     */
    private Map parseAttributes(String attributesText) {
        Map attributes = new HashMap();
        StringTokenizer t1 = new StringTokenizer(attributesText, ",");
        while (t1.hasMoreTokens()) {
            StringTokenizer t2 =
                new StringTokenizer(t1.nextToken().trim(), "=");
            String attributeName = t2.nextToken();
            String attributeValue = t2.nextToken();
            try {
                attributes.put(attributeName, Integer.valueOf(attributeValue));
            } catch (NumberFormatException e) {
                attributes.put(attributeName, attributeValue);
            }
        }
        return attributes;
    }
}
