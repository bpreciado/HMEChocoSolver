package ch.iterial.hme.poc.util;

import org.springframework.core.io.ClassPathResource;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.io.IOException;

public abstract class ImageLoadingUtils {

    public static Icon fromFile(final String filename) {
        try {
            return new ImageIcon(new ClassPathResource(filename).getURL());
        } catch (final IOException e) {
            return null;
        }
    }

}
