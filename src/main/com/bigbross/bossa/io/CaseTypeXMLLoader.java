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

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.bigbross.bossa.wfnet.CaseType;

/**
 * This class wraps an external XML case type definition. <p>
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
    
    public CaseType createCaseType() {
        
        // Get id, new case type.
        
        // Get places and create them.
        
        // Get transitions and create them.
        
        // Get arcs and create them.
        
        // Create attributes.
        
        // buildTemplate
        
        return null;
    }
}
