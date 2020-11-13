package eu.kartoffelquadrat.etlplayground;

/**
 * Helper class to determine whether this program instance was launched from a JAR or natively form file system.
 *
 * @author Maximilian Schiedermeier
 */
public class LaunchModeDetector {
    public static boolean isRunningFromJar() {
        return LaunchModeDetector.class.getResource("LaunchModeDetector.class").toString().startsWith("jar");
    }
}
