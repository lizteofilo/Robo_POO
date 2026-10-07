package src.classes;

public class Tabuleiro {

    public static final int LADO = 4;
    public static boolean USAR_COR = true;
    public static int ATRASO_MS = 700;

    private static final int LARG = 14;
    private static final String PREFIXO = "    ";

    private int alimentoX;
    private int alimentoY;
    private boolean mostrarAlimento = true;


    private Obstaculo[][] obstaculos = new Obstaculo[LADO][LADO];

    public Tabuleiro(int alimentoX, int alimentoY) {
        if (!posicaoValida(alimentoX, alimentoY)) {
            throw new IllegalArgumentException("Posição do alimento fora do tabuleiro: (" + alimentoX + "," + alimentoY + ")");
        }
        this.alimentoX = alimentoX;
        this.alimentoY = alimentoY;
    }

    public static boolean posicaoValida(int x, int y) {
        return x >= 0 && x < LADO && y >= 0 && y < LADO;
    }

    public int getAlimentoX() {
        return alimentoX;
    }

    public int getAlimentoY() {
        return alimentoY;
    }


    public void setMostrarAlimento(boolean mostrarAlimento) {
        this.mostrarAlimento = mostrarAlimento;
    }


    public boolean inserirObstaculo(Obstaculo o, int x, int y) {
        if (!posicaoValida(x, y)) return false;
        if (obstaculos[x][y] != null) return false;
        if (x == alimentoX && y == alimentoY) return false;
        if (x == 0 && y == 0) return false;
        obstaculos[x][y] = o;
        return true;
    }


    public void verificarObstaculo(Robo r) {
        if (r.isExplodido()) return;
        int x = r.getX();
        int y = r.getY();
        Obstaculo o = obstaculos[x][y];
        if (o != null) {
            o.bater(r);
            if (r.isExplodido()) {
                obstaculos[x][y] = null;
            }
        }
    }


    public static void pausar() {
        if (ATRASO_MS <= 0) return;
        try {
            Thread.sleep(ATRASO_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    public static String colorir(String texto, String cor) {
        if (!USAR_COR) return texto;
        String codigo = codigoCor(cor);
        if (codigo == null) return texto;
        return "\u001B[1;" + codigo + "m" + texto + "\u001B[0m";
    }

    private static String codigoCor(String cor) {
        if (cor == null) return null;
        switch (cor.toLowerCase()) {
            case "azul":
                return "94";
            case "verde":
                return "92";
            case "vermelho":
                return "91";
            case "amarelo":
                return "93";
            case "roxo":
                return "95";
            case "ciano":
                return "96";
            default:
                return null;
        }
    }

    public void imprimir(Robo... robos) {
        String borda = PREFIXO + repetir("+" + repetir("-", LARG), LADO) + "+";
        String vazia = PREFIXO + repetir("|" + repetir(" ", LARG), LADO) + "|";

        System.out.println();
        System.out.println("  y");
        for (int y = LADO - 1; y >= 0; y--) {
            System.out.println(borda);
            System.out.println(vazia);

            StringBuilder linha = new StringBuilder(" " + y + "  ");
            for (int x = 0; x < LADO; x++) {
                linha.append("|").append(celula(x, y, robos));
            }
            linha.append("|");
            System.out.println(linha);

            System.out.println(vazia);
        }
        System.out.println(borda);

        StringBuilder eixoX = new StringBuilder(PREFIXO);
        for (int x = 0; x < LADO; x++) {
            eixoX.append(" ").append(centralizar(String.valueOf(x), LARG));
        }
        eixoX.append("   x");
        System.out.println(eixoX);
        System.out.println();
    }


    public void imprimirSituacao(Robo... robos) {
        System.out.println("Situação dos robôs:");
        for (Robo r : robos) {
            StringBuilder msg = new StringBuilder("  ");
            msg.append(r.getRotulo()).append(" (").append(r.getCor()).append(", ").append(r.getTipo()).append(")");

            if (r.isExplodido()) {
                msg.append(" explodiu em (").append(r.getX()).append(",").append(r.getY()).append(")");
            } else {
                msg.append(" está em (").append(r.getX()).append(",").append(r.getY()).append(")");
                if (r.encontrouAlimento(alimentoX, alimentoY)) {
                    msg.append(" -> encontrou o alimento!");
                }
            }
            System.out.println(msg);
        }
        System.out.println();
    }


    private String celula(int x, int y, Robo[] robos) {
        StringBuilder visivel = new StringBuilder();
        StringBuilder colorido = new StringBuilder();

        for (Robo r : robos) {
            if (!r.isExplodido() && r.getX() == x && r.getY() == y) {
                adicionar(visivel, colorido, r.getNome(), r.getCor());
            }
        }
        if (obstaculos[x][y] != null) {
            if (obstaculos[x][y] instanceof Bomba) {
                adicionar(visivel, colorido, "BOMBA", "vermelho");
            } else {
                adicionar(visivel, colorido, "ROCHA", "roxo");
            }
        }
        if (mostrarAlimento && x == alimentoX && y == alimentoY) {
            adicionar(visivel, colorido, "ALIMENTO", "amarelo");
        }

        int sobra = LARG - visivel.length();
        int esquerda = Math.max(0, sobra / 2);
        int direita = Math.max(0, sobra - esquerda);
        return repetir(" ", esquerda) + colorido + repetir(" ", direita);
    }

    private void adicionar(StringBuilder visivel, StringBuilder colorido, String texto, String cor) {
        if (visivel.length() > 0) {
            visivel.append(" ");
            colorido.append(" ");
        }
        visivel.append(texto);
        colorido.append(colorir(texto, cor));
    }

    private static String centralizar(String texto, int largura) {
        int sobra = largura - texto.length();
        int esquerda = sobra / 2;
        return repetir(" ", esquerda) + texto + repetir(" ", sobra - esquerda);
    }

    private static String repetir(String s, int vezes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vezes; i++) sb.append(s);
        return sb.toString();
    }
}