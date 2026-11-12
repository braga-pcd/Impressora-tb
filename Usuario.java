class Usuario {

    String nome = "Nicolas";
    String senha = "67Brainroot";

    boolean conferir(String nomeDigitado, String senhaDigitada) {
        return nome.equals(nomeDigitado) && senha.equals(senhaDigitada);
    }
}
