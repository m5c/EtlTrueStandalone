package eu.kartoffelquadrat.etlplayground;

import java.io.File;
import java.io.IOException;

/**
 * Launcher class that prepares and triggers the demo model transformation.
 *
 * @author Maximilian Schiedermeier
 */
public class Launcher {

    /**
     * Requires two arguments. First argument: Location of input model. Second argument: Location where target model
     * shall be stored.
     *
     * @param args location of input and output model
     */
    public static void main(String[] args) {

        // Make sure an input model is provided and the output model does not yet exist.
        verifyInputParameters(args);

        // Extract provided model locations
        String inputModelLocation = args[0];
        String outputModelLocation = args[1];

        // Create an empty file where generated model shall be persisted. Epsilon is not smart enough to do this by
        // itself.
        prepareTargetFile(args[1]);

        // Create a transformer, based on provided ETL file. The returned object is capable of deep-copying trees.
        TreeTransformer transformer = new TreeTransformer();

        // Actually use the transformer object to create a deep copy of a provided input tree.
        transformer.transform(inputModelLocation, outputModelLocation);
    }

    /**
     * Verifies the URIs of the input / output model. Input model must exists. Output model is overwritten if it already exists.
     *
     * @param args
     */
    private static void verifyInputParameters(String[] args) {
        if (args == null || args.length != 2)
            throw new RuntimeException("Run arguments are not ok. This program requires two arguments. One: Location " +
                    "of input model (xmi). Two: Location where output model shall be stored.");

        if (!isExistentFile(args[0]))
            throw new RuntimeException("Input model (" + args[0] + ") not found.");

        if (isExistentFile(args[1]))
            eraseTargetFile(args[1]);
    }

    /**
     * Erases the target model in case it already exists at the specified target location.
     * @param location
     */
    private static void eraseTargetFile(String location)
    {
        File previousTargetFile = new File(location);
        previousTargetFile.delete();
        System.out.println("[Pre-existent target model erased]");
    }

    /**
     * Verifies if a file location provided as string already exists on disk.
     *
     * @param modelLocation
     * @return
     */
    private static boolean isExistentFile(String modelLocation) {
        File f = new File(modelLocation);
        return (f.exists() && f.isFile());
    }

    /**
     * Since ETL requires an existing file at the target location, this method can be used to prepare an empty file at
     * the desired location.
     *
     * @param location as the place on our hard drive where the empty file shall be created.
     */
    private static void prepareTargetFile(String location) {
        try {
            File file = new File(location);
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("Unable to create file at specified target location.");
        }
    }
}
