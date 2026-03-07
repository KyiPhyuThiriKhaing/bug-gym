package dev.philixtheexplorer.buggym.application;

/**
 * Resolves the runtime app version from package metadata and system properties.
 */
public final class AppVersionResolver {

    private AppVersionResolver() {
    }

    public static String resolve(Class<?> ownerClass) {
        String fromManifest = ownerClass.getPackage() != null
                ? ownerClass.getPackage().getImplementationVersion()
                : null;
        if (fromManifest != null && !fromManifest.isBlank()) {
            return fromManifest;
        }

        String fromProperty = System.getProperty("app.version");
        if (fromProperty != null && !fromProperty.isBlank()) {
            return fromProperty;
        }

        return "dev";
    }
}
