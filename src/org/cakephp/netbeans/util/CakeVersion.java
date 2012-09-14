/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2012 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2012 Sun Microsystems, Inc.
 */
package org.cakephp.netbeans.util;

import java.io.IOException;
import org.cakephp.netbeans.CakePhpFrameworkProvider;
import org.netbeans.modules.php.api.phpmodule.PhpModule;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

/**
 *
 * @author junichi11
 */
public class CakeVersion {

    private int mejor;
    private int minor;
    private int revision;
    private String notStable;
    private static CakeVersion INSTANCE = null;
    private static PhpModule pm;

    private CakeVersion(PhpModule pm) {
        CakeVersion.pm = pm;
        String[] split = getCakePhpVersionSplit(pm);
        switch (split.length) {
            case 4:
                notStable = split[3];
            case 3:
                revision = Integer.parseInt(split[2]);
            case 2:
                minor = Integer.parseInt(split[1]);
            case 1:
                mejor = Integer.parseInt(split[0]);
                break;
            case 0:
            default:
                mejor = -1;
                minor = -1;
                revision = -1;
                notStable = ""; // NOI18N
                break;
        }
    }

    public static CakeVersion getInstance(PhpModule pm) {
        if (INSTANCE == null || CakeVersion.pm != pm) {
            INSTANCE = new CakeVersion(pm);
        }
        return INSTANCE;
    }

    public int getMejor() {
        return mejor;
    }

    public int getMinor() {
        return minor;
    }

    public int getRevision() {
        return revision;
    }

    public String getNotStable() {
        return notStable;
    }

    /**
     * Check CakePHP mejor version
     *
     * @param mejorVersion
     * @return
     */
    public boolean isCakePhp(int mejorVersion) {
        return mejor == mejorVersion;
    }

    /**
     * Get CakePHP version.
     *
     * @param PhpModule phpModule
     * @return String If can't get the version file, return null.
     */
    private String getCakePhpVersion(PhpModule phpModule) {
        FileObject root = CakePhpFrameworkProvider.getCakePhpDirectory(phpModule);
        FileObject cake = root.getFileObject("cake"); // NOI18N
        FileObject version;
        if (cake != null) {
            version = root.getFileObject("cake/VERSION.txt"); // NOI18N
        } else {
            version = root.getFileObject("lib/Cake/VERSION.txt"); // NOI18N
        }
        if (version == null) {
            return null;
        }
        try {
            String versionNumber = null;
            for (String line : version.asLines("UTF-8")) { // NOI18N
                if (!line.contains("//") && !line.equals("")) { // NOI18N
                    line = line.trim();
                    versionNumber = line;
                    break;
                }
            }
            return versionNumber;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    private String[] getCakePhpVersionSplit(PhpModule phpModule) {
        String version = getCakePhpVersion(phpModule);
        if (version == null) {
            return null;
        }
        return version.split("[., -]"); // NOI18N
    }
}
