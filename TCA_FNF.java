import com.sun.jna.Library;
import com.sun.jna.Native;

public class TCA_FNF {

    // reseta cor para padrão
    public static void resetColor() {
        System.out.print("\u001b[0m");
    }

    //muda a cor das notas 
    public static String corDaNota(char nota) {

        switch (nota) {
            case 'w':
                return "\u001b[34m"; 

            case 'e':
                return "\u001b[32m"; 

            case 'r':
                return "\u001b[33m"; 

            case 't':
                return "\u001b[31m"; 

            default:
                return "\u001b[0m"; 
        }
    }

    public static void printNotaColorida(char nota) {
    if (nota == ' ') {
        System.out.print(" ");
        return;
    }                                        // reset color
    System.out.print(corDaNota(nota) + nota + "\u001b[0m");
}

    public interface Crt extends Library {
        Crt INSTANCE = Native.load("msvcrt", Crt.class);
        int _kbhit();
        int _getch();
    }

    // retorna true se alguma tecla foi pressionada
    public static boolean pressionouTecla() {
        return Crt.INSTANCE._kbhit() != 0;
    }

    // obtém a tecla pressionada
    public static int obtemTeclaPressionada() {
        return Crt.INSTANCE._getch();

    }

    // limpa completamente o console
    public static void limparConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void iniciarJogo(int d) {
        limparConsole();

        // intervalos de tempo para cada dificuldade
        double intervalo;
        double facil = 0.75;
        double medio = 0.5;
        double dificil = 0.25;
        double impossivel = 0.15;
        double padrao = 1.0;

        // define o intervalo com base na dificuldade
        switch (d) {
            case 0:
                intervalo = facil;
                break;

            case 1:
                intervalo = medio;
                break;

            case 2:

                intervalo = dificil;
                break;

            case 3:
                intervalo = impossivel;
                break;

            default:
                intervalo = padrao;
                break;
        }

        // converte segundos para milissegundos
        long espera = (long) (intervalo * 1000);
        int linhas = 10;
        int colunas = 4;
        int pontos = 0;

        // matriz do jogo
        char[][] matriz = new char[linhas][colunas];

        // cada tecla corresponde uma coluna
        char[] teclasColunas = { 'w', 'e', 'r', 't' };

        // inicializa matriz com espaços
        for (int i = 0; i < linhas; i++)
            for (int j = 0; j < colunas; j++)
                matriz[i][j] = ' ';

        while (true) {

            // se apertar esc sai do jogo (ou menu)
            if (pressionouTecla()) {
                int esc = obtemTeclaPressionada();
                if (esc == 27)
                    return;
            }

            // move todas as linhas para baixo (as notas descem)
            for (int i = linhas - 1; i > 0; i--) {
                for (int j = 0; j < colunas; j++) {
                    matriz[i][j] = matriz[i - 1][j];
                }
            }

            // não vai deixar acomular nota na tela
            for (int j = 0; j < colunas; j++)
                matriz[0][j] = ' ';

            // gera uma nota nova
            int notaNova = (int) (Math.random() * 4);
            matriz[0][notaNova] = teclasColunas[notaNova];

            limparConsole();
            System.out.println("Pontos: " + pontos);
            System.out.println("Pressione (ESC) para sair.\n");

            // imprime matriz do jogo
            for (int i = 0; i < linhas; i++) {
                System.out.print("| ");
                for (int j = 0; j < colunas; j++) {
                    printNotaColorida(matriz[i][j]);
                    System.out.print(" ");
                }
                System.out.println("|");
            }

            // procura a nota na última linha
            char teclaNaLinhaFinal = ' ';

            for (int j = 0; j < colunas; j++) {
                if (matriz[linhas - 1][j] != ' ') {
                    teclaNaLinhaFinal = matriz[linhas - 1][j];
                    break;
                }
            }

            // se há nota no final deve ser pressionada a tecla correta

            if (teclaNaLinhaFinal != ' ') {
                long tempoInicio = System.currentTimeMillis();

                while (System.currentTimeMillis() - tempoInicio < espera) {
                    if (pressionouTecla()) {
                        int t = obtemTeclaPressionada();
                        t = Character.toLowerCase((char) t);

                        if (t == teclaNaLinhaFinal) {
                            pontos++;

                            break;

                        } else {
                            limparConsole();
                            System.out.println("GAME OVER!!!");
                            System.out.println("Pontuação final: " + pontos);
                            System.out.println("\nPressione qualquer tecla para voltar ao menu");
                            obtemTeclaPressionada();
                            return;
                        }
                    }
                }
            }

            // faz a matriz do jogo não descer toda de uma vez
            try {
                Thread.sleep(espera);
            } catch (Exception e) {
            }
        }
    }

    public static void menuDificuldade() {
        String[] dificuldades = new String[4];
        dificuldades[0] = "Fácil       (0.75s)    ";
        dificuldades[1] = "Média       (0.50s)    ";
        dificuldades[2] = "Difícil     (0.25s)    ";
        dificuldades[3] = "Impossível  (?????)    ";
        int opc = 0;

        while (true) {
            limparConsole();
            System.out.println();
            System.out.println("╔══════════════════════════╗");
            System.out.println("║      DIFICULDADE         ║");
            System.out.println("╠══════════════════════════╣");

            for (int i = 0; i < dificuldades.length; i++) {
                if (i == opc) {
                    System.out.print("║> ");
                    setColor(1);
                    System.out.print(dificuldades[i]);
                    resetColor();
                    System.out.println(" ║");
                } else {
                    System.out.println("║  " + dificuldades[i] + " ║");
                }
            }

            System.out.println("╚══════════════════════════╝");
            System.out.println();
            System.out.println("Use (W/S) para navegar e (ENTER) para selecionar (ESC) para voltar.");

            if (pressionouTecla()) {
                int ch = obtemTeclaPressionada();

                switch (ch) {
                    case 'w':
                    case 'W':
                        opc--;
                        if (opc < 0)
                            opc = dificuldades.length - 1;
                        break;

                    case 's':
                    case 'S':
                        opc++;
                        if (opc >= dificuldades.length)
                            opc = 0;
                        break;

                    case 13: // ENTER
                        iniciarJogo(opc);
                        return;

                    case 27: // ESC
                        return;
                }
            }
        }
    }

    public static void setColor(int cor) {
        String s = "[0m";
        switch (cor) {
            case 1:
                s = "[32m";
                break;
            case 2:
                s = "[97m";
                break; 
            case 3:
                s = "[35m"; 
                break;
        }

        System.out.print((char) 27 + s);
    }

    public static void atualizarMenu(int opcaoSelecionada, String[] opcoes) {
        limparConsole();
        System.out.println();
        System.out.println("╔═════════════════════╗");
        System.out.println("║         MENU        ║");
        System.out.println("╠═════════════════════╣");
        for (int i = 0; i < opcoes.length; i++) {
            if (i == opcaoSelecionada) {
                System.out.print("║> ");
                setColor(1);
                System.out.print(opcoes[i]);
                setColor(2);
                System.out.println("  ║");
            } else {
                System.out.println("║  " + opcoes[i] + "  ║");
            }
        }
        System.out.println("╚═════════════════════╝");
        System.out.println();
        System.out.println("Use (W/S) para navegar e (ENTER) para selecionar (ESC) para sair.");

    }

    public static void executarAcaoMenu(int opcaoSelecionada) {

        limparConsole();

        switch (opcaoSelecionada) {
            case 0:
                System.out.println("\n\nJogar selecionado.");
                menuDificuldade();
                break;

            case 1:
                
                System.out.println("\n\nSobre selecionado.\n");
                setColor(3);
                System.out.println("Código realizado por João Pedro Peres da Silva e Laura Mayumi Benedito Assakura.");
                System.out.println("Este codigo foi inspirado em guitar hero e friday night funkin'");
                System.out.println("Use as teclas w e r t\n\n");
                System.out.println("Pressione uma tecla para continuar...");
                resetColor();
                break;
        }

        obtemTeclaPressionada();
        limparConsole();

    }

    public static void main(String[] args) {

        String[] opcoes = {
                "Jogar            ",
                "Sobre            ",
        };

        int opcaoSelecionada = 0;
        atualizarMenu(opcaoSelecionada, opcoes);

        while (true) {
            if (pressionouTecla()) {
                int ch = obtemTeclaPressionada();

                switch (ch) {
                    case 'w':
                    case 'W':
                        opcaoSelecionada--;
                        if (opcaoSelecionada < 0) {
                            opcaoSelecionada = opcoes.length - 1;
                        }

                        atualizarMenu(opcaoSelecionada, opcoes);
                        break;

                    case 's':
                    case 'S':
                        opcaoSelecionada++;
                        if (opcaoSelecionada >= opcoes.length) {
                            opcaoSelecionada = 0;
                        }
                        atualizarMenu(opcaoSelecionada, opcoes);
                        break;

                    case 13:
                        executarAcaoMenu(opcaoSelecionada);
                        atualizarMenu(opcaoSelecionada, opcoes);
                        break;

                    case 27:
                        return;
                }

            }

        }
    }
}