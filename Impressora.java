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

    void imprimeTexto(){
        Scanner sc = new Scanner(System.in);

        println("\n=== IMPRESSÃO DE TEXTO ===");

        System.out.println("Dados: ");
        String dados = sc.nextLine();
        System.out.println("Posição: ");
        int posicao = sc.nextInt();
        System.out.println("Estilo: ");
        int estilo = sc.nextInt();
        System.out.println("Tamanho: ");
        int tamanho = sc.nextInt();
        System.out.println("Copias: ");
        int copias = sc.nextInt();

        int retorno = ImpressoraDLL.INSTANCE.ImpressaoTexto(dados, posicao, estilo, tamanho);

        if(retorno >= 0) {
            for (int c = 0; c <= copias; c++) {
                System.out.println("Impressão de Texto OK. Retorno " + retorno);
            }
        }else{
            System.out.println("Erro ao imprimir texto. Retorno " + retorno);
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



