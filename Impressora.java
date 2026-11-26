import java.util.InputMismatchException;
import java.util.Scanner;

class Impressora {

    boolean impresionState = false; // Se tiver false significa que a conexão está desligada, se true ela esta ligada.
    boolean simular = false; // true = teste / false = impressora real | depois apagar isso

    int tipo = 1;
    String modelo = "i9"; // ou i8
    String conexao = "USB";
    int parametro = 0;


    // Atalhos para mostrar mensagens no console   // // //
    static void print(Object text) {                 //
        System.out.print(text);                      //
    }                                                //
    //
    static void println(Object text) {               //
        System.out.println(text);                    //
    }                                                //
    // Fim dos atalhos para mostrar mensagens no console //


    // 1 = USB, 2 = Serial, 3 = Rede, 4 = Bluetooth
    boolean configurarIm(int novoTipo, String novoModelo, String novaConexao, int novoParametro) {//configurar impressora com base na parte do BX
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

    void openIm() {// isso abre conexão
        if (impresionState) {
            println("A conexão ja está aberta.");
            return;
        }

        if (simular) { // depois remover
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
            println("Não foi possivel carregar a DLL: " + erro.getMessage());
        }
    }

    void closeIm() { //isso fecha conexão
        if (!impresionState) {
            println("A conexão ja ta fechada.");
            return;
        }

        if (simular) { // depois remover
            impresionState = false;
            println("[TESTE]: Conexao fechada");
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
            println("Não foi possivel acessar a DLL: " + erro.getMessage());
        }
    }

    boolean isConnected() { // isso serve pra ver se a impressora está ativa (ativa = ture | desativada = false) exemplo na linha 132
        return impresionState;
    }

    void imprimeTexto() {
        Scanner sc = Main.sc;

        if (!isConnected()) {
            println("Conexão fechada. Abra a conexão antes de prosseguir.");
            return;
        }

        println("\n=== IMPRESSÃO DE TEXTO ===");

        String dados = "";
        int posicao = 1;
        int estilo = 0;
        int tamanho = 5;
        int avanco = 3;
        try {
            System.out.println("Dados: ");
            dados = sc.nextLine();


        } catch (InputMismatchException e) {
            System.out.println("Erro: Você não digitou um número inteiro válido!");
            return;
        }

        if (simular) {
            System.out.println("[TESTE] Impressão de Texto OK");
            System.out.println("[TESTE] Papel cortado com avanço de " + avanco + " linhas.");

            return;
        }
        boolean sucessoTotal = true;
        int retorno = -1;
        int c = 0;
        retorno = ImpressoraDLL.INSTANCE.ImpressaoTexto(dados, posicao, estilo, tamanho);

        if (retorno >= 0) {
                retorno = ImpressoraDLL.INSTANCE.ImpressaoTexto(dados, posicao, estilo, tamanho);
                System.out.println(" impressa com sucesso. Retorno " + retorno);

        } else {
            System.out.println("Erro ao imprimir "+ retorno);
            sucessoTotal = false;
        }


        if(sucessoTotal ){
            int retCorte = ImpressoraDLL.INSTANCE.Corte(avanco);
            if(retCorte >=0){
                println("Papel cortado comm seucesso");
                ImpressoraDLL.INSTANCE.SinalSonoro(1, 50, 5);
            }
        }else{
            println("AVISO: Impressão concluída, porém houve um erro ao cortar.");
        }
    }

    void imprimeQRCode() {
        if (!isConnected()) {
            println("Conexão fechada. Abra a conexão antes de prosseguir.");
            return;
        }

        Scanner sc = Main.sc;
        println("\n=== IMPRESSÃO DE QR CODE ===");

        String dados;
        int tamanho = 4;
        int nivelCorrecao = 2;
        int avanco = 10;

        System.out.print("Conteúdo do QR Code (ex: link ou texto): ");
        dados = sc.nextLine();

        if (dados.isBlank()) {
            println("Erro: O conteúdo não pode estar vazio.");
            return;
        }

        if (simular) {
            println("[TESTE] QR Code impresso com sucesso! Conteúdo: " + dados);
            println("[TESTE] Avançando 6 linhas...");
            println("[TESTE] Cortando papel...");
            return;
        }

        try {
            int retorno = ImpressoraDLL.INSTANCE.ImpressaoQRCode(
                    dados, tamanho, nivelCorrecao
            );

            if (retorno != 0) {
                println("Erro ao imprimir QR Code: " + retorno);
                return;
            }


            int retAvanco = ImpressoraDLL.INSTANCE.AvancaPapel(6);

            if (retAvanco != 0) {
                println("Erro ao avançar papel: " + retAvanco);
                return;
            }


            int retCorte = ImpressoraDLL.INSTANCE.Corte(avanco);

            if (retCorte != 0) {
                println("Erro ao cortar papel: " + retCorte);
                return;
            }

            println("QR Code impresso e cortado com sucesso!");

            ImpressoraDLL.INSTANCE.SinalSonoro(1, 50, 5);

        } catch (LinkageError erro) {
            println("Erro ao acessar a DLL: " + erro.getMessage());
        }
    }
    void imprimeCodigoBarras() {
        if (!isConnected()) {
            println("Conexão fechada. Abra a conexão antes de prosseguir.");
            return;
        }

        Scanner sc = Main.sc;
        println("\n=== IMPRESSÃO DE CÓDIGO DE BARRAS ===");

        int tipo = 8;
        String dados;
        int altura = 60;
        int largura = 2;
        int hri = 2;
        int avanco = 10;

        System.out.print("Dados do código de barras (ex: {A012345678912): ");
        dados = sc.nextLine();

        if (dados.isBlank()) {
            println("Erro: O código de barras não pode estar vazio.");
            return;
        }

        if (!dados.startsWith("{A") && !dados.startsWith("{B") && !dados.startsWith("{C")) {
            println("Erro: O código deve começar com {A, {B ou {C.");
            return;
        }

        if (simular) {
            println("[TESTE] Código de barras impresso com sucesso! Dados: " + dados);
            println("[TESTE] Avançando 6 linhas...");
            println("[TESTE] Cortando papel...");
            return;
        }

        try {
            int retorno = ImpressoraDLL.INSTANCE.ImpressaoCodigoBarras(
                    tipo, dados, altura, largura, hri
            );

            if (retorno != 0) {
                println("Erro ao imprimir código de barras: " + retorno);
                return;
            }


            int retAvanco = ImpressoraDLL.INSTANCE.AvancaPapel(6);

            if (retAvanco != 0) {
                println("Erro ao avançar papel: " + retAvanco);
                return;
            }


            int retCorte = ImpressoraDLL.INSTANCE.Corte(avanco);

            if (retCorte != 0) {
                println("Erro ao cortar papel: " + retCorte);
                return;
            }

            println("Código de barras impresso e cortado com sucesso!");

            ImpressoraDLL.INSTANCE.SinalSonoro(1, 50, 5);

        } catch (LinkageError erro) {
            println("Erro ao acessar a DLL: " + erro.getMessage());
        }
    }

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

        if (retorno >= 0) {
            println("Cupom SAT (XML) impresso com sucesso! Retorno: " + retorno);

            // Corte e Bip no final
            int retCorte = ImpressoraDLL.INSTANCE.Corte(avanco);
            if (retCorte >= 0) {
                println("Papel cortado com sucesso!");
                ImpressoraDLL.INSTANCE.SinalSonoro(1, 50, 5);
            }
        } else {
            println("Erro ao imprimir XML SAT. Retorno: " + retorno);
        }
    }
}


/**
 if (isConnected() == true){
 println("A conexão está ativa");

 } else if (isConnected() == false){
 println("A conexão está desativada");

 }
 */



