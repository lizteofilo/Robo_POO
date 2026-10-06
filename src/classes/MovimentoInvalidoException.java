package src.classes;

public class MovimentoInvalidoException extends Exception {

    public MovimentoInvalidoException(String mensagem) {
        super(mensagem);
    }
}