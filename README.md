# 🩺 TrabalhoRA3 – Sistema de Consultas Médicas (POO)

Este projeto simula um sistema de **gestão de consultas médicas**, desenvolvido para o Trabalho RA3 da disciplina de **Programação Orientada a Objetos (POO)**. Ele permite **importar dados de consultas via CSV**, **armazenar em binário**, realizar **buscas com filtros**, e **exportar resultados** com interface gráfica em **JavaFX**.

---

## 📁 Estrutura do Projeto

```
TrabalhoRA3/
├── pom.xml
├── README.md
├── src/
│   ├── model/               # Classes de domínio
│   │   ├─ Medico.java
│   │   ├─ Paciente.java
│   │   └─ Consulta.java
│   │
│   ├── exceptions/          # Exceções personalizadas
│   │   ├─ ArquivoInvalidoException.java
│   │   ├─ PersistenciaException.java
│   │   └─ BuscaInvalidaException.java
│   │
│   ├── persistence/         # Persistência CSV e binária
│   │   └─ ArquivoService.java
│   │
│   ├── utils/               # Escrita de CSV
│   │   └─ CsvWriter.java
│   │
│   ├── controller/          # Lógica de controle
│   │   └─ ConsultaController.java
│   │
│   ├── view/                # Interface gráfica JavaFX
│   │   └─ MainApp.java
│   │
│   └── application.properties
│
└── docs/
    ├─ diagrama-classe.png
    └─ manual_uso.md
```

---

## 🚀 Como Executar

### 1. Pré-requisitos

- Java 17
- Maven 3.8+
- JavaFX SDK (caso não esteja embutido via plugin)
---

## ✨ Funcionalidades

- 📥 **Importar** dados de consultas a partir de um arquivo CSV.
- 💾 **Gravar** e **recuperar** a lista de consultas em formato binário (`.dat`).
- 🔍 **Buscar** consultas por médico, paciente ou data.
- 📤 **Exportar** os resultados da busca para novo arquivo CSV.
- 🧩 Interface amigável com campos de busca e botões de ação.
- 🔐 **Tratamento de erros** com exceções específicas.

---

## 🧾 Formato do CSV de Entrada

O CSV deve conter o seguinte cabeçalho:

```csv
CRM,Nome Médico,Especialidade,CPF,Nome Paciente,Data Nascimento,Data Consulta
```

### 📌 Exemplo de linha válida:

```csv
12345,João Silva,Cardiologista,11122233344,Maria Oliveira,1980-10-22,2023-11-15
```

> As datas devem seguir o padrão **YYYY-MM-DD** (ISO).

---

## 🛠️ Tecnologias e Estilo

- Java 17
- JavaFX
- Maven
- Google Java Style Guide
- Arquitetura MVC

---

## ⚠️ Exceções Tratadas

| Classe                        | Quando é lançada                                       |
|------------------------------|--------------------------------------------------------|
| `ArquivoInvalidoException`   | Arquivo CSV mal-formado ou com campos inválidos       |
| `PersistenciaException`      | Erros ao ler ou gravar arquivos binários              |
| `BuscaInvalidaException`     | Parâmetros de busca inconsistentes                    |
