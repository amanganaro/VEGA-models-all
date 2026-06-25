package insilico.models;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import insilico.core.model.InsilicoModel;
import insilico.core.model.InsilicoModelOutput;
import insilico.core.model.runner.InsilicoModelRunnerByMolecule;
import insilico.core.model.runner.InsilicoModelWrapper;
import insilico.core.molecule.InsilicoMolecule;
import insilico.core.molecule.conversion.SmilesMolecule;
import insilico.models.dispatcher.ModelDispatcher;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatasetGenerator {

    public static void main(String[] args) throws Exception {
        // load molecules
        InputStream is = DatasetGenerator.class.getClassLoader().getResourceAsStream("molecules.csv");
        if (is == null)
            throw new IllegalStateException("molecules.csv not found in src/test/resources/");

        ArrayList<InsilicoMolecule> molecules = new ArrayList<>();
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
                molecules.add(molecule);
            }
        }

        ModelDispatcher dispatcher = new ModelDispatcher();
        List<InsilicoModel> models = dispatcher.getCompleteModelList();

        InsilicoModelRunnerByMolecule runner = new InsilicoModelRunnerByMolecule();


        Path outputPath = Paths.get("src", "test", "resources", "groundTruth.csv");

        try (CSVWriter writer = new CSVWriter(new FileWriter(outputPath.toFile()), '\t',
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {

            List<String> header = new ArrayList<>();
            header.add("NAME");
            header.add("SMILES");
            for (InsilicoModel model : models) {
                header.addAll(List.of(model.GetResultsName()));
                runner.AddModel(model);
            }
            writer.writeNext(header.toArray(new String[0]));

            runner.Run(molecules);

            for (InsilicoMolecule molecule : molecules) {
                System.out.println("--- SMILES: " + molecule.getInputSMILES() + " ---");

                List<String> row = new ArrayList<>();
                row.add(molecule.GetId());
                row.add(molecule.GetSMILES());
                for (InsilicoModelWrapper curModel : runner.GetModelWrappers()) {
                    System.out.println("MODEL: " + curModel.getModel().getInfo().getName());

                    curModel.getResult().stream().filter(result -> {
                        return result.getMoleculeId().equals(molecule.GetId());
                    }).findFirst().ifPresent(result -> {
                        if (result.getResults() == null)
                            row.addAll(Arrays.stream(curModel.getModel().GetResultsName()).map(item -> {
                                return "\t";
                            }).toList());
                        else
                            row.addAll(List.of(result.getResults()));
                    });
                }
                writer.writeNext(row.toArray(new String[0]));
            }

            System.out.println("Ground truth written to: " + outputPath.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
