package eu.kartoffelquadrat.etlplayground;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Helper class to resolve relative resource locations to fully qualified URIs, required by epsilon. Automatically
 * adapts the generated URIs based on whether the context is a launch from the file system or from a self-contained
 * jar.
 *
 * @author Maximilian Schiedermeier
 */
public class ResourceUtils {

    /**
     * Generic URL resolver to generate URIs which allow reading provided resources, either from disk or JAR. This
     * method automatically figures out whether the current context is a JAR or the files system and adapts the
     * corresponding URIS.
     *
     * @param relativeResourceLocation
     * @return URI to the requested resource.
     * @throws URISyntaxException
     */
    public static URI getResourceURI(String relativeResourceLocation) throws URISyntaxException {

        // First case is for the transformation demo running straight on the file-system (not from a jar)
        // The generated URIs are ordinary references to the file-system.
        if (!LaunchModeDetector.isRunningFromJar()) {
            Path root;
            if (relativeResourceLocation.startsWith("/"))
                root = Paths.get(ResourceUtils.class.getResource("").toURI()).getRoot();
            else
                root = Paths.get(ResourceUtils.class.getResource("").toURI()).getParent().getParent().getParent();
            URI targetUri = root.resolve(relativeResourceLocation).toAbsolutePath().toUri();
            return targetUri;
        }

        // Second case is for the transformation demo running from a self-contained JAR.
        // The generated URIs are references to resources provided within the JAR.
        else {
            // Get URI to a file in the JAR
            URI classInJarUri = ResourceUtils.class.getResource("ResourceUtils.class").toURI();

            // Get URI to just the JAR
            String resourceBaseUri = classInJarUri.toString().split("!")[0];

            // Get URI-String to generic location where maven places all the resources, within the JAR.
            String resourcesUriString = resourceBaseUri + "!" + File.separator;

            // append relative file location, create corresponding URI
            String jarInternalFileUriString = resourcesUriString + relativeResourceLocation;
            URI jarInternalFileUri = new URI(jarInternalFileUriString);
            System.out.println(jarInternalFileUri);
            return jarInternalFileUri;
        }
    }
}
