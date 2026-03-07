package dev.philixtheexplorer.buggym.application;

import dev.philixtheexplorer.buggym.service.UpdateService;
import javafx.concurrent.Task;

/**
 * Application use case for checking update availability.
 */
public class UpdateCheckUseCase {

    private final UpdateService updateService;

    public UpdateCheckUseCase(UpdateService updateService) {
        this.updateService = updateService;
    }

    public Task<UpdateCheckResult> createCheckTask(String currentVersion) {
        return new Task<>() {
            @Override
            protected UpdateCheckResult call() throws Exception {
                String latestVersion = updateService.fetchLatestVersion();
                if (latestVersion == null) {
                    return new UpdateCheckResult(Status.VERSION_UNAVAILABLE, currentVersion, null);
                }

                if ("dev".equals(currentVersion)
                        || updateService.compareVersions(currentVersion, latestVersion) < 0) {
                    return new UpdateCheckResult(Status.UPDATE_AVAILABLE, currentVersion, latestVersion);
                }

                return new UpdateCheckResult(Status.UP_TO_DATE, currentVersion, latestVersion);
            }
        };
    }

    public enum Status {
        VERSION_UNAVAILABLE,
        UPDATE_AVAILABLE,
        UP_TO_DATE
    }

    public record UpdateCheckResult(Status status, String currentVersion, String latestVersion) {
    }
}
