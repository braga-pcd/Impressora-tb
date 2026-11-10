
<div align="center">

# 🖨️ Sistema de Controle de Impressora Elgin

### Projeto de Programação Orientada a Objetos — Java + JNA

**Controle de uma impressora térmica Elgin i8/i9 pelo console.**  
Conexões, impressão de cupons, leitura de XML e comandos de periféricos em um só projeto.

<br>

![Java](https://img.shields.io/badge/Java-POO-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JNA](https://img.shields.io/badge/JNA-5.15.0-3D5A80?style=for-the-badge)
![Elgin](https://img.shields.io/badge/Elgin-i8%20%7C%20i9-14947B?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Em%20desenvolvimento-F2C94C?style=for-the-badge)

**[Sobre](#-sobre-o-projeto) • [Funcionalidades](#-funcionalidades) • [Instalação](#-como-executar) • [Testes](#-testes) • [Equipe](#-equipe)**

</div>

---

## 📘 Sobre o projeto

Este projeto foi desenvolvido como atividade da disciplina de **Programação Orientada a Objetos (POO)**. A proposta é construir um programa **Java de console** que se comunica com impressoras térmicas **Elgin i8 e i9**, utilizando a biblioteca nativa `E1_Impressora01.dll` por meio da biblioteca **JNA**.

O sistema foi dividido entre quatro integrantes. A atividade envolve **classes, objetos, atributos, métodos, laços de repetição e tratamento de erros**, além de controle de conexão e comandos de impressão.

> [!IMPORTANT]
> **Estado do projeto:** os arquivos de conexão e um `Main.java` de testes estão disponíveis no material analisado. Login, menu definitivo e operações de impressão devem ser conferidos depois que as partes dos integrantes forem integradas. **Não há confirmação de teste em impressora física.**

## ✨ Funcionalidades

As funcionalidades abaixo são as **exigências do trabalho**. A tabela descreve o que o sistema completo deverá oferecer; não significa que todas já tenham sido implementadas e testadas.

| Opção | Função | O que faz |
|:---:|---|---|
| `1` | ⚙️ Configurar conexão | Define tipo de conexão, modelo, endereço/porta e parâmetro. |
| `2` | 🔌 Abrir conexão | Inicia a comunicação com a impressora. |
| `3` | 🔒 Fechar conexão | Encerra a comunicação com segurança. |
| `4` | 🧾 Imprimir texto | Envia mensagens formatadas para o papel térmico. |
| `5` | ▦ Imprimir QR Code | Gera um QR Code com os dados informados. |
| `6` | ▥ Imprimir código de barras | Imprime um código de barras compatível. |
| `7` | 📄 Imprimir XML SAT | Imprime um cupom a partir de um XML de venda. |
| `8` | ↩️ Imprimir cancelamento SAT | Imprime o comprovante de cancelamento a partir de XML. |
| `9` | 💵 Abrir gaveta | Aciona uma gaveta de dinheiro compatível. |
| `10` | 🔊 Sinal sonoro | Solicita bips à impressora, quando suportado. |
| **Extra** | 📏 Avançar papel | Move o papel pela quantidade de linhas informada. |
| **Extra** | 📊 Consultar status | Consulta condições como presença de papel. |
| `0` | 🚪 Sair | Encerra o sistema. |

### 🔐 Regras do sistema

- Login com **até três tentativas** e aviso de quantas restam.
- Menu exibido novamente depois de cada operação, até escolher **Sair**.
- Pelo menos três classes: `Main`, `Usuario` e `Impressora`.
- Nenhuma solicitação de dados de impressão se a conexão não estiver aberta.
- Validação das entradas e tratamento de falhas com `try/catch`.
- Conferência do **código de retorno** dos comandos da DLL.

## 🔗 Tipos de conexão

A classe `Impressora.java` permite configurar os tipos previstos pela biblioteca:

| Tipo | Conexão | Exemplo de configuração |
|:---:|---|---|
| `1` | 🔌 USB | `"USB"`, parâmetro `0` |
| `2` | 🖥️ Serial | `"COM2"`, velocidade `9600` |
| `3` | 🌐 Rede TCP/IP | `"192.168.0.20"`, porta `9100` |
| `4` | 📶 Bluetooth | Endereço MAC do dispositivo, parâmetro `0` |

**Modelos:** `i8` ou `i9`. A disponibilidade de cada conexão e recurso depende da impressora utilizada.

## 🧰 Tecnologias utilizadas

| Tecnologia | Utilização |
|---|---|
| ☕ **Java** | Linguagem do programa e implementação dos conceitos de POO. |
| 🧩 **JNA 5.15.0** | Comunicação entre o Java e a biblioteca nativa. |
| 🖨️ **SDK Elgin — E1_Impressora01.dll** | Comandos de controle e impressão. |
| 💻 **IntelliJ IDEA** | Ambiente sugerido pelo professor para desenvolvimento e execução. |
| 🌿 **Git e GitHub** | Versionamento do código e trabalho em equipe. |
| 📄 **XML** | Dados dos cupons SAT de exemplo fornecidos na atividade. |

**Versão do Java usada pelo grupo:** `PREENCHER` (consultar com `java -version`).  
**Modelo testado fisicamente:** `AINDA NÃO INFORMADO`.

## 📁 Estrutura do projeto

A organização abaixo reúne os arquivos fornecidos pelo professor e as classes planejadas para a integração. **A localização dos `.java` pode variar** conforme a configuração do IntelliJ.

```text
Projeto POO EM/
├── Main.java                         # Início do programa / menu final
├── Usuario.java                      # Login (a integrar)
├── Impressora.java                   # Configuração e conexão
├── OperacoesImpressao.java           # Texto, QR Code, barras e XML SAT (a integrar)
├── OperacoesExtras.java              # Gaveta, bip, status e cancelamento (a integrar)
├── ImpressoraDLL.java                # Interface JNA fornecida: NÃO ALTERAR
├── XMLSAT.xml                        # Exemplo de cupom de venda
├── CANC_SAT.xml                       # Exemplo de cancelamento
├── Bibliotecas E1 Impressora/
│   ├── x64/E1_Impressora01.dll
│   └── x86/E1_Impressora01.dll
├── libs Java/
│   └── jna-5.15.0.jar
├── README.md
└── .gitignore
```

> [!NOTE]
> O ZIP original do professor já traz `ImpressoraDLL.java`, as DLLs, o JAR do JNA, os dois XMLs e `README_MODELO.md`. Os arquivos criados pelos integrantes devem ser adicionados ao projeto sem modificar a interface JNA original.

## ⚙️ Como executar

### 1. Requisitos

- **Windows** para utilizar a DLL nativa fornecida pela Elgin.
- **JDK** instalado (confirme a versão utilizada pelo grupo).
- **IntelliJ IDEA** ou outra IDE Java configurada.
- Impressora **Elgin i8/i9** compatível, caso deseje testar o modo real.

### 2. Baixe e abra o projeto

Baixe este repositório em **Code → Download ZIP**, extraia os arquivos e abra a pasta principal no IntelliJ em **File → Open**.

Se preferir usar Git:

```bash
git clone URL_DO_REPOSITORIO
```

> Substitua `URL_DO_REPOSITORIO` pelo endereço real do projeto no GitHub.

### 3. Adicione a biblioteca JNA

No IntelliJ, abra:

`File → Project Structure → Libraries → + → Java`

Escolha o arquivo:

```text
libs Java/jna-5.15.0.jar
```

### 4. Confira a pasta de execução

Em **Run → Edit Configurations**, configure o **Working directory** como a **raiz do projeto**, onde está a pasta `Bibliotecas E1 Impressora`.

O `ImpressoraDLL.java` procura automaticamente a versão da DLL correspondente à arquitetura da JVM (**x64** ou **x86**) a partir dessa pasta.

### 5. Execute

Abra `Main.java` e utilize **Run**. Até que o menu definitivo seja integrado, o `Main.java` de teste disponível executa verificações automáticas de conexão.

#### 🧪 Teste sem impressora física

No código de testes de conexão, a opção:

```java
Impressora imp = new Impressora();
imp.simular = true;
```

permite testar **a lógica de abrir e fechar conexão**, sem acessar a DLL real. Os avisos aparecem no console.

Para tentar utilizar o equipamento real:

```java
imp.simular = false;
```

> ⚠️ **Simular não é imprimir:** esse modo não gera cupons reais, não comprova compatibilidade com a DLL e não substitui a demonstração com uma Elgin física.

## 🔐 Login de teste

| Informação | Valor |
|---|---|
| Usuário | `PREENCHER APÓS DEFINIÇÃO DO GRUPO` |
| Senha | `PREENCHER APÓS DEFINIÇÃO DO GRUPO` |
| Tentativas permitidas | **3** |

O login faz parte dos requisitos do sistema final. **As credenciais ainda não foram informadas** nos arquivos disponíveis.

## 🧪 Testes

### ✅ Verificações realizadas com os arquivos disponíveis

Os seguintes testes foram executados localmente com **OpenJDK 21** e o **JNA 5.15.0** do pacote fornecido:

| Teste | Resultado |
|---|---|
| Compilar `Impressora.java`, `Main.java` de testes e `ImpressoraDLL.java` com JNA | ✅ Passou |
| Simulação de abertura e fechamento por USB | ✅ Passou |
| Simulação de abertura e fechamento por Bluetooth | ✅ Passou |
| Simulação de abertura e fechamento por serial | ✅ Passou |
| Simulação de abertura e fechamento por rede | ✅ Passou |
| Fechar conexão já fechada | ✅ Mensagem exibida |
| Configurar tipo inválido | ✅ Entrada rejeitada |
| Comunicação com uma Elgin física | ⏳ Não verificada |
| Login, menu e impressão real de cada operação | ⏳ Validar após integração |

### 📋 Testes pendentes antes da entrega

- [ ] Confirmar as três tentativas de login e suas mensagens.
- [ ] Conferir o menu completo e a opção de sair.
- [ ] Tentar imprimir sem conexão e confirmar o bloqueio **antes** de pedir dados.
- [ ] Verificar impressão de texto, QR Code e código de barras.
- [ ] Testar `XMLSAT.xml` e `CANC_SAT.xml`.
- [ ] Testar gaveta, sinal sonoro, avanço e status, **quando suportados**.
- [ ] Conferir erros de entrada, XML ausente e falha da DLL.
- [ ] Demonstrar o programa em uma impressora real.

### 📌 Observação sobre os retornos

A maioria dos comandos retorna `0` quando funciona. **`ImpressaoTexto` é uma exceção:** nesse caso, valores não negativos indicam envio bem-sucedido. `StatusImpressora` também pode retornar valores que representam o estado consultado (por exemplo, `5`, `6` ou `7` para papel, conforme o PDF).

## 👥 Equipe

| Integrante | Responsabilidade |
|---|---|
| **Nicolas BX** | **Parte 1:** login e menu principal |
| **Kauã** | **Parte 2:** configuração e conexão da impressora |
| **PREENCHER NOME** | **Parte 3:** operações de impressão |
| **PREENCHER NOME** | **Parte 4:** funções extras, testes e documentação |

> Antes da entrega, completar os nomes de todos os integrantes e conferir os commits de cada um no GitHub.

## 🌿 Git e organização do repositório

O professor solicita **ao menos cinco commits com mensagens claras**, incluindo participação dos integrantes, e que as pastas `.idea` e `out` não sejam enviadas.

Conteúdo mínimo do `.gitignore`:

```gitignore
.idea/
out/
```

Mensagens de commit devem explicar a mudança, por exemplo: `Adiciona login com 3 tentativas` ou `Implementa conexao com a impressora`.

## 📚 Referências

- 📄 **Trabalho_Impressora_Elgin.pdf** — requisitos, comandos e critérios de avaliação fornecidos pelo professor.
- 📝 **README_MODELO.md** — modelo de documentação fornecido no ZIP.
- 🔗 **[Documentação oficial dos comandos Elgin](https://elgindevelopercommunity.github.io/group___m1.html)**.

---

<div align="center">

**🖨️ Projeto acadêmico • Programação Orientada a Objetos**

*Feito em equipe, uma funcionalidade de cada vez.*

</div>
