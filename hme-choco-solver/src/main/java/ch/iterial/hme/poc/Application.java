package ch.iterial.hme.poc;

import ch.iterial.hme.poc.model.Problem;
import ch.iterial.hme.poc.model.problem.BandwidthProblem;
import ch.iterial.hme.poc.model.problem.CriticalDeviceProblem;
import ch.iterial.hme.poc.model.problem.MaxLatencyProblem;
import ch.iterial.hme.poc.model.problem.TimeFrameLatencyProblem;
import ch.iterial.hme.poc.service.InputDataReader;
import ch.iterial.hme.poc.ui.SolverFrame;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class Application {

    public static void main(final String... args) {

        final Properties config = loadConfig();

        final String dataFolderProperty = config.getProperty("resources.xml-folder");
        final String maxDeltaProperty = config.getProperty("problems.max-latency.critical-delta");
        final String criticalDeviceProperty = config.getProperty("problems.critical-device-latency.ids");
        final String criticalDeltaProperty = config.getProperty("problems.devices-number.critical-delta");
        final String timeFrameSecondsProperty = config.getProperty("problems.devices-number.time-frame-sec");
        final String criticalNumberProperty = config.getProperty("problems.devices-number.critical-uid-number");

        final File dataFolder = absoluteFile(dataFolderProperty);
        final InputDataReader inputDataReader = new InputDataReader(dataFolder.getAbsolutePath());

        final Map<Class<? extends Problem>, Problem> problems = new HashMap<>();

        final MaxLatencyProblem maxLatencyProblem = new MaxLatencyProblem(Integer.parseInt(maxDeltaProperty));
        final CriticalDeviceProblem criticalDeviceProblem = new CriticalDeviceProblem(Arrays
                .asList(StringUtils.split(criticalDeviceProperty, ','))
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList()));
        final BandwidthProblem bandwidthProblem = new BandwidthProblem();
        final TimeFrameLatencyProblem timeFrameLatencyProblem = new TimeFrameLatencyProblem(
                Integer.parseInt(criticalDeltaProperty),
                Integer.parseInt(timeFrameSecondsProperty),
                Integer.parseInt(criticalNumberProperty)
        );

        putToMap(problems, BandwidthProblem.class, bandwidthProblem);
        putToMap(problems, MaxLatencyProblem.class, maxLatencyProblem);
        putToMap(problems, CriticalDeviceProblem.class, criticalDeviceProblem);
        putToMap(problems, TimeFrameLatencyProblem.class, timeFrameLatencyProblem);

        SwingUtilities.invokeLater(() -> {
            final SolverFrame frame = new SolverFrame(inputDataReader, problems);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(new Dimension(800, 600));
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
        });
    }

    private static <T extends Problem> void putToMap(final Map<Class<? extends Problem>, Problem> map, final Class<T> key, final T value) {
        map.put(key, value);
    }

    private static Properties loadConfig() {
        try (final InputStream input = new FileSystemResource(absoluteFile("config.properties")).getInputStream()) {
            final Properties properties = new Properties();

            properties.load(input);

            return properties;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static File absoluteFile(final String path) {
        final File testFile = new File(path);
        if (testFile.isAbsolute()) {
            if (testFile.exists()) {
                return testFile;
            }
        }
        final File workspace = new File(System.getProperty("user.dir"));
        if (workspace.exists()) {
            final File workspaceFolder = new File(workspace, path);
            if (workspaceFolder.exists()) {
                return workspaceFolder;
            }
        }
        final Resource testResource = new ClassPathResource(path);
        if (testResource.exists()) {
            try {
                return testResource.getFile();
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        throw new IllegalArgumentException();
    }

}
