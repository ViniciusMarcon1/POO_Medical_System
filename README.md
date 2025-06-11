# 🩺 TrabalhoRA3 – Sistema de Consultas Médicas (POO)

## Visão Geral

Aplicação desktop desenvolvida em Java Swing para gerenciar consultas médicas e de pacientes.
Permite:

* Carregar dados de médicos e pacientes a partir de CSV
* Importar consultas (CSV → objetos) e persistir em binário
* Buscar consultas por CRM, CPF, data e meses sem atendimento
* Exportar resultados parciais e relatório final em CSV

## Pré-requisitos

* JDK 11 ou superior instalado
* Diretório de trabalho contendo:

  * `application.properties`
  * `medicos.csv`
  * `pacientes.csv`
  * `consultas.csv`

## Estrutura de Pastas

```
TrabalhoRA3/
├── src/
│   ├── model/             # Pessoa, Medico, Paciente, Consulta, Exportavel
│   ├── exceptions/        # ArquivoInvalidoException, PersistenciaException, BuscaInvalidaException
│   ├── persistence/       # ArquivoService
│   ├── utils/             # CsvWriter
│   ├── controller/        # ConsultaController
│   └── view/              # MainApp.java (Swing UI)
├── application.properties
├── medicos.csv
├── pacientes.csv
└── consultas.csv
```

## Configuração (`application.properties`)

```properties
medicos.csv=medicos.csv
pacientes.csv=pacientes.csv
consultas.csv=consultas.csv
csv.delimitador=,
formato.data=dd/MM/yyyy
formato.hora=HH:mm
```

## Compilação e Execução

```bash
# Compilar todas as classes
javac -d bin src/**/*.java

# Executar a aplicação
java -cp bin:. view.MainApp
```

## Fluxo de Uso

1. **Carga inicial**: ao iniciar, lê `medicos.csv`, `pacientes.csv` e `consultas.csv`, instancia objetos e grava `consultas.bin`.
2. **Busca Médico**: na aba, insira o CRM e clique em Buscar; exibe resultados em tabela; clique em Exportar CSV para `busca_medico.csv`.
3. **Busca Paciente**: insira o CPF, clique em Buscar; exibe resultados; clique em Exportar CSV para `busca_paciente.csv`.
4. **Relatório Final**: botão “Gerar Relatório Final” concatena todas as consultas em `relatorio_final.csv`.

## Detalhes Técnicos

* **Modelo de Domínio**: `Pessoa` abstrata, `Medico` e `Paciente` estendem, usando herança e encapsulamento.
* **Polimorfismo**: interface `Exportavel` implementada por `Consulta` para exportação genérica.
* **Persistência**: classe `ArquivoService` com leitura/escrita de CSV e serialização binária (`Serializable`).
* **Tratamento de Erros**: exceções customizadas (`ArquivoInvalidoException`, `PersistenciaException`, `BuscaInvalidaException`).
* **UI**: Java Swing, `JTabbedPane`, formulários com `JTextField`, `JTable` e feedback via `JOptionPane`.

## Exemplos de CSV

**medicos.csv**

```csv
crm,nome,especialidade
CRM1001,João da Silva,PEDIATRA
CRM1002,Maria dos Santos,DERMATOLOGISTA
...```

**pacientes.csv**
```csv
cpf,nome,dataNascimento
106977991,Gustavo Ramos,08/07/2007
126855092,Rafael Barros,14/10/1954
...```

**consultas.csv**
```csv
crm,cpf,data,horario
CRM1001,106977991,15/03/2024,09:30
CRM1002,126855092,22/05/2024,14:15
...```

## Uso
1. **Preparar**: remova arquivos binários antigos (`consultas.bin`, `busca_*.csv`, `relatorio_final.csv`).
2. **Compilar**: execute `javac -d bin src/**/*.java`.
3. **Iniciar**: execute `java -cp bin:. view.MainApp`.
4. **Buscar Médico**: teste CRM existente e vazio.
5. **Buscar Paciente**: teste CPF existente e vazio.
6. **Gerar Relatório Final**: verifique `relatorio_final.csv`.
7. **Validar**: abra CSVs gerados e compare formatos e dados.

