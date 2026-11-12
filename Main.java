import java.util.Scanner;

public class Main {

    // Um Scanner para o programa inteiro.
    static Scanner sc = new Scanner(System.in);

    static int lerNumero(String mensagem) {
        while (true) {
            Impressora.print(mensagem);
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException erro) {
                Impressora.println("Digite um numero valido!");
            }
        }
    }

    public static void main(String[] args) {
        Usuario usuario = new Usuario();
        Impressora imp = new Impressora();
        OperacoesExtras extras = new OperacoesExtras();

        imp.simular = false; // true = teste sem impressora; false = impressora real

        // Login: tres tentativas.
        boolean entrou = false;
        for (int tentativa = 1; tentativa <= 3; tentativa++) {
            Impressora.println("\n=== LOGIN ===");
            Impressora.print("Usuario: ");
            String nome = sc.nextLine();
            Impressora.print("Senha: ");
            String senha = sc.nextLine();

            if (usuario.conferir(nome, senha)) {
                entrou = true;
                Impressora.println("Login realizado!");
                break;
            }

            Impressora.println("Dados incorretos. Restam " + (3 - tentativa) + " tentativa(s).");
        }

        if (!entrou) {
            Impressora.println("Acesso bloqueado. Programa encerrado.");
            return;
        }

        // Menu: repete ate escolher 0.
        int opcao = -1;
        while (opcao != 0) {
            Impressora.println("\n=== IMPRESSORA ELGIN ===");
            Impressora.println("1 - Configurar conexao");
            Impressora.println("2 - Abrir conexao");
            Impressora.println("3 - Fechar conexao");
            Impressora.println("4 - Imprimir texto");
            Impressora.println("5 - Imprimir QR Code");
            Impressora.println("6 - Imprimir codigo de barras");
            Impressora.println("7 - Imprimir XML SAT");
            Impressora.println("8 - Imprimir cancelamento SAT");
            Impressora.println("9 - Abrir gaveta");
            Impressora.println("10 - Tocar bip");
            Impressora.println("11 - Avancar papel (extra)");
            Impressora.println("12 - Consultar papel (extra)");
            Impressora.println("0 - Sair");

            opcao = lerNumero("Opcao: ");

            // A regra do professor: nao pedir dados de impressao sem conexao.
            if (opcao >= 4 && opcao <= 12 && !imp.isConnected()) {
                Impressora.println("Conexao fechada. Abra a conexao antes.");
                continue;
            }

            try {
                if (opcao == 1) {
                    Impressora.println("1=USB, 2=Serial, 3=Rede, 4=Bluetooth");
                    int tipo = lerNumero("Tipo: ");

                    if (tipo < 1 || tipo > 4) {
                        Impressora.println("Tipo invalido!");
                        continue;
                    }

                    Impressora.print("Modelo (i8 ou i9): ");
                    String modelo = sc.nextLine();
                    String conexao = "USB";
                    int parametro = 0;

                    if (tipo == 2) {
                        Impressora.print("Porta (ex: COM2): ");
                        conexao = sc.nextLine();
                        parametro = lerNumero("Velocidade (ex: 9600): ");
                    } else if (tipo == 3) {
                        Impressora.print("IP da impressora: ");
                        conexao = sc.nextLine();
                        parametro = lerNumero("Porta da rede (ex: 9100): ");
                    } else if (tipo == 4) {
                        Impressora.print("MAC Bluetooth: ");
                        conexao = sc.nextLine();
                    }

                    imp.configurarIm(tipo, modelo, conexao, parametro);

                } else if (opcao == 2) {
                    imp.openIm();
                } else if (opcao == 3) {
                    imp.closeIm();
                } else if (opcao == 4) {
                    imp.imprimeTexto();
                } else if (opcao == 5) {
                    imp.imprimeQRCode();
                } else if (opcao == 6) {
                    imp.imprimeCodigoBarras();
                } else if (opcao == 7) {
                    imp.imprimeXMLSAT();
                } else if (opcao == 8) {
                    extras.cancelamento(imp);
                } else if (opcao == 9) {
                    extras.gaveta(imp);
                } else if (opcao == 10) {
                    extras.bip(imp);
                } else if (opcao == 11) {
                    extras.avancarPapel(imp);
                } else if (opcao == 12) {
                    extras.statusPapel(imp);
                } else if (opcao == 0) {
                    Impressora.println("Saindo...");
                } else {
                    Impressora.println("Opcao invalida!");
                }
            } catch (LinkageError erro) {
                Impressora.println("Erro ao acessar a DLL: " + erro.getMessage());
            } catch (Exception erro) {
                Impressora.println("Erro: " + erro.getMessage());
            }
        }

        if (imp.isConnected()) {
            imp.closeIm();
        }
        Impressora.println("Programa encerrado.");
    }
}
