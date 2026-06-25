package models;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.core.model.InsilicoModelOutput;
import insilico.core.molecule.InsilicoMolecule;
import insilico.core.tools.utils.FileUtilities;
import insilico.models.dispatcher.ModelDispatcher;
import insilico.models.exception.ModelNotFoundException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseModelValidationTest {

    protected static List<InsilicoMolecule> dataset;
    protected static List<Map<String, String>> groundTruth;

    @BeforeAll
    void loadDataset() throws IOException, CsvValidationException {

        dataset = new ArrayList<>();
        groundTruth = new ArrayList<>();

//        URL resource = BaseModelValidationTest.class.getResource("src/test/testDataset.csv");

        InputStream is = BaseModelValidationTest.class.getClassLoader().getResourceAsStream("testDataset.csv");

        if (is == null) {
            throw new IllegalStateException("testDataset.csv not found in src/test/resources/");
        }

        try (CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new InputStreamReader(is, StandardCharsets.UTF_8))){

            Map<String, String> line;
            while ((line = reader.readMap()) != null) {

                InsilicoMolecule molecule = new InsilicoMolecule();
                molecule.SetSMILES(line.get("SMILES"));

                dataset.add(molecule);
                groundTruth.add(line);
            }
        }
    }



    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("allModels")
    void eachModelMustMatchGroundTruth(ModelValidator model) throws GenericFailureException {

        SoftAssertions softly = new SoftAssertions();

        for(InsilicoMolecule molecule : dataset) {
            InsilicoModelOutput prediction = model.model().Execute(molecule);

            Optional<Map<String, String>> optionalBaseline = groundTruth.stream().filter(result -> {
                return result.get("SMILES").equals(molecule.GetSMILES());
            }).findFirst();

            System.out.println(optionalBaseline);

            if(optionalBaseline.isPresent()) {
                Map<String, String> baseLine = optionalBaseline.get();

                System.out.println(baseLine.get(prediction.getResults()[0]));

                double result = Double.parseDouble(baseLine.get(prediction.getResults()[0]));

                softly.assertThat(result)
                        .as("%s → %s", model.model().getInfo().getName(), "Prediction")
                        .isEqualTo(prediction.getMainResultValue());
            }
        }

        softly.assertAll();
    }

    static Stream<InsilicoModel> allModels() throws ModelNotFoundException, InitFailureException {
        ModelDispatcher md = new ModelDispatcher();
        return md.getCompleteModelList().stream();
    }

}
