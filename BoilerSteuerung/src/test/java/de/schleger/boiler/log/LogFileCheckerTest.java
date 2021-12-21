package de.schleger.boiler.log;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import de.schleger.boiler.time.DummyTimeProvider;

public class LogFileCheckerTest {
    private static final String LOG_PATH = "./logdir";
    private static final String DUMMY_FILE_PATH = "dummy.file";
    private static final String DUMMY_FILE_PATH_2 = "dummy2.file";
    private static final File DUMMY_FILE = new File(LOG_PATH, DUMMY_FILE_PATH);
    private static final File DUMMY_FILE_2 = new File(LOG_PATH, DUMMY_FILE_PATH_2);

    private DummyTimeProvider timeProvider;
    private LogFileChecker logDelete;

    @Before
    public void setUp() throws IOException {
        timeProvider = new DummyTimeProvider();
        logDelete = new LogFileChecker(timeProvider, new File(LOG_PATH), 365, new LogDescriptor(DUMMY_FILE_PATH, 1));

        Files.write(DUMMY_FILE.toPath(), "".getBytes(), StandardOpenOption.CREATE);
        Files.write(DUMMY_FILE_2.toPath(), "".getBytes(), StandardOpenOption.CREATE);
    }

    @Test
    public void loeschtAlleBeschriebenenFilesDieAltGenugSind() {
        timeProvider.setTime(LocalDateTime.now().plusDays(10));
        logDelete.update();

        assertThat(Files.exists(DUMMY_FILE.toPath()), equalTo(false));
    }

    @Test
    public void loeschtFilesNichtWennNochZuNeu() {
        timeProvider.setTime(LocalDateTime.now());
        logDelete.update();

        assertThat(Files.exists(DUMMY_FILE.toPath()), equalTo(true));
        assertThat(Files.exists(DUMMY_FILE_2.toPath()), equalTo(true));
    }

    @Test
    public void laeuftNichtDoppeltProTag() throws IOException {
        timeProvider.setTime(LocalDateTime.now().plusDays(10));
        logDelete.update();

        assertThat(Files.exists(DUMMY_FILE.toPath()), equalTo(false));

        Files.write(DUMMY_FILE.toPath(), "".getBytes(), StandardOpenOption.CREATE);

        timeProvider.setTime(LocalDateTime.now().plusDays(10).plusHours(12));
        logDelete.update();

        assertThat(Files.exists(DUMMY_FILE.toPath()), equalTo(true));
    }

    @Test
    public void laeuftEinmalProTag() throws IOException {
        timeProvider.setTime(LocalDateTime.now().plusDays(10));
        logDelete.update();

        assertThat(Files.exists(DUMMY_FILE.toPath()), equalTo(false));

        Files.write(DUMMY_FILE.toPath(), "".getBytes(), StandardOpenOption.CREATE);

        timeProvider.setTime(LocalDateTime.now().plusDays(11).plusMinutes(1));
        logDelete.update();

        assertThat(Files.exists(DUMMY_FILE.toPath()), equalTo(false));
    }
}
