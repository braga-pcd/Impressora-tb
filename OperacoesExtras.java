import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

// Parte 4: comandos extras, separados do menu.
class OperacoesExtras {

    void cancelamento(Impressora imp) {
        try {
            // O professor forneceu este XML de exemplo.
            String xml = Files.readString(Path.of("CANC_SAT.xml"));

            // Pega a assinatura que ja existe dentro do XML.
            String inicioTag = "<assinaturaQRCODE>";
            String fimTag = "</assinaturaQRCODE>";
            int inicio = xml.indexOf(inicioTag);
            int fim = xml.indexOf(fimTag);

            if (inicio == -1 || fim == -1) {
                Impressora.println("Assinatura nao encontrada no XML.");
                return;
            }
            String assinatura = xml.substring(inicio + inicioTag.length(), fim);

            if (imp.simular) {
                Impressora.println("[TESTE] XML de cancelamento lido e enviado!");
                return;
            }

            int retorno = ImpressoraDLL.INSTANCE.ImprimeXMLCancelamentoSAT(xml, assinatura, 0);
            if (retorno == 0) {
                Impressora.println("Cancelamento enviado para impressao!");
            } else {
                Impressora.println("Erro no cancelamento. Codigo: " + retorno);
            }
        } catch (IOException erro) {
            Impressora.println("Arquivo CANC_SAT.xml nao encontrado ou inacessivel.");
        }
    }

    void gaveta(Impressora imp) {
        if (imp.simular) {
            Impressora.println("[TESTE] Gaveta aberta!");
            return;
        }
        int retorno = ImpressoraDLL.INSTANCE.AbreGaveta(0, 5, 10);
        if (retorno == 0) {
            Impressora.println("Comando de abrir gaveta enviado!");
        } else {
            Impressora.println("Erro na gaveta. Codigo: " + retorno);
        }
    }

    void bip(Impressora imp) {
        int quantidade = Main.lerNumero("Quantidade de bips (1 a 63): ");
        if (quantidade < 1 || quantidade > 63) {
            Impressora.println("Quantidade invalida!");
            return;
        }
        if (imp.simular) {
            Impressora.println("[TESTE] " + quantidade + " bip(s)!");
            return;
        }
        int retorno = ImpressoraDLL.INSTANCE.SinalSonoro(quantidade, 50, 5);
        if (retorno == 0) {
            Impressora.println("Bip enviado!");
        } else {
            Impressora.println("Erro no bip. Codigo: " + retorno);
        }
    }

    void avancarPapel(Impressora imp) {
        int linhas = Main.lerNumero("Quantas linhas avancar? ");
        if (linhas <= 0) {
            Impressora.println("Informe um numero maior que zero.");
            return;
        }
        if (imp.simular) {
            Impressora.println("[TESTE] Avancou " + linhas + " linhas.");
            return;
        }
        int retorno = ImpressoraDLL.INSTANCE.AvancaPapel(linhas);
        if (retorno == 0) {
            Impressora.println("Papel avancado!");
        } else {
            Impressora.println("Erro ao avancar. Codigo: " + retorno);
        }
    }

    void statusPapel(Impressora imp) {
        if (imp.simular) {
            Impressora.println("[TESTE] Papel disponivel (simulado).");
            return;
        }
        int retorno = ImpressoraDLL.INSTANCE.StatusImpressora(3);
        if (retorno == 5) {
            Impressora.println("Tem papel!");
        } else if (retorno == 6) {
            Impressora.println("Papel acabando!");
        } else if (retorno == 7) {
            Impressora.println("Sem papel!");
        } else {
            Impressora.println("Status nao identificado. Codigo: " + retorno);
        }
    }
}
