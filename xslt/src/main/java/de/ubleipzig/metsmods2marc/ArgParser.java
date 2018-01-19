/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.ubleipzig.metsmods2marc;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.PrintWriter;
import java.util.stream.Stream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.fcrepo.client.FcrepoClient;
import org.slf4j.Logger;

/**
 * Command-line arguments parser.
 *
 * @author awoods
 * @author escowles
 * @author whikloj
 * @since 2016-08-29
 */
class ArgParser {

    private static final String BAG_CONFIG_OPTION_KEY = "bag-config";

    private static final String BAG_PROFILE_OPTION_KEY = "bag-profile";

    private static final Logger logger = getLogger(ArgParser.class);

    private static final Options configOptions = new Options();

    private static final Options configFileOptions = new Options();

    static {
        // Help option
        configOptions.addOption(Option.builder("h")
                .longOpt("help")
                .hasArg(false)
                .desc("Print these options")
                .required(false)
                .build());

        // Resource option
        configOptions.addOption(Option.builder("r")
                .longOpt("resource")
                .hasArg(true).numberOfArgs(1).argName("resource")
                .desc("Resource (URI) to import")
                .required(true).build());

        // Repository option
        configOptions.addOption(Option.builder("p")
                .longOpt("repository")
                .hasArg(true).numberOfArgs(1).argName("repository")
                .desc("Repository Base URI")
                .required(false).build());

        configOptions.addOption(Option.builder("d")
                .longOpt("dir")
                .hasArg(true).numberOfArgs(1).argName("dir")
                .desc("The directory to export to")
                .required(true).build());

        configOptions.addOption(Option.builder("b")
                .longOpt("bibframe")
                .hasArg(false)
                .desc("Export resource as BibFrame RDF")
                .required(false).build());
    }

    /**
     * Parse command line options based on the provide Options.
     *
     * @param configOptions valid set of Options
     * @param args command line arguments
     * @return the list of option and values
     * @throws ParseException if invalid/missing option is found
     */
    private static CommandLine parseConfigArgs(final Options configOptions, final String[] args)
            throws ParseException {
        return new DefaultParser().parse(configOptions, args);
    }

    /**
     * Check for a help flag.
     *
     * @param args command line arguments
     * @return whether the help flag was found.
     */
    private boolean helpFlagged(final String[] args) {
        return Stream.of(args).anyMatch(x -> x.equals("-h") || x.equals("--help"));
    }

    /**
     * Parse command line arguments into a Config object.
     *
     * @param args command line arguments
     * @return the parsed config file or command line args.
     */
    private Config parseConfiguration(final String[] args) {
        // first see if they've specified a config file
        CommandLine c = null;
        Config config = null;
        // check for presence of the help flag
        if (helpFlagged(args)) {
            printHelp(null);
        }

        try {
            c = parseConfigArgs(configOptions, args);
            config = parseConfigurationArgs(c);
            addSharedOptions(c, config);
        } catch (final ParseException e) {
            printHelp("Error parsing args: " + e.getMessage());
        }

        return config;
    }

    /**
     * This method parses the command-line args.
     *
     * @param cmd command line options
     * @return Config
     */
    private Config parseConfigurationArgs(final CommandLine cmd) {
        final Config config = new Config();

        config.setResource(cmd.getOptionValue('r'));
        config.setBaseDirectory(cmd.getOptionValue('d'));
        config.setBibFrame(cmd.hasOption('b'));
        if (cmd.hasOption('p')) {
            config.setRepositoryBaseUri(cmd.getOptionValue('p'));
        }
        return config;
    }

    /**
     * This method add/updates the values of any options that may be
     * valid in either scenario (config file or fully command line).
     *
     * @param cmd a parsed command line
     * @return Config the config which may be updated
     */
    private void addSharedOptions(final CommandLine cmd, final Config config) {
        final String user = cmd.getOptionValue("user");
        if (user != null) {
            if (user.indexOf(':') == -1) {
                printHelp("user option must be in the format username:password");
            } else {
                config.setUsername(user.substring(0, user.indexOf(':')));
                config.setPassword(user.substring(user.indexOf(':') + 1));
            }
        }
    }


    /**
     * Parse command-line arguments.
     *
     * @param args Command-line arguments
     * @return A configured Importer or Exporter instance.
     **/
    TransferProcess parse(final String[] args) {
        final Config config = parseConfiguration(args);
        return new Ingester(config);
    }

    /**
     * Get a new client.
     *
     * @return fcrepoClient
     */
    private FcrepoClient.FcrepoClientBuilder clientBuilder() {
        return FcrepoClient.client();
    }

    /**
     * Print help/usage information.
     *
     * @param message the message or null for none
     */
    private void printHelp(final String message) {
        final HelpFormatter formatter = new HelpFormatter();
        final PrintWriter writer = new PrintWriter(System.out);
        if (message != null) {
            writer.println("\n-----------------------\n" + message + "\n-----------------------\n");
        }

        writer.println("Running Import/Export Utility from command line arguments");
        formatter.printHelp(
                writer, 80, "xslt", "", configOptions, 4, 4, "", true);

        writer.println("\n--- or ---\n");

        writer.println("Running Import/Export Utility from configuration file");
        formatter.printHelp(
                writer, 80, "xslt", "", configFileOptions, 4, 4, "", true);

        writer.println("\n");
        writer.flush();

        if (message != null) {
            throw new RuntimeException(message);
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * Parse command-line arguments.
     *
     * @param args Command-line arguments
     * @return A configured Ingester instance.
     **/
    TransferProcess init(final String[] args) {
        final Config config = parseConfiguration(args);
        return new Ingester(config);
    }

}
