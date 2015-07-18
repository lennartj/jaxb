package com.sun.xml.bind.v2.util;

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

/**
 * Simple argument validator, inspired by the commons-lang.
 *
 * @author <a href="mailto:lj@jguru.se">Lennart J&ouml;relid</a>
 */
public final class Validate {

    /**
     * Hide constructor for utility classes.
     */
    private Validate() {
    }

    /**
     * Validates that the supplied object is not null, and throws a NullPointerException otherwise.
     *
     * @param object       The object to validate for {@code null}-ness.
     * @param argumentName The argument name of the object to validate. If supplied (i.e. non-{@code null}),
     *                     this value is used in composing a better exception message.
     */
    public static void notNull(final Object object, final String argumentName) {
        if (object == null) {
            throw new NullPointerException(getMessage("null", argumentName));
        }
    }

    /**
     * Validates that the supplied object is not null, and throws an IllegalArgumentException otherwise.
     *
     * @param aString      The string to validate for emptyness.
     * @param argumentName The argument name of the object to validate.
     *                     If supplied (i.e. non-{@code null}), this value is used in composing
     *                     a better exception message.
     */
    public static void notEmpty(final String aString, final String argumentName) {

        // Check sanity
        notNull(aString, argumentName);

        if (aString.length() == 0) {
            throw new IllegalArgumentException(getMessage("empty", argumentName));
        }
    }

    /**
     * Validates that the supplied condition is true, and throws an IllegalArgumentException otherwise.
     *
     * @param condition The condition to validate for truth.
     * @param message   The exception message used within the IllegalArgumentException if the condition is false.
     */
    public static void isTrue(final boolean condition, final String message) {

        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    //
    // Private helpers
    //

    private static String getMessage(final String exceptionDefinition, final String argumentName) {
        return "Cannot handle "
                + exceptionDefinition
                + (argumentName == null ? "" : " '" + argumentName + "'")
                + " argument.";
    }
}

