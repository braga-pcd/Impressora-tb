
import java.util.InputMismatchException;
import java.util.Scanner;

class Impressora {

    boolean impresionState = false; // false = desligada, true = ligada
    boolean simular = false; // true = teste, false = impressora real

    int tipo = 1;
    String modelo = "i9";
    String conexao = "USB";
    int parametro = 0;


    // Atalhos para mostrar mensagens no console
    static void print(Object text) {
        System.out.print(text);
    }

    static void println(Object text) {
        System.out.println(text);
    }


    // 1 = USB, 2 = Serial, 3 = Rede, 4 = Bluetooth
    boolean configurarIm(int novoTipo, String novoModelo, String novaConexao, int novoParametro) {

        if (impresionState) {
            println("Feche a conexão antes de configurar");
            return false;
        }

        if (novoTipo < 1 || novoTipo > 4) {
            println("Tipo inválido. Escolha de 1 a 4");
            return false;
        }

        if (!"i8".equalsIgnoreCase(novoModelo) && !"i9".equalsIgnoreCase(novoModelo)) {
            println("Modelo inválido. Use i8 ou i9");
            return false;
        }

        if (novaConexao == null || novaConexao.isBlank()) {
            println("Informe USB, COM, IP ou MAC Bluetooth");
            return false;
        }

        if (novoTipo == 1 && (!novaConexao.equalsIgnoreCase("USB") || novoParametro != 0)) {
            println("USB usa conexão USB e parametro 0");
            return false;
        }

        if (novoTipo == 2 && novoParametro <= 0) {
            println("Informe a velocidade da porta serial");
            return false;
        }

        if (novoTipo == 3 && (novoParametro < 1 || novoParametro > 65535)) {
            println("Informe uma porta de rede entre 1 e 65535");
            return false;
        }

        if (novoTipo == 4 && novoParametro != 0) {
            println("Bluetooth usa parametro 0");
            return false;
        }

        tipo = novoTipo;
        modelo = novoModelo;
        conexao = novaConexao;
        parametro = novoParametro;

        println("Configuração salva");
        return true;
    }


    void openIm() {

        if (impresionState) {
            println("A conexão já está aberta.");
            return;
        }

        if (simular) {
            impresionState = true;
            println("[TESTE] Conexão aberta");
            return;
        }

        try {
            int retorno = ImpressoraDLL.INSTANCE.AbreConexaoImpressora(tipo, modelo, conexao, parametro);

            if (retorno == 0) {
                impresionState = true;
                println("Conexão aberta");
            } else {
                println("Erro ao conectar. Código: " + retorno);
            }

        } catch (LinkageError erro) {
            impresionState = false;
            println("Não foi possível carregar a DLL: " + erro.getMessage());
        }
    }


    void closeIm() {

        if (!impresionState) {
            println("A conexão já está fechada.");
            return;
        }

        if (simular) {
            impresionState = false;
            println("[TESTE] Conexão fechada");
            return;
        }

        try {
            int retorno = ImpressoraDLL.INSTANCE.FechaConexaoImpressora();

            if (retorno == 0) {
                impresionState = false;
                println("Conexão fechada");
            } else {
                println("Erro ao desconectar. Código: " + retorno);
            }

        } catch (LinkageError erro) {
            println("Não foi possível acessar a DLL: " + erro.getMessage());
        }
    }


    boolean isConnected() {
        return impresionState;
    }


    // IMPRESSÃO DE TEXTO
    void imprimeTexto() {

        Scanner sc = Main.sc;

        if (!isConnected()) {
            println("Conexão fechada. Abra a conexão antes de prosseguir.");
            return;
        }

        println("\n=== IMPRESSÃO DE TEXTO ===");

        String dados;
        int posicao = 1;
        int estilo = 0;
        int tamanho = 0; // Tamanho normal (antes era 5)
        int copias;
        int avanco = 10; // Espaço antes de cortar

        try {
            println("Dados: ");
            dados = sc.nextLine();

            println("Cópias: ");
            copias = sc.nextInt();
            sc.nextLine();

        } catch (InputMismatchException e) {
            sc.nextLine();
            println("Erro: Digite um número inteiro válido!");
            return;
        }

        if (dados.isBlank()) {
            println("Erro: O texto não pode estar vazio.");
            return;
        }

        if (copias <= 0) {
            println("Erro: A quantidade de cópias deve ser maior que zero.");
            return;
        }

        // MODO SIMULAÇÃO
        if (simular) {
            for (int c = 0; c < copias; c++) {
                println("[TESTE] Cópia " + (c + 1) + ": " + dados);
                println("[TESTE] Avançando 6 linhas...");
                println("[TESTE] Cortando papel...");
            }
            return;
        }

        // IMPRESSÃO REAL
        try {
            for (int c = 0; c < copias; c++) {

                // Imprime uma cópia por vez
                int retorno = ImpressoraDLL.INSTANCE.ImpressaoTexto(
                        dados + "\n", posicao, estilo, tamanho
                );

                if (retorno < 0) {
                    println("Erro ao imprimir cópia " + (c + 1) + ": " + retorno);
                    break;
                }

                // Avança o papel após imprimir
                int retAvanco = ImpressoraDLL.INSTANCE.AvancaPapel(6);

                if (retAvanco != 0) {
                    println("Erro ao avançar papel: " + retAvanco);
                    break;
                }

                // Corta cada cópia separadamente
                int retCorte = ImpressoraDLL.INSTANCE.Corte(avanco);

                if (retCorte != 0) {
                    println("Erro ao cortar papel: " + retCorte);
                    break;
                }

                println("Cópia " + (c + 1) + " impressa e cortada com sucesso!");

                ImpressoraDLL.INSTANCE.SinalSonoro(1, 50, 5);
            }

        } catch (LinkageError erro) {
            println("Erro ao acessar a DLL: " + erro.getMessage());
        }
    }


    // IMPRESSÃO DE QR CODE
    void imprimeQRCode() {

        if (!isConnected()) {
            println("Conexão fechada. Abra a conexão antes de prosseguir.");
            return;
        }

        Scanner sc = Main.sc;

        println("\n=== IMPRESSÃO DE QR CODE ===");

        int tamanho = 4;
        int nivelCorrecao = 2;
        int avanco = 10;

        print("Conteúdo do QR Code (ex: link ou texto): ");
        String dados = sc.nextLine();

        if (dados.isBlank()) {
            println("Erro: O QR Code não pode estar vazio.");
            return;
        }

        if (simular) {
            println("[TESTE] QR Code: " + dados);
            println("[TESTE] Avançando 6 linhas...");
            println("[TESTE] Papel cortado.");
            return;
        }

        try {
            int retorno = ImpressoraDLL.INSTANCE.ImpressaoQRCode(
                    dados, tamanho, nivelCorrecao
            );

            if (retorno == 0) {

                println("QR Code enviado com sucesso!");

                // Avança o papel antes do corte
                int retAvanco = ImpressoraDLL.INSTANCE.AvancaPapel(6);

                if (retAvanco != 0) {
                    println("Erro ao avançar papel: " + retAvanco);
                    return;
                }

                // Corta depois do QR Code
                int retCorte = ImpressoraDLL.INSTANCE.Corte(avanco);

                if (retCorte == 0) {
                    println("QR Code impresso e papel cortado!");
                    ImpressoraDLL.INSTANCE.SinalSonoro(1, 50, 5);
                } else {
                    println("Erro ao cortar papel: " + retCorte);
                }

            } else {
                println("Erro ao imprimir QR Code: " + retorno);
            }

        } catch (LinkageError erro) {
            println("Erro ao acessar a DLL: " + erro.getMessage());
        }
    }


    // IMPRESSÃO DE CÓDIGO DE BARRAS
    void imprimeCodigoBarras() {

        if (!isConnected()) {
            println("Conexão fechada. Abra a conexão antes de prosseguir.");
            return;
        }

        Scanner sc = Main.sc;
        println("\n=== IMPRESSÃO DE CÓDIGO DE BARRAS ===");

        int tipo = 8; // CODE 128
        int altura = 60;
        int largura = 2;
        int hri = 2;
        int avanco = 5;

        print("Dados do código de barras (ex: {A012345678912): ");
        String dados = sc.nextLine();

        if (simular) {
            println("[TESTE] Código de barras: " + dados);
            println("[TESTE] Papel cortado.");
            return;
        }

        int retorno = ImpressoraDLL.INSTANCE.ImpressaoCodigoBarras(
                tipo, dados, altura, largura, hri
        );

        if (retorno == 0) {
            println("Código de barras impresso com sucesso!");

            int retCorte = ImpressoraDLL.INSTANCE.Corte(avanco);

            if (retCorte == 0) {
                println("Papel cortado com sucesso!");
                ImpressoraDLL.INSTANCE.SinalSonoro(1, 50, 5);
            } else {
                println("Erro ao cortar papel: " + retCorte);
            }

        } else {
            println("Erro ao imprimir código de barras. Retorno: " + retorno);
        }
    }


    // IMPRESSÃO DE XML SAT
    void imprimeXMLSAT() {

        if (!isConnected()) {
            println("Conexão fechada. Abra a conexão antes de prosseguir.");
            return;
        }

        String caminhoXML = "path=XMLSAT.xml";
        int avanco = 5;

        if (simular) {
            println("[TESTE] XML SAT impresso com sucesso!");
            println("[TESTE] Papel cortado.");
            return;
        }

        int retorno = ImpressoraDLL.INSTANCE.ImprimeXMLSAT(caminhoXML, 0);

        if (retorno == 0) {
            println("Cupom SAT (XML) impresso com sucesso!");

            int retCorte = ImpressoraDLL.INSTANCE.Corte(avanco);

            if (retCorte == 0) {
                println("Papel cortado com sucesso!");
                ImpressoraDLL.INSTANCE.SinalSonoro(1, 50, 5);
            } else {
                println("Erro ao cortar papel: " + retCorte);
            }

        } else {
            println("Erro ao imprimir XML SAT. Retorno: " + retorno);
        }
    }
}
