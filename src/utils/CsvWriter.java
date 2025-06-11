package utils;

import model.Exportavel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class CsvWriter {

    public void export(List<Exportavel> lista, String path) throws IOException {
        Properties props = new Properties();
        try (InputStream in = CsvWriter.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (in != null) {
                props.load(in);
            }
        }
        char delimiter = props.getProperty("csv.delimitador", ",").charAt(0);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            // Cabeçalho
            writer.write(String.join(Character.toString(delimiter), "crm", "cpf", "data", "horario"));
            writer.newLine();

            for (Exportavel item : lista) {
                writer.write(item.toCsv());
                writer.newLine();
            }
        }
    }
}
