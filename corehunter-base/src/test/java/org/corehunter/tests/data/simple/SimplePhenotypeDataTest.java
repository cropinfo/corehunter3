
package org.corehunter.tests.data.simple;

import static org.corehunter.tests.TestData.HEADERS_UNIQUE_NAMES;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.corehunter.data.simple.SimplePhenotypeData;
import org.jamesframework.core.subset.SubsetSolution;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import uno.informatics.common.ConversionException;
import uno.informatics.common.ConversionUtilities;
import uno.informatics.data.DataType;
import uno.informatics.data.Feature;
import uno.informatics.data.ScaleType;
import uno.informatics.data.SimpleEntity;
import uno.informatics.data.io.FileType;
import uno.informatics.data.pojo.FeaturePojo;
import uno.informatics.data.pojo.MethodPojo;
import uno.informatics.data.pojo.ScalePojo;

public class SimplePhenotypeDataTest {

    private static final String TEST_OUTPUT = "target/testoutput";

    protected final static Object[] OBJECT_ROW1 = new Object[] {
        1, 1.1, "R1C3", true, createDate("12/12/2012")
    };
    protected final static Object[] OBJECT_ROW2 = new Object[] {
        2, 2.2, "R2C3", false, createDate("13/12/2012")
    };
    protected final static Object[] OBJECT_ROW3 = new Object[] {
        3, 3.3, "R3C3", true, createDate("14/12/2012")
    };

    protected final static Object[] OBJECT_COL1 = new Object[] {
        OBJECT_ROW1[0], OBJECT_ROW2[0], OBJECT_ROW3[0]
    };

    protected final static Object[] OBJECT_COL2 = new Object[] {
        OBJECT_ROW1[1], OBJECT_ROW2[1], OBJECT_ROW3[1]
    };

    protected final static Object[] OBJECT_COL3 = new Object[] {
        OBJECT_ROW1[2], OBJECT_ROW2[2], OBJECT_ROW3[2]
    };

    protected final static Object[] OBJECT_COL4 = new Object[] {
        OBJECT_ROW1[3], OBJECT_ROW2[3], OBJECT_ROW3[3]
    };

    protected final static Object[] OBJECT_COL5 = new Object[] {
        OBJECT_ROW1[4], OBJECT_ROW2[4], OBJECT_ROW3[4]
    };

    protected static final List<Feature> OBJECT_FEATURES_MIN_MAX_COL = new ArrayList<Feature>();

    static {
        OBJECT_FEATURES_MIN_MAX_COL.add(new FeaturePojo("col1", "Col 1", new MethodPojo("col1", "Col 1",
            new ScalePojo("col1", "Col 1", DataType.INTEGER, ScaleType.INTERVAL, 0, 4, OBJECT_COL1))));
        OBJECT_FEATURES_MIN_MAX_COL.add(new FeaturePojo("col2", "Col 2", new MethodPojo("col2", "Col 2",
            new ScalePojo("col2", "Col 2", DataType.DOUBLE, ScaleType.RATIO, 0.0, 4.0))));
        OBJECT_FEATURES_MIN_MAX_COL.add(new FeaturePojo("col3", "Col 3", new MethodPojo("col3", "Col 3",
            new ScalePojo("col3", "Col 3", DataType.STRING, ScaleType.NOMINAL, OBJECT_COL3))));
        OBJECT_FEATURES_MIN_MAX_COL.add(new FeaturePojo("col4", "Col 4", new MethodPojo("col4", "Col 4",
            new ScalePojo("col4", "Col 4", DataType.BOOLEAN, ScaleType.NOMINAL, OBJECT_COL4))));
        OBJECT_FEATURES_MIN_MAX_COL.add(new FeaturePojo("col5", "Col 5", new MethodPojo("col5", "Col 5",
            new ScalePojo("col5", "Col 5", DataType.DATE, ScaleType.NOMINAL, OBJECT_COL5))));
    }

    protected static final List<List<Object>> OBJECT_TABLE_AS_LIST = new ArrayList<List<Object>>();

    static {
        OBJECT_TABLE_AS_LIST.add(new ArrayList<Object>(OBJECT_ROW1.length));

        OBJECT_TABLE_AS_LIST.get(0).add(OBJECT_ROW1[0]);
        OBJECT_TABLE_AS_LIST.get(0).add(OBJECT_ROW1[1]);
        OBJECT_TABLE_AS_LIST.get(0).add(OBJECT_ROW1[2]);
        OBJECT_TABLE_AS_LIST.get(0).add(OBJECT_ROW1[3]);
        OBJECT_TABLE_AS_LIST.get(0).add(OBJECT_ROW1[4]);

        OBJECT_TABLE_AS_LIST.add(new ArrayList<Object>(OBJECT_ROW2.length));

        OBJECT_TABLE_AS_LIST.get(1).add(OBJECT_ROW2[0]);
        OBJECT_TABLE_AS_LIST.get(1).add(OBJECT_ROW2[1]);
        OBJECT_TABLE_AS_LIST.get(1).add(OBJECT_ROW2[2]);
        OBJECT_TABLE_AS_LIST.get(1).add(OBJECT_ROW2[3]);
        OBJECT_TABLE_AS_LIST.get(1).add(OBJECT_ROW2[4]);

        OBJECT_TABLE_AS_LIST.add(new ArrayList<Object>(OBJECT_ROW2.length));

        OBJECT_TABLE_AS_LIST.get(2).add(OBJECT_ROW3[0]);
        OBJECT_TABLE_AS_LIST.get(2).add(OBJECT_ROW3[1]);
        OBJECT_TABLE_AS_LIST.get(2).add(OBJECT_ROW3[2]);
        OBJECT_TABLE_AS_LIST.get(2).add(OBJECT_ROW3[3]);
        OBJECT_TABLE_AS_LIST.get(2).add(OBJECT_ROW3[4]);
    }

    private static final int[] SOLUTION = new int[] {
        0, 2
    };

    private SimpleEntity[] expectedHeaders;
    private String dataName;

    @BeforeClass
    public static void beforeClass() {
        System.out.println("Test simple phenotype matrix data");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("Done");
    }

    @Test
    public void toCsvFileWithAllIds() throws IOException {
        expectedHeaders = HEADERS_UNIQUE_NAMES;

        SimplePhenotypeData phenotypeData = new SimplePhenotypeData("Phenotype Data",
            OBJECT_FEATURES_MIN_MAX_COL, OBJECT_TABLE_AS_LIST);

        List<Integer> ids = new ArrayList<Integer>(phenotypeData.getIDs());

        Path dirPath = Paths.get(TEST_OUTPUT);

        Files.createDirectories(dirPath);

        dirPath = Files.createTempDirectory(dirPath, "Phenotype-Csv-AllIds");

        // solution in natural order
        SubsetSolution solution = new SubsetSolution(new TreeSet<Integer>(ids));

        for (int i = 0; i < SOLUTION.length; ++i) {
            solution.select(SOLUTION[i]);
        }

        dataName = "out1.csv";

        Path path = Paths.get(dirPath.toString(), dataName);

        Files.deleteIfExists(path);

        System.out.println(" |- Write phenotype File (with solution) " + dataName);

        phenotypeData.writeData(path, FileType.CSV, solution, false, true, true);

        assertTrue("Output 1 is not correct!", FileUtils.contentEquals(new File(
            SimplePhenotypeDataTest.class.getResource("/phenotypes/out/Phenotype-Csv-AllIds1.csv").getPath()),
            path.toFile()));

        dataName = "out2.csv";

        path = Paths.get(dirPath.toString(), dataName);

        Files.deleteIfExists(path);

        System.out.println(" |- Write phenotype File (with solution) " + dataName);

        phenotypeData.writeData(path, FileType.CSV, solution, true, true, true);

        assertTrue("Output 2 is not correct!", FileUtils.contentEquals(new File(
            SimplePhenotypeDataTest.class.getResource("/phenotypes/out/Phenotype-Csv-AllIds2.csv").getPath()),
            path.toFile()));

        // solution in reverse natural order
        solution = new SubsetSolution(new TreeSet<Integer>(ids), Comparator.reverseOrder());

        for (int i = 0; i < SOLUTION.length; ++i) {
            solution.select(SOLUTION[i]);
        }

        dataName = "out3.csv";

        path = Paths.get(dirPath.toString(), dataName);

        Files.deleteIfExists(path);

        System.out.println(" |- Write phenotype File (with solution reverse order) " + dataName);

        phenotypeData.writeData(path, FileType.CSV, solution, false, true, true);

        assertTrue("Output 3 is not correct!", FileUtils.contentEquals(new File(
            SimplePhenotypeDataTest.class.getResource("/phenotypes/out/Phenotype-Csv-AllIds3.csv").getPath()),
            path.toFile()));

        dataName = "out4.csv";

        path = Paths.get(dirPath.toString(), dataName);

        Files.deleteIfExists(path);

        System.out.println(" |- Write phenotype File (with solution reverse order) " + dataName);

        phenotypeData.writeData(path, FileType.CSV, solution, true, true, true);

        assertTrue("Output 4 is not correct!", FileUtils.contentEquals(new File(
            SimplePhenotypeDataTest.class.getResource("/phenotypes/out/Phenotype-Csv-AllIds4.csv").getPath()),
            path.toFile()));
    }

    @Test
    public void toCsvFileWithSelectedlIds() throws IOException {
        expectedHeaders = HEADERS_UNIQUE_NAMES;

        SimplePhenotypeData phenotypeData = new SimplePhenotypeData("Phenotype Data",
            OBJECT_FEATURES_MIN_MAX_COL, OBJECT_TABLE_AS_LIST);

        List<Integer> ids = new ArrayList<Integer>(phenotypeData.getIDs());

        Path dirPath = Paths.get(TEST_OUTPUT);

        Files.createDirectories(dirPath);

        dirPath = Files.createTempDirectory(dirPath, "Phenotype-Csv-SelectedlIds");

        // solution in natural order
        SubsetSolution solution = new SubsetSolution(new TreeSet<Integer>(ids));

        for (int i = 0; i < SOLUTION.length; ++i) {
            solution.select(SOLUTION[i]);
        }

        dataName = "out1.csv";

        Path path = Paths.get(dirPath.toString(), dataName);

        Files.deleteIfExists(path);

        System.out.println(" |- Write phenotype File (with selected) " + dataName);

        phenotypeData.writeData(path, FileType.CSV, solution, false, true, false);

        assertTrue("Output 1 is not correct!",
            FileUtils.contentEquals(
                new File(SimplePhenotypeDataTest.class
                    .getResource("/phenotypes/out/Phenotype-Csv-SelectedlIds1.csv").getPath()),
                path.toFile()));

        dataName = "out2.csv";

        path = Paths.get(dirPath.toString(), dataName);

        Files.deleteIfExists(path);

        System.out.println(" |- Write phenotype File (with selected and index) " + dataName);

        phenotypeData.writeData(path, FileType.CSV, solution, true, true, false);

        assertTrue("Output 2 is not correct!",
            FileUtils.contentEquals(
                new File(SimplePhenotypeDataTest.class
                    .getResource("/phenotypes/out/Phenotype-Csv-SelectedlIds2.csv").getPath()),
                path.toFile()));

        // solution in reverse natural order
        solution = new SubsetSolution(new TreeSet<Integer>(ids), Comparator.reverseOrder());

        for (int i = 0; i < SOLUTION.length; ++i) {
            solution.select(SOLUTION[i]);
        }

        dataName = "out3.csv";

        path = Paths.get(dirPath.toString(), dataName);

        Files.deleteIfExists(path);

        System.out.println(" |- Write phenotype File (with selected reverse order) " + dataName);

        phenotypeData.writeData(path, FileType.CSV, solution, false, true, false);

        assertTrue("Output 3 is not correct!",
            FileUtils.contentEquals(
                new File(SimplePhenotypeDataTest.class
                    .getResource("/phenotypes/out/Phenotype-Csv-SelectedlIds3.csv").getPath()),
                path.toFile()));

        dataName = "out4.csv";

        path = Paths.get(dirPath.toString(), dataName);

        Files.deleteIfExists(path);

        System.out.println(" |- Write phenotype File (with selected reverse order and index) " + dataName);

        phenotypeData.writeData(path, FileType.CSV, solution, true, true, false);

        assertTrue("Output 4 is not correct!",
            FileUtils.contentEquals(
                new File(SimplePhenotypeDataTest.class
                    .getResource("/phenotypes/out/Phenotype-Csv-SelectedlIds4.csv").getPath()),
                path.toFile()));
    }

    @Test
    public void toCsvFileWithUnselectedlIds() throws IOException {
        expectedHeaders = HEADERS_UNIQUE_NAMES;

        SimplePhenotypeData phenotypeData = new SimplePhenotypeData("Phenotype Data",
            OBJECT_FEATURES_MIN_MAX_COL, OBJECT_TABLE_AS_LIST);

        List<Integer> ids = new ArrayList<Integer>(phenotypeData.getIDs());

        Path dirPath = Paths.get(TEST_OUTPUT);

        Files.createDirectories(dirPath);

        dirPath = Files.createTempDirectory(dirPath, "Phenotype-Csv-UnselectedlIds");

        // solution in natural order
        SubsetSolution solution = new SubsetSolution(new TreeSet<Integer>(ids));

        for (int i = 0; i < SOLUTION.length; ++i) {
            solution.select(SOLUTION[i]);
        }

        dataName = "out1.csv";

        Path path = Paths.get(dirPath.toString(), dataName);

        Files.deleteIfExists(path);

        System.out.println(" |- Write phenotype File (with unselected) " + dataName);

        phenotypeData.writeData(path, FileType.CSV, solution, false, false, true);

        assertTrue("Output 1 is not correct!",
            FileUtils.contentEquals(
                new File(SimplePhenotypeDataTest.class
                    .getResource("/phenotypes/out/Phenotype-Csv-UnselectedlIds1.csv").getPath()),
                path.toFile()));

        dataName = "out2.csv";

        path = Paths.get(dirPath.toString(), dataName);

        Files.deleteIfExists(path);

        System.out.println(" |- Write phenotype File (with unselected and index) " + dataName);

        phenotypeData.writeData(path, FileType.CSV, solution, true, false, true);

        assertTrue("Output 2 is not correct!",
            FileUtils.contentEquals(
                new File(SimplePhenotypeDataTest.class
                    .getResource("/phenotypes/out/Phenotype-Csv-UnselectedlIds2.csv").getPath()),
                path.toFile()));

        // solution in reverse natural order
        solution = new SubsetSolution(new TreeSet<Integer>(ids), Comparator.reverseOrder());

        for (int i = 0; i < SOLUTION.length; ++i) {
            solution.select(SOLUTION[i]);
        }

        dataName = "out3.csv";

        path = Paths.get(dirPath.toString(), dataName);

        Files.deleteIfExists(path);

        System.out.println(" |- Write phenotype File (with unselected reverse order) " + dataName);

        phenotypeData.writeData(path, FileType.CSV, solution, false, false, true);

        assertTrue("Output 3 is not correct!",
            FileUtils.contentEquals(
                new File(SimplePhenotypeDataTest.class
                    .getResource("/phenotypes/out/Phenotype-Csv-UnselectedlIds3.csv").getPath()),
                path.toFile()));

        dataName = "out4.csv";

        path = Paths.get(dirPath.toString(), dataName);

        Files.deleteIfExists(path);

        System.out.println(" |- Write phenotype File (with unselected reverse order and index) " + dataName);

        phenotypeData.writeData(path, FileType.CSV, solution, true, false, true);

        assertTrue("Output 4 is not correct!",
            FileUtils.contentEquals(
                new File(SimplePhenotypeDataTest.class
                    .getResource("/phenotypes/out/Phenotype-Csv-UnselectedlIds4.csv").getPath()),
                path.toFile()));
    }

    /**
     * @param string a data as a string
     * @return a data from string
     */
    private static Object createDate(String value) {
        try {
            return ConversionUtilities.convertToDate(value);
        } catch (ConversionException e) {
            return null;
        }
    }

}