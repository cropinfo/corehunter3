/*--------------------------------------------------------------*/
/* Licensed to the Apache Software Foundation (ASF) under one   */
/* or more contributor license agreements.  See the NOTICE file */
/* distributed with this work for additional information        */
/* regarding copyright ownership.  The ASF licenses this file   */
/* to you under the Apache License, Version 2.0 (the            */
/* "License"); you may not use this file except in compliance   */
/* with the License.  You may obtain a copy of the License at   */
/*                                                              */
/*   http://www.apache.org/licenses/LICENSE-2.0                 */
/*                                                              */
/* Unless required by applicable law or agreed to in writing,   */
/* software distributed under the License is distributed on an  */
/* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       */
/* KIND, either express or implied.  See the License for the    */
/* specific language governing permissions and limitations      */
/* under the License.                                           */
/*--------------------------------------------------------------*/

package org.corehunter.tests.data.simple;


import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.corehunter.data.simple.SimpleBiAllelicGenotypeVariantData;
import org.corehunter.data.simple.SimplePhenotypicTraitData;

import static org.corehunter.tests.TestData.BLANK_HEADERS;
import static org.corehunter.tests.TestData.HEADERS_UNIQUE_NAMES;
import static org.corehunter.tests.TestData.HEADERS_NAMES_AND_IDS;
import static org.corehunter.tests.TestData.PHENOTYPIC_TRAIT_NAMES;
import static org.corehunter.tests.TestData.PHENOTYPIC_TRAIT_VALUES;
import static org.corehunter.tests.TestData.SET;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import uno.informatics.common.io.FileType;
import uno.informatics.data.FeatureDataset;
import uno.informatics.data.FeatureDatasetRow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import uno.informatics.data.SimpleEntity;

/**
 * @author Herman De Beukelaer
 */
public class SimplePhenotypicTraitDataTest {

    private static final String CSV_NAMES = "/phenotypes/names-no-whitespace.csv";
    private static final String CSV_NAMES_IDS = "/phenotypes/names-and-ids.csv";
    private static final String TXT_NO_NAMES = "/phenotypes/no-names-no-whitespace.txt";

    private static final String ERRONEOUS_FILES_DIR = "/phenotypes/err/";
    
    private SimpleEntity[] expectedHeaders;
    private String datasetName;
    
    @BeforeClass
    public static void beforeClass(){
        System.out.println("Test simple phenotypic trait data");
    }
    
    @AfterClass
    public static void afterClass(){
        System.out.println("Done");
    }
    
    @Test
    public void fromCsvFileWithNames() throws IOException {
        datasetName = "names-no-whitespace.csv";
        expectedHeaders = HEADERS_UNIQUE_NAMES;
        System.out.println(" |- File " + datasetName);
        testData(SimplePhenotypicTraitData.readData(
            Paths.get(SimplePhenotypicTraitDataTest.class.getResource(CSV_NAMES).getPath()),
            FileType.CSV
        ));
    }
    
    @Test
    public void fromCsvFileWithNamesAndIds() throws IOException {
        datasetName = "names-and-ids.csv";
        expectedHeaders = HEADERS_NAMES_AND_IDS;
        System.out.println(" |- File " + datasetName);
        testData(SimplePhenotypicTraitData.readData(
            Paths.get(SimplePhenotypicTraitDataTest.class.getResource(CSV_NAMES_IDS).getPath()),
            FileType.CSV
        ));
    }
    
    @Test
    public void fromTxtFileWithoutNames() throws IOException {
        datasetName = "no-names-no-whitespace.txt";
        expectedHeaders = BLANK_HEADERS;
        System.out.println(" |- File " + datasetName);
        testData(SimplePhenotypicTraitData.readData(
            Paths.get(SimplePhenotypicTraitDataTest.class.getResource(TXT_NO_NAMES).getPath()),
            FileType.TXT
        ));
    }
    
    @Test
    public void erroneousFiles() throws IOException {
        System.out.println(" |- Test erroneous files:");
        Path dir = Paths.get(SimplePhenotypicTraitDataTest.class.getResource(ERRONEOUS_FILES_DIR).getPath());
        try(DirectoryStream<Path> directory = Files.newDirectoryStream(dir)){
            for(Path file : directory){
                System.out.print("  |- " + file.getFileName().toString() + ": ");
                FileType type = file.toString().endsWith(".txt") ? FileType.TXT : FileType.CSV;
                boolean thrown = false;
                try {
                    SimpleBiAllelicGenotypeVariantData.readData(file, type);
                } catch (IOException ex){
                    thrown = true;
                    System.out.print(ex.getMessage());
                } finally {
                    System.out.println();
                }
                assertTrue("File " + file + " should throw exception.", thrown);
            }
        }
    }

    private void testData(SimplePhenotypicTraitData data) {
        
        // check dataset name, if set
        String expectedDatasetName = datasetName != null ? datasetName : "Phenotypic trait data";
        assertEquals("Incorrect dataset name.", expectedDatasetName, data.getDatasetName());
        
        // check IDs
        assertEquals("Ids not correct.", SET, data.getIDs());
        // check size
        assertEquals("Incorrect dataset size.", SET.size(), data.getDatasetSize());
        
        // check trait names
        FeatureDataset fData = data.getData();
        for(int t = 0; t < fData.getFeatures().size(); t++){
            
            assertEquals("Trait name for trait " + t + " is not correct.",
                         PHENOTYPIC_TRAIT_NAMES[t], fData.getFeatures().get(t).getName());
            
        }
        
        // check individuals (headers and trait values)
        int size = data.getDatasetSize();

        for (int i = 0; i < size; i++) {
            
            // check header
            assertEquals("Header for individual " + i + " is not correct.", expectedHeaders[i], data.getHeader(i));

            // check trait values
            FeatureDatasetRow row = fData.getRow(i);
            for(int t = 0; t < row.getColumnCount(); t++){
                assertEquals(
                    "Incorrect value for trait " + t + " in individual " + i + ".",
                    PHENOTYPIC_TRAIT_VALUES[i][t],
                    row.getValue(t)
                );
            }
            
        }
        
    }
}