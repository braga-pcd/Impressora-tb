class Usuario {

    String nome = "admin";
    String senha = "123";

    boolean conferir(String nomeDigitado, String senhaDigitada) {
        return nome.equals(nomeDigitado) && senha.equals(senhaDigitada);
    }
}
