package de.schleger.boiler.log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.schleger.boiler.information.InformationUpdater;
import de.schleger.boiler.time.TimeProvider;

public class LogFileChecker implements InformationUpdater {
    private static final int LOG_FILE_CHECK_INTERVAL_IN_DAYS = 1;

    private static final Logger LOG = LogManager.getLogger(LogFileChecker.class);

    private File logDirectory;
    private int maxLogFileAliveTime;
    private TimeProvider timeProvider;
    private List<LogDescriptor> logList;

    private LocalDateTime timeLastFileCheck;

    public LogFileChecker(TimeProvider timeProvider, File logDirectory, int maxLogFileAliveTime,
            LogDescriptor... logDescriptor) {
        this.timeProvider = timeProvider;
        this.logDirectory = logDirectory;
        this.maxLogFileAliveTime = maxLogFileAliveTime;
        logList = Arrays.asList(logDescriptor);
    }

    @Override
    public void update() {
        if (hasCheckerToRun()) {
            checkAndDeleteOldFiles();
        }
    }

    private boolean hasCheckerToRun() {
        if (timeLastFileCheck == null) {
            return true;
        }

        return timeLastFileCheck.plusDays(LOG_FILE_CHECK_INTERVAL_IN_DAYS).isBefore(timeProvider.getTime());
    }

    public void checkAndDeleteOldFiles() {
        try {
            // Checkt jedes File ob es gelöscht werden muss
            String[] paths = logDirectory.list();
            for (String filePath : paths) {
                Path completeFilePath = new File(logDirectory + "/" + filePath).toPath();

                for (LogDescriptor logDescriptor : logList) {
                    if (filePath.contains(logDescriptor.getPath())) {
                        if (isFileOldEnoughToDelete(completeFilePath, logDescriptor.getLifeTimeInDays())) {
                            deleteFile(completeFilePath);
                        }
                    } else {
                        if (isFileOldEnoughToDelete(completeFilePath, maxLogFileAliveTime)) {
                            deleteFile(completeFilePath);
                        }
                    }
                }
            }
            // Update Time
            timeLastFileCheck = timeProvider.getTime();
        } catch (Exception e) {
            LOG.log(Level.ERROR, "Fehler beim prüfen der Logfiles", e);
        }
    }

    private void deleteFile(Path completeFilePath) throws IOException {
        LOG.log(Level.INFO, "lösche Log File: " + completeFilePath);
        Files.delete(completeFilePath);
    }

    private boolean isFileOldEnoughToDelete(Path completeFilePath, int logLifeTimeInDays) throws IOException {
        Instant instant = Instant.ofEpochMilli(Files.getLastModifiedTime(completeFilePath).toMillis());
        LocalDateTime LastModifiedTimeOfFile = LocalDateTime.ofInstant(instant, ZoneOffset.systemDefault());

        if (LastModifiedTimeOfFile.plusDays(logLifeTimeInDays).isBefore(timeProvider.getTime())) {
            return true;
        }

        return false;
    }
}