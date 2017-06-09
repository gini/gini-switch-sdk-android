package net.gini.tariffsdk.utils;


public class ExifUtils {

    private ExifUtils() {
        throw new IllegalStateException("No Instance of " + ExifUtils.class.getName());
    }

    /**
     * Gives you the amount of degrees that you have to rotate the image to display it correct from
     * the exif tag. Note that only not flipped exif tags are supported.
     */
    public static int getDegreesFromExif(final int exif) {
        switch (exif) {
            case 1:
                return 0;
            case 3:
                return 180;
            case 6:
                return 90;
            case 8:
                return 270;
        }
        return 0;
    }

    /**
     * Gives you the exif tag depending on the degrees of rotation. You should do a %360 on your
     * degrees, since this method only handles rotations <= 270 degrees.
     */
    public static int getExifFromDegrees(int degrees) {
        switch (degrees) {
            case 0:
                return 1; // 0CW
            case 90:
                return 6; // 270CW
            case 180:
                return 3; // 180CW
            case 270:
                return 8; // 90CW
            default:
                return 1; // 0CW
        }
    }
}
