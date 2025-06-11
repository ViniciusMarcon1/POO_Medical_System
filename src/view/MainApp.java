package view;

import controller.ConsultaController;
import exceptions.PersistenciaException;
import exceptions.BuscaInvalidaException;
import model.Consulta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
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
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            try {
                controller.loadData(BINARY_PATH);
            } catch (PersistenciaException e) {
                JOptionPane.showMessageDialog(frame,
                    "Erro ao carregar dados iniciais: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Busca Médico", createMedicoTab());
            tabbedPane.addTab("Busca Paciente", createPacienteTab());

            JButton reportButton = new JButton("Gerar Relatório Final");
            reportButton.addActionListener(evt -> {
                try {
                    controller.consolidarRelatorio(
                        List.of(controller.getAllConsultas()),
                        "relatorio_final.csv"
                    );
                    JOptionPane.showMessageDialog(frame, "Relatório gerado com sucesso.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame,
                        "Falha ao gerar relatório: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
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
        JButton searchButton = new JButton("Buscar");
        form.add(new JLabel("CRM:"));
        form.add(crmField);
        form.add(searchButton);
        panel.add(form, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
            new String[]{"CRM","Nome Médico","CPF","Nome Paciente","Data","Horário"}, 0
        );
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton exportButton = new JButton("Exportar CSV");
        exportButton.addActionListener(evt -> {
            try {
                List<Consulta> results = controller.buscar(
                    crmField.getText(), null, null, null, null
                );
                controller.exportarCsv(results, "busca_medico.csv");
                JOptionPane.showMessageDialog(panel, "Exportação concluída.");
            } catch (BuscaInvalidaException | IOException ex) {
                JOptionPane.showMessageDialog(panel,
                    "Erro na exportação: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(exportButton, BorderLayout.SOUTH);

        searchButton.addActionListener(evt -> {
            try {
                List<Consulta> results = controller.buscar(
                    crmField.getText(), null, null, null, null
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
        JButton searchButton = new JButton("Buscar");
        form.add(new JLabel("CPF:"));
        form.add(cpfField);
        form.add(searchButton);
        panel.add(form, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
            new String[]{"CRM","Nome Médico","CPF","Nome Paciente","Data","Horário"}, 0
        );
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton exportButton = new JButton("Exportar CSV");
        exportButton.addActionListener(evt -> {
            try {
                List<Consulta> results = controller.buscar(
                    null, cpfField.getText(), null, null, null
                );
                controller.exportarCsv(results, "busca_paciente.csv");
                JOptionPane.showMessageDialog(panel, "Exportação concluída.");
            } catch (BuscaInvalidaException | IOException ex) {
                JOptionPane.showMessageDialog(panel,
                    "Erro na exportação: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(exportButton, BorderLayout.SOUTH);

        searchButton.addActionListener(evt -> {
            try {
                List<Consulta> results = controller.buscar(
                    null, cpfField.getText(), null, null, null
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
            } catch (BuscaInvalidaException ex) {
                JOptionPane.showMessageDialog(panel,
                    "Busca inválida: " + ex.getMessage(),
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        });

        return panel;
    }
}
