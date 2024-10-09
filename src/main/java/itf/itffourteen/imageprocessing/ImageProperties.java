package itf.itffourteen.imageprocessing;

import java.awt.Font;

public class ImageProperties {
    public static final int IMAGE_WIDTH = 300;
    public static final int IMAGE_HEIGHT = 140;
    public static final int BARCODE_HEIGHT = 100;
    public static final int BAR_WIDTH = 2;
    public static final int THICK_BAR_WIDTH = BAR_WIDTH * 2;
    public static final int BORDER_THICKNESS = THICK_BAR_WIDTH * 2;
    public static final String INVERTED_STOP_PATTERN = "00";
    public static final String FONT = "Arial";
    public static final int FONT_STYLE = Font.PLAIN;
    public static final int FONT_SIZE = 16;
    public static final int LABEL_X_COORDINATE = IMAGE_WIDTH / 24 * 7;
    public static final int LABEL_Y_COORDINATE = (IMAGE_HEIGHT + BARCODE_HEIGHT) / 2 + 13;
}
