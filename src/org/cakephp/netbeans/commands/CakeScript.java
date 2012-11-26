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
package org.cakephp.netbeans.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cakephp.netbeans.module.CakePhpModule;
import org.cakephp.netbeans.preferences.CakePreferences;
import org.cakephp.netbeans.util.CakeVersion;
import org.netbeans.api.extexecution.ExecutionDescriptor;
import org.netbeans.api.extexecution.input.InputProcessor;
import org.netbeans.api.extexecution.input.InputProcessors;
import org.netbeans.api.extexecution.input.LineProcessor;
import org.netbeans.modules.php.api.executable.InvalidPhpExecutableException;
import org.netbeans.modules.php.api.executable.PhpExecutable;
import org.netbeans.modules.php.api.executable.PhpExecutableValidator;
import org.netbeans.modules.php.api.phpmodule.PhpModule;
import org.netbeans.modules.php.api.util.UiUtils;
import org.netbeans.modules.php.spi.framework.commands.FrameworkCommand;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle;
import org.openide.windows.InputOutput;
import org.xml.sax.SAXException;

public final class CakeScript {

    static final Logger LOGGER = Logger.getLogger(CakeScript.class.getName());
    public static final String OPTIONS_SUB_PATH = "CakePhp"; // NOI18N
    public static final String SCRIPT_NAME = "cake"; // NOI18N
    public static final String SCRIPT_NAME_LONG = SCRIPT_NAME + ".php"; // NOI18N
    private static final String HELP_COMMAND = "--help"; // NOI18N
    private static final String BAKE_COMMAND = "bake"; // NOI18N
    private static final String LIST_COMMAND = "command_list"; // NOI18N
    private static final List<String> LIST_XML_COMMAND = Arrays.asList(LIST_COMMAND, "--xml"); // NOI18N
    // XXX any default params?
    private static final List<String> DEFAULT_PARAMS = Collections.emptyList();
    private static final String CORE_SHELLS_DIRECTORY = "cake/console/libs"; // NOI18N
    private static final String VENDORS_SHELLS_DIRECTORY = "vendors/shells"; // NOI18N
    private final String cakePath;
    private List<String> appPrams = new ArrayList<String>();

    private CakeScript(String cakePath) {
        this.cakePath = cakePath;
    }

    /**
     * Get the project specific, <b>valid only</b> Cake script. If not found,
     * {@code null} is returned.
     *
     * @param phpModule PHP module for which Cake script is taken
     * @param warn <code>true</code> if user is warned when the Cake script is
     * not valid
     * @return Cake console script or {@code null} if the script is not valid
     */
    @NbBundle.Messages({
        "# {0} - error message",
        "CakeScript.script.invalid=<html>Project''s Cake script is not valid.<br>({0})"
    })
    public static CakeScript forPhpModule(PhpModule phpModule, boolean warn) throws InvalidPhpExecutableException {
        String console = null;
        FileObject script = getPath(phpModule);
        if (script != null) {
            console = FileUtil.toFile(script).getAbsolutePath();
        }
        String error = validate(console);
        if (error == null) {
            return new CakeScript(console);
        }
        if (warn) {
            NotifyDescriptor.Message message = new NotifyDescriptor.Message(
                    Bundle.CakeScript_script_invalid(error),
                    NotifyDescriptor.WARNING_MESSAGE);
            DialogDisplayer.getDefault().notify(message);
        }
        throw new InvalidPhpExecutableException(error);
    }

    private static FileObject getPath(PhpModule phpModule) {
        CakePhpModule module = CakePhpModule.forPhpModule(phpModule);
        FileObject consoleDirectory = module.getConsoleDirectory(CakePhpModule.DIR_TYPE.APP);
        if (consoleDirectory == null) {
            LOGGER.log(Level.WARNING, "Not found " + SCRIPT_NAME);
            return null;
        }
        return consoleDirectory.getFileObject(SCRIPT_NAME_LONG);
    }

    @NbBundle.Messages("CakeScript.script.label=Cake script")
    public static String validate(String command) {
        return PhpExecutableValidator.validateCommand(command, Bundle.CakeScript_script_label());
    }

    public static String getOptionsPath() {
        return UiUtils.OPTIONS_PATH + "/" + OPTIONS_SUB_PATH; // NOI18N
    }

    public void runCommand(PhpModule phpModule, List<String> parameters, Runnable postExecution) {
        if (!CakePreferences.getAppName(phpModule).equals("app")) { // NOI18N
            FileObject app = CakePhpModule.forPhpModule(phpModule).getDirectory(CakePhpModule.DIR_TYPE.APP);
            appPrams.add("-app"); // NOI18N
            appPrams.add(app.getPath());
        }
        createPhpExecutable(phpModule)
                .displayName(getDisplayName(phpModule, parameters.get(0)))
                .additionalParameters(getAllParams(parameters))
                .run(getDescriptor(postExecution));
    }

    public String getHelp(PhpModule phpModule, String[] params) {
        assert phpModule != null;

        List<String> allParams = new ArrayList<String>();
        allParams.addAll(Arrays.asList(params));
        allParams.add(HELP_COMMAND);

        HelpLineProcessor lineProcessor = new HelpLineProcessor();
        Future<Integer> result = createPhpExecutable(phpModule)
                .displayName(getDisplayName(phpModule, allParams.get(0)))
                .additionalParameters(getAllParams(allParams))
                .run(getSilentDescriptor(), getOutProcessorFactory(lineProcessor));
        try {
            if (result != null) {
                result.get();
            }
        } catch (CancellationException ex) {
            // canceled
        } catch (ExecutionException ex) {
            UiUtils.processExecutionException(ex, OPTIONS_SUB_PATH);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        return lineProcessor.getHelp();
    }

    public List<FrameworkCommand> getCommands(PhpModule phpModule) {
        List<FrameworkCommand> freshCommands = getFrameworkCommandsInternalXml(phpModule);
        if (freshCommands != null) {
            return freshCommands;
        }
        freshCommands = getFrameworkCommandsInternalConsole(phpModule);
        if (freshCommands != null) {
            return freshCommands;
        }
        // XXX some error => rerun command with console
        if (CakeVersion.getInstance(phpModule).isCakePhp(2)) {
            runCommand(phpModule, Collections.singletonList(LIST_COMMAND), null);
        }
        return null;
    }

    public void bake(PhpModule phpModule) {
        runCommand(phpModule, Collections.singletonList(BAKE_COMMAND), null);
    }

    private PhpExecutable createPhpExecutable(PhpModule phpModule) {
        return new PhpExecutable(cakePath)
                .workDir(FileUtil.toFile(CakePhpModule.getCakePhpDirectory(phpModule)));
    }

    private List<String> getAllParams(List<String> params) {
        List<String> allParams = new ArrayList<String>();
        allParams.addAll(DEFAULT_PARAMS);
        allParams.addAll(appPrams);
        allParams.addAll(params);
        return allParams;
    }

    @NbBundle.Messages({
        "# {0} - project name",
        "# {1} - command",
        "CakeScript.command.title={0} ({1})"
    })
    private String getDisplayName(PhpModule phpModule, String command) {
        return Bundle.CakeScript_command_title(phpModule.getDisplayName(), command);
    }

    private ExecutionDescriptor getDescriptor(Runnable postExecution) {
        ExecutionDescriptor executionDescriptor = PhpExecutable.DEFAULT_EXECUTION_DESCRIPTOR
                .optionsPath(OPTIONS_SUB_PATH);
        if (postExecution != null) {
            executionDescriptor = executionDescriptor.postExecution(postExecution);
        }
        return executionDescriptor;
    }

    private ExecutionDescriptor.InputProcessorFactory getOutProcessorFactory(final LineProcessor lineProcessor) {
        return new ExecutionDescriptor.InputProcessorFactory() {
            @Override
            public InputProcessor newInputProcessor(InputProcessor defaultProcessor) {
                return InputProcessors.ansiStripping(InputProcessors.bridge(lineProcessor));
            }
        };
    }

    private ExecutionDescriptor getSilentDescriptor() {
        return new ExecutionDescriptor()
                .inputOutput(InputOutput.NULL);
    }

    private List<FrameworkCommand> getFrameworkCommandsInternalXml(PhpModule phpModule) {
        File tmpFile;
        try {
            tmpFile = File.createTempFile("nb-cake-commands-", ".xml"); // NOI18N
            tmpFile.deleteOnExit();
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, null, ex);
            return null;
        }
        if (!redirectToFile(phpModule, tmpFile, LIST_XML_COMMAND)) {
            return null;
        }
        List<CakeCommandItem> commandsItem = new ArrayList<CakeCommandItem>();
        try {
            CakePhpCommandXmlParser.parse(tmpFile, commandsItem);
        } catch (SAXException ex) {
            // incorrect xml provided by cakephp?
            LOGGER.log(Level.INFO, null, ex);
        }
        if (commandsItem.isEmpty()) {
            // error
            tmpFile.delete();
            return null;
        }
        // parse each command
        List<FrameworkCommand> commands = new ArrayList<FrameworkCommand>();
        for (CakeCommandItem item : commandsItem) {
            if (!redirectToFile(phpModule, tmpFile, Arrays.asList(item.getCommand(), HELP_COMMAND, "xml"))) { // NOI18N
                commands.add(new CakePhpCommand(phpModule,
                        item.getCommand(), item.getDescription(), item.getDisplayName()));
                continue;
            }
            List<CakeCommandItem> mainCommandsItem = new ArrayList<CakeCommandItem>();
            try {
                CakePhpCommandXmlParser.parse(tmpFile, mainCommandsItem);
            } catch (SAXException ex) {
                LOGGER.log(Level.WARNING, "Xml file Error:{0}", ex.getMessage());
                commands.add(new CakePhpCommand(phpModule,
                        item.getCommand(), item.getDescription(), item.getDisplayName()));
                continue;
            }
            if (mainCommandsItem.isEmpty()) {
                tmpFile.delete();
                return null;
            }
            // add main command
            CakeCommandItem main = mainCommandsItem.get(0);
            String mainCommand = main.getCommand();
            String provider = item.getDescription();
            if (!provider.equals("CORE") && !provider.matches("^[a-z0-9-_]+")) { // NOI18N
                mainCommand = provider + "." + mainCommand;
            }
            commands.add(new CakePhpCommand(phpModule,
                    mainCommand, "[" + provider + "] " + main.getDescription(), main.getDisplayName())); // NOI18N

            // add subcommands
            List<CakeCommandItem> subcommands = main.getSubcommands();
            if (subcommands == null) {
                continue;
            }
            for (CakeCommandItem subcommand : subcommands) {
                String[] command = {mainCommand, subcommand.getCommand()};
                commands.add(new CakePhpCommand(phpModule,
                        command, "[" + provider + "] " + subcommand.getDescription(), main.getCommand() + " " + subcommand.getDisplayName())); // NOI18N
            }
        }
        tmpFile.delete();
        return commands;
    }

    private boolean redirectToFile(PhpModule phpModule, File file, List<String> commands) {
        Future<Integer> result = createPhpExecutable(phpModule)
                .fileOutput(file, true)
                .warnUser(false)
                .additionalParameters(commands)
                .run(getSilentDescriptor());
        try {
            if (result == null || result.get() != 0) {
                // error
                return false;
            }
        } catch (CancellationException ex) {
            // canceled
            return false;
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return false;
        } catch (ExecutionException ex) {
            // ignored
            return false;
        }
        return true;
    }

    private List<FrameworkCommand> getFrameworkCommandsInternalConsole(PhpModule phpModule) {
        // cakephp1.3+
        List<FrameworkCommand> commands = new ArrayList<FrameworkCommand>();
        List<FileObject> shellDirs = new ArrayList<FileObject>();
        String[] shells = {CORE_SHELLS_DIRECTORY, VENDORS_SHELLS_DIRECTORY, CakePreferences.getAppName(phpModule) + "/" + VENDORS_SHELLS_DIRECTORY};

        for (String shell : shells) {
            FileObject shellFileObject = CakePhpModule.getCakePhpDirectory(phpModule).getFileObject(shell);
            if (shellFileObject != null) {
                shellDirs.add(shellFileObject);
            }
        }

        for (FileObject shellDir : shellDirs) {
            Enumeration<? extends FileObject> shellFiles = null;
            if (shellDir != null) {
                shellFiles = shellDir.getChildren(false);
            } else {
                return null;
            }
            if (shellFiles != null) {
                while (shellFiles.hasMoreElements()) {
                    FileObject shell = shellFiles.nextElement();
                    if (!shell.getName().equals("shell") && !shell.isFolder()) { // NOI18N
                        commands.add(new CakePhpCommand(phpModule, shell.getName(), "[" + getShellsPlace(phpModule, shellDir) + "]", shell.getName())); // NOI18N
                    }
                }
            }
        }
        return commands;
    }

    private String getShellsPlace(PhpModule phpModule, FileObject shellDir) {
        String place = ""; // NOI18N
        String app = CakePreferences.getAppName(phpModule);
        FileObject source = CakePhpModule.getCakePhpDirectory(phpModule);
        if (source.getFileObject(CORE_SHELLS_DIRECTORY) == shellDir) {
            place = "CORE"; // NOI18N
        } else if (source.getFileObject(app + "/" + VENDORS_SHELLS_DIRECTORY) == shellDir) {
            place = "APP VENDOR"; // NOI18N
        } else if (source.getFileObject(VENDORS_SHELLS_DIRECTORY) == shellDir) {
            place = "VENDOR"; // NOI18N
        }
        return place;
    }

    //~ Inner classes
    private static class HelpLineProcessor implements LineProcessor {

        private StringBuilder sb = new StringBuilder();

        @Override
        public void processLine(String line) {
            sb.append(line);
            sb.append("\n"); // NOI18N
        }

        @Override
        public void reset() {
        }

        @Override
        public void close() {
        }

        public String getHelp() {
            return sb.toString();
        }
    }
}
