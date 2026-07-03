package models;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.core.model.InsilicoModelOutput;
import insilico.core.model.runner.InsilicoModelRunnerByMolecule;
import insilico.core.molecule.InsilicoMolecule;
import insilico.core.molecule.conversion.SmilesMolecule;
import insilico.models.dispatcher.ModelDispatcher;
import insilico.models.exception.ModelNotFoundException;
import models.interfaces.iModelValidator;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseModelValidationTest {

    protected static ArrayList<InsilicoMolecule> dataset;
    protected static List<Map<String, String>> groundTruth;

    @BeforeAll
    void loadDataset() throws IOException, CsvValidationException {

        dataset = new ArrayList<>();
        groundTruth = new ArrayList<>();

        InputStream is = BaseModelValidationTest.class.getClassLoader().getResourceAsStream("groundTruth.csv");

        if (is == null) {
            throw new IllegalStateException("groundTruth.csv not found in src/test/resources/");
        }

        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(is, StandardCharsets.UTF_8))
                .withCSVParser(new CSVParserBuilder()
                        .withSeparator('\t')
                        .build())
                .build()) {

            String[] headers = reader.readNext();
            int nameIndex = Arrays.asList(headers).indexOf("NAME");
            int smilesIndex = Arrays.asList(headers).indexOf("SMILES");

            String[] lineArray;
            while ((lineArray = reader.readNext()) != null) {
                InsilicoMolecule molecule = SmilesMolecule.Convert(lineArray[smilesIndex]);
                molecule.SetId(lineArray[nameIndex]);
                dataset.add(molecule);

                Map<String, String> line = new HashMap<>();
                for(int i = 0; i < lineArray.length; i++){
                    line.put(headers[i], lineArray[i]);
                }
                groundTruth.add(line);
            }
        }
    }


    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("allModels")
    void eachModelMustMatchGroundTruth(InsilicoModel model) throws GenericFailureException {

        InsilicoModelRunnerByMolecule runner = new InsilicoModelRunnerByMolecule();
        runner.AddModel(model);
        runner.Run(dataset);

        SoftAssertions softly = new SoftAssertions();

        if(model.getInfo().getKey().equals("DILI_ONTOX")){
            System.out.println(model.getInfo().getKey());
        }

        List<InsilicoModelOutput> predictions = runner.GetModelWrapper(model.getClass()).getResult();

        for(InsilicoModelOutput prediction : predictions) {
            groundTruth.stream().filter(result -> {
                return result.get("NAME").equals(prediction.getMoleculeId());
            }).findFirst().ifPresent(baseLine -> {

                System.out.println("---- "+prediction.getMoleculeSMILES()+ " "
                        + prediction.getMoleculeId()+ " -----");


                for(int i = 0; i < prediction.getResults().length; i++) {

                    String resultName = model.getInfo().getKey()+"-"+model.GetResultsName()[i];
                    String result = baseLine.get(resultName);

                    System.out.println("PREDICTED: " + model.GetResultsName()[i]
                            +": "+ prediction.getResults()[i]);

                    System.out.println("IN DATABASE: " + model.GetResultsName()[i]
                            +": "+ result);


                    softly.assertThat(result)
                            .as("%s → %s", model.getInfo().getName(), resultName)
                            .isEqualTo(prediction.getResults()[i]);
                }
            });
        }

        softly.assertAll();
    }

//    static Stream<ModelValidator> allModels() throws ModelNotFoundException, InitFailureException {
//        ModelDispatcher md = new ModelDispatcher();
//        return md.getCompleteModelList().stream().map(ModelValidator::new);
//    }

    static Stream<InsilicoModel> allModels() throws ModelNotFoundException, InitFailureException, GenericFailureException {
        ModelDispatcher md = new ModelDispatcher();
        return md.getCompleteModelList().stream();
    }

}
