package view;

import controller.ConsultaController;
import exceptions.PersistenciaException;
import exceptions.BuscaInvalidaException;
import model.Consulta;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MainApp {
    private static final ConsultaController controller = new ConsultaController();
    private static final String BINARY_PATH = "consultas.bin";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Trabalho RA3 - Consultas");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);

            try {
                controller.loadData(BINARY_PATH);
            } catch (PersistenciaException e) {
                JOptionPane.showMessageDialog(frame,
                    "Erro ao carregar dados iniciais: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }

            JPanel medicoTab = createMedicoTab();
            JPanel pacienteTab = createPacienteTab();

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Busca Médico", medicoTab);
            tabbedPane.addTab("Busca Paciente", pacienteTab);

            JButton reportButton = new JButton("Gerar Relatório Final");
            reportButton.addActionListener(evt -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Salvar relatório final como...");
                chooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
                int res = chooser.showSaveDialog(frame);
                if (res == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    String path = file.getAbsolutePath().endsWith(".csv")
                        ? file.getAbsolutePath()
                        : file.getAbsolutePath() + ".csv";
                    try {
                        controller.consolidarRelatorio(
                            List.of(controller.getAllConsultas()),
                            path
                        );
                        JOptionPane.showMessageDialog(frame, "Relatório gerado em:\n" + path);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame,
                            "Falha ao gerar relatório: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            frame.setLayout(new BorderLayout());
            frame.add(tabbedPane, BorderLayout.CENTER);
            frame.add(reportButton, BorderLayout.SOUTH);
            frame.setVisible(true);
        });
    }

    private static JPanel createMedicoTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JTextField crmField = new JTextField(10);
        JTextField dataInicialField = new JTextField(8);
        JTextField dataFinalField = new JTextField(8);
        JButton searchButton = new JButton("Buscar");

        form.add(new JLabel("CRM:"));
        form.add(crmField);
        form.add(new JLabel("Data Inicial (dd/MM/yyyy):"));
        form.add(dataInicialField);
        form.add(new JLabel("Data Final (dd/MM/yyyy):"));
        form.add(dataFinalField);
        form.add(searchButton);
        panel.add(form, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
            new String[]{"CRM","Nome Médico","CPF","Nome Paciente","Data","Horário"}, 0
        );
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // popule inicial
        try {
            for (Consulta c : controller.getAllConsultas()) {
                model.addRow(new Object[]{
                    c.getMedico().getCrm(),
                    c.getMedico().getNome(),
                    c.getPaciente().getCpf(),
                    c.getPaciente().getNome(),
                    c.getData().format(DATE_FORMATTER),
                    c.getHorario().format(TIME_FORMATTER)
                });
            }
        } catch (Exception ignored) {}

        JButton exportButton = new JButton("Exportar CSV");
        exportButton.addActionListener(evt -> {
            try {
                LocalDate di = dataInicialField.getText().isEmpty() ? null
                    : LocalDate.parse(dataInicialField.getText(), DATE_FORMATTER);
                LocalDate df = dataFinalField.getText().isEmpty() ? null
                    : LocalDate.parse(dataFinalField.getText(), DATE_FORMATTER);

                List<Consulta> results = controller.buscar(
                    crmField.getText(), null, di, df, null
                );

                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Salvar busca médico como...");
                chooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
                if (chooser.showSaveDialog(panel) == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    String path = file.getAbsolutePath().endsWith(".csv")
                        ? file.getAbsolutePath()
                        : file.getAbsolutePath() + ".csv";

                    controller.exportarCsv(results, path);
                    JOptionPane.showMessageDialog(panel, "Exportado em:\n" + path);
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(panel,
                    "Formato de data inválido. Use dd/MM/yyyy.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (BuscaInvalidaException | IOException ex) {
                JOptionPane.showMessageDialog(panel,
                    "Erro na exportação: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(exportButton, BorderLayout.SOUTH);

        searchButton.addActionListener(evt -> {
            try {
                LocalDate di = dataInicialField.getText().isEmpty() ? null
                    : LocalDate.parse(dataInicialField.getText(), DATE_FORMATTER);
                LocalDate df = dataFinalField.getText().isEmpty() ? null
                    : LocalDate.parse(dataFinalField.getText(), DATE_FORMATTER);

                List<Consulta> results = controller.buscar(
                    crmField.getText(), null, di, df, null
                );
                model.setRowCount(0);
                for (Consulta c : results) {
                    model.addRow(new Object[]{
                        c.getMedico().getCrm(),
                        c.getMedico().getNome(),
                        c.getPaciente().getCpf(),
                        c.getPaciente().getNome(),
                        c.getData().format(DATE_FORMATTER),
                        c.getHorario().format(TIME_FORMATTER)
                    });
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(panel,
                    "Formato de data inválido. Use dd/MM/yyyy.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (BuscaInvalidaException ex) {
                JOptionPane.showMessageDialog(panel,
                    "Busca inválida: " + ex.getMessage(),
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        });

        return panel;
    }

    private static JPanel createPacienteTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JTextField cpfField = new JTextField(10);
        JTextField dataInicialField = new JTextField(8);
        JTextField dataFinalField = new JTextField(8);
        JButton searchButton = new JButton("Buscar");

        form.add(new JLabel("CPF:"));
        form.add(cpfField);
        form.add(new JLabel("Data Inicial (dd/MM/yyyy):"));
        form.add(dataInicialField);
        form.add(new JLabel("Data Final (dd/MM/yyyy):"));
        form.add(dataFinalField);
        form.add(searchButton);
        panel.add(form, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
            new String[]{"CRM","Nome Médico","CPF","Nome Paciente","Data","Horário"}, 0
        );
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // popule inicial
        try {
            for (Consulta c : controller.getAllConsultas()) {
                model.addRow(new Object[]{
                    c.getMedico().getCrm(),
                    c.getMedico().getNome(),
                    c.getPaciente().getCpf(),
                    c.getPaciente().getNome(),
                    c.getData().format(DATE_FORMATTER),
                    c.getHorario().format(TIME_FORMATTER)
                });
            }
        } catch (Exception ignored) {}

        JButton exportButton = new JButton("Exportar CSV");
        exportButton.addActionListener(evt -> {
            try {
                LocalDate di = dataInicialField.getText().isEmpty() ? null
                    : LocalDate.parse(dataInicialField.getText(), DATE_FORMATTER);
                LocalDate df = dataFinalField.getText().isEmpty() ? null
                    : LocalDate.parse(dataFinalField.getText(), DATE_FORMATTER);

                List<Consulta> results = controller.buscar(
                    null, cpfField.getText(), di, df, null
                );

                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Salvar busca paciente como...");
                chooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
                if (chooser.showSaveDialog(panel) == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    String path = file.getAbsolutePath().endsWith(".csv")
                        ? file.getAbsolutePath()
                        : file.getAbsolutePath() + ".csv";

                    controller.exportarCsv(results, path);
                    JOptionPane.showMessageDialog(panel, "Exportado em:\n" + path);
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(panel,
                    "Formato de data inválido. Use dd/MM/yyyy.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (BuscaInvalidaException | IOException ex) {
                JOptionPane.showMessageDialog(panel,
                    "Erro na exportação: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(exportButton, BorderLayout.SOUTH);

        searchButton.addActionListener(evt -> {
            try {
                LocalDate di = dataInicialField.getText().isEmpty() ? null
                    : LocalDate.parse(dataInicialField.getText(), DATE_FORMATTER);
                LocalDate df = dataFinalField.getText().isEmpty() ? null
                    : LocalDate.parse(dataFinalField.getText(), DATE_FORMATTER);

                List<Consulta> results = controller.buscar(
                    null, cpfField.getText(), di, df, null
                );
                model.setRowCount(0);
                for (Consulta c : results) {
                    model.addRow(new Object[]{
                        c.getMedico().getCrm(),
                        c.getMedico().getNome(),
                        c.getPaciente().getCpf(),
                        c.getPaciente().getNome(),
                        c.getData().format(DATE_FORMATTER),
                        c.getHorario().format(TIME_FORMATTER)
                    });
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(panel,
                    "Formato de data inválido. Use dd/MM/yyyy.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (BuscaInvalidaException ex) {
                JOptionPane.showMessageDialog(panel,
                    "Busca inválida: " + ex.getMessage(),
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        });

        return panel;
    }
}
