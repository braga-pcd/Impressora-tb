public class Main {
    public static void main(String[] args) {

        Impressora imp = new Impressora();
        // imp.simular = true; // Teste sem impressora fisica

        Impressora.println("=== TESTE USB ===");
        imp.configurarIm(1, "i9", "USB", 0);
        imp.openIm();
        Impressora.println("Conectada: " + imp.isConnected());
        imp.closeIm();

        Impressora.println("\n=== TESTE BLUETOOTH ===");
        imp.configurarIm(4, "i9", "AA:BB:CC:DD:EE:FF", 0);
        imp.openIm();
        Impressora.println("Conectada: " + imp.isConnected());
        imp.closeIm();

        Impressora.println("\n=== TESTE SERIAL ===");
        imp.configurarIm(2, "i8", "COM2", 9600);
        imp.openIm();
        imp.closeIm();

        Impressora.println("\n=== TESTE REDE ===");
        imp.configurarIm(3, "i9", "192.168.0.20", 9100);
        imp.openIm();
        imp.closeIm();

        Impressora.println("\n=== TESTE DE ERROS ===");
        imp.closeIm(); // Ja esta fechada
        imp.configurarIm(9, "i9", "USB", 0); // Tipo invalido
        Impressora.println("Conectada no final: " + imp.isConnected());
    }
}
