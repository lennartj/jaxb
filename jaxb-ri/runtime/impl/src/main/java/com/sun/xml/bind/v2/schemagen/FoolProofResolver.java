/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.xml.bind.v2.schemagen;

import com.sun.xml.bind.v2.util.Validate;

import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>{@link SchemaOutputResolver} that wraps the user-specified resolver and makes sure that
 * it's following the contract.</p>
 * <p>This protects the rest of the {@link XmlSchemaGenerator} from client programming errors.</p>
 */
final class FoolProofResolver extends SchemaOutputResolver {

    // Internal state
    private static final Logger log = Logger.getLogger(FoolProofResolver.class.getName());
    private final SchemaOutputResolver resolver;

    /**
     * Compound constructor creating a FoolProofResolver delegating all work to the supplied SchemaOutputResolver.
     *
     * @param delegate A non-null SchemaOutputResolver instance.
     */
    public FoolProofResolver(final SchemaOutputResolver delegate) {

        // Check sanity
        Validate.notNull(delegate, "delegate");

        // Assign internal state
        this.resolver = delegate;
    }

    /**
     * {@inheritDoc}
     */
    public Result createOutput(final String namespaceUri, final String suggestedFileName) throws IOException {

        // Log somewhat
        log.entering(getClass().getName(), "createOutput", new Object[]{namespaceUri, suggestedFileName});

        final Result toReturn = resolver.createOutput(namespaceUri, suggestedFileName);
        if (toReturn != null) {

            final String sysId = toReturn.getSystemId();
            final String suffix = "(Namespace: [" + namespaceUri + "], Suggested FileName: ["
                    + suggestedFileName + "])";
            if(log.isLoggable(Level.FINER)) {
                log.finer("System ID: [" + sysId + "]. " + suffix);
            }

            // Check sanity
            if(sysId == null) {
                throw new AssertionError("System ID cannot be null. " + suffix);
            }

            // TODO: make sure that the system Id is absolute

            /*
             * 1) We cannot use java.net.URI, since it disallows some characters legally useable in
             *    file names (such as space).
             * 2) We cannot use java.net.URL, since it does not permit a made-up URI like kohsuke://foo/bar/zot
             */

        }

        log.exiting(getClass().getName(), "createOutput", toReturn);

        // All done.
        return toReturn;
    }
}
