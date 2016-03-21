package ch.iterial.hme.poc.service;

import ch.iterial.hme.poc.model.input.Timestamped;
import ch.iterial.hme.poc.util.XmlReadingUtils;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InputDataReader implements Iterator<Timestamped> {

    private final XStream stream = XmlReadingUtils.createXStreamInstance();
    private final List<File> inputFiles = new ArrayList<>();
    private final AtomicInteger fileIndex = new AtomicInteger(0);

    public InputDataReader(final String absoluteDataFolder) {
        final File dataFolder = new File(absoluteDataFolder);
        if (dataFolder.isAbsolute() && dataFolder.exists() && dataFolder.isDirectory()) {
            final File[] files = dataFolder.listFiles();
            if (ArrayUtils.isNotEmpty(files)) {
                inputFiles.addAll(Arrays.asList(files)
                        .stream()
                        .filter(file -> file.isFile() && StringUtils.endsWithIgnoreCase(file.getName(), ".xml"))
                        .map(file -> new XmlToInputTuple(file, Timestamped.class.cast(stream.fromXML(file))))
                        .sorted((tupleOne, tupleTwo) -> tupleOne.input.timestamp().compareTo(tupleTwo.input.timestamp()))
                        .map(tuple -> tuple.file)
                        .collect(Collectors.toList()));
            }
        }
    }

    public int currentIndex() {
        return fileIndex.get() + 1;
    }

    public int maxIndex() {
        return inputFiles.size();
    }

    public String currentFile() {
        return inputFiles.get(fileIndex.get()).getName();
    }

    @Override
    public boolean hasNext() {
        return fileIndex.get() < inputFiles.size();
    }

    @Override
    public Timestamped next() {
        return Timestamped.class.cast(stream.fromXML(inputFiles.get(fileIndex.getAndIncrement())));
    }

    private static final class XmlToInputTuple {
        private final File file;
        private final Timestamped input;

        private XmlToInputTuple(final File file, final Timestamped input) {
            this.file = file;
            this.input = input;
        }
    }

}
