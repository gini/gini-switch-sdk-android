package net.gini.switchsdk.utils;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExifUtilsTest {

    private List<Integer> mUnsupportedDegrees;
    private List<Integer> mUnsupportedExifTags;

    @Test
    public void getDegreesFromExif_shouldReturn0IfTagIs1() {
        int degreesFromExif = ExifUtils.getDegreesFromExif(1);
        assertEquals("Supported Exif tag 1 should return 0.", 0, degreesFromExif);
    }

    @Test
    public void getDegreesFromExif_shouldReturn0IfTagIsNotSupported() {
        for (Integer unsupportedExifTag : mUnsupportedExifTags) {
            int degreesFromExif = ExifUtils.getDegreesFromExif(unsupportedExifTag);
            assertEquals("Unsupported Exif tag should return 0.", 0, degreesFromExif);
        }
    }

    @Test
    public void getDegreesFromExif_shouldReturn180IfTagIs3() {
        int degreesFromExif = ExifUtils.getDegreesFromExif(3);
        assertEquals("Supported Exif tag 3 should return 180.", 180, degreesFromExif);
    }

    @Test
    public void getDegreesFromExif_shouldReturn270IfTagIs8() {
        int degreesFromExif = ExifUtils.getDegreesFromExif(8);
        assertEquals("Supported Exif tag 8 should return 270.", 270, degreesFromExif);
    }

    @Test
    public void getDegreesFromExif_shouldReturn90IfTagIs6() {
        int degreesFromExif = ExifUtils.getDegreesFromExif(6);
        assertEquals("Supported Exif tag 6 should return 90.", 90, degreesFromExif);
    }

    @Test
    public void getExifFromDegrees_shouldReturn1IfDegreeIs0() {
        int exifTag = ExifUtils.getExifFromDegrees(0);
        assertEquals("Supported degree 0 should return 1.", 1, exifTag);
    }

    @Test
    public void getExifFromDegrees_shouldReturn1IfDegreeIsNotSupported() {
        for (Integer unsupportedExifTag : mUnsupportedDegrees) {
            int exifTag = ExifUtils.getExifFromDegrees(unsupportedExifTag);
            assertEquals("Unsupported degree should return 1.", 1, exifTag);
        }
    }

    @Test
    public void getExifFromDegrees_shouldReturn3IfDegreeIs180() {
        int exifTag = ExifUtils.getExifFromDegrees(180);
        assertEquals("Supported degree 180 should return 3.", 3, exifTag);
    }

    @Test
    public void getExifFromDegrees_shouldReturn6IfDegreeIs90() {
        int exifTag = ExifUtils.getExifFromDegrees(90);
        assertEquals("Supported degree 90 should return 6.", 6, exifTag);
    }

    @Test
    public void getExifFromDegrees_shouldReturn8IfDegreeIs270() {
        int exifTag = ExifUtils.getExifFromDegrees(270);
        assertEquals("Supported degree 270 should return 8.", 8, exifTag);
    }

    @Before
    public void setUp() throws Exception {

        mUnsupportedExifTags = new ArrayList<>();
        for (int i = -10; i < 10; i++) {
            mUnsupportedExifTags.add(i);
        }
        final List<Integer> supportedExifTags = Arrays.asList(1, 3, 6, 8);
        mUnsupportedExifTags.removeAll(supportedExifTags);

        mUnsupportedDegrees = new ArrayList<>();
        for (int i = -360; i < 360; i++) {
            mUnsupportedDegrees.add(i);
        }
        final List<Integer> supportedDegrees = Arrays.asList(0, 90, 180, 270);
        mUnsupportedDegrees.removeAll(supportedDegrees);
    }
}