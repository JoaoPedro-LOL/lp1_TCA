import com.sun.jna.Library;
import com.sun.jna.Native;

public class TCA_FNF {

    public static void setCor256(int cor) {
        if (cor < 0 || cor > 255) {
            resetColor();
            return;
        }
        System.out.print("\u001b[38;5;" + cor + "m");
    }

   

    public static void resetColor() {
        System.out.print("\u001b[0m");
    }

    public interface Crt extends Library {
        Crt INSTANCE = Native.load("msvcrt", Crt.class);

        int _kbhit();

        int _getch();
    }

    public static boolean pressionouTecla() {
        return Crt.INSTANCE._kbhit() != 0;
    }

    public static int obtemTeclaPressionada() {
        return Crt.INSTANCE._getch();

    }

    public static void limparConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void gotoXY(int linha, int coluna) {
        char escCode = 0x1B;
        System.out.print(String.format("%c[%d;%df", escCode, linha, coluna));
    }

    public static void iniciarJogoComDificuldade(int d) {
        limparConsole();

        double intervalo;
        double facil = 0.75;
        double medio = 0.5;
        double dificil = 0.25;
        double impossivel = 0.15;
        double padrao = 1.0;

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

        long espera = (long) (intervalo * 1000);
        int linhas = 10;
        int colunas = 4;
        int pontos = 0;

        char[][] matriz = new char[linhas][colunas];

        char[] teclasColunas = { 'w', 'e', 'r', 't' };


        for (int i = 0; i < linhas; i++)
            for (int j = 0; j < colunas; j++)
                matriz[i][j] = ' ';

        while (true) {

            if (pressionouTecla()) {
                int lol = obtemTeclaPressionada();
                if (lol == 27)
                    return;
            }

            for (int i = linhas - 1; i > 0; i--) {
                for (int j = 0; j < colunas; j++) {
                    matriz[i][j] = matriz[i - 1][j];
                }
            }

            for (int j = 0; j < colunas; j++)
                matriz[0][j] = ' ';
            int nomeLegal = (int) (Math.random() * 4);
            matriz[0][nomeLegal] = teclasColunas[nomeLegal];

            limparConsole();
            System.out.println("Pontos: " + pontos);
            System.out.println("Pressione (ESC) para sair.\n");

            for (int i = 0; i < linhas; i++) {
                System.out.print("| ");
                for (int j = 0; j < colunas; j++) {
                    System.out.print(matriz[i][j] + " ");
                }
                System.out.println("|");
            }

            char teclaNaLinhaFinal = ' ';
            int ultimaColuna = -1;

            for (int j = 0; j < colunas; j++) {
                if (matriz[linhas - 1][j] != ' ') {
                    teclaNaLinhaFinal = matriz[linhas - 1][j];
                    ultimaColuna = j;
                    break;
                }
            }

            if (teclaNaLinhaFinal != ' ') {
                long tempoInicio = System.currentTimeMillis();
                boolean respondeu = false;

                while (System.currentTimeMillis() - tempoInicio < espera) {
                    if (pressionouTecla()) {
                        int t = obtemTeclaPressionada();
                        t = Character.toLowerCase((char) t);

                        if (t == teclaNaLinhaFinal) {
                            pontos++;
                            respondeu = true;
                            break;
                        } else {
                            limparConsole();
                            System.out.println("GAME OVER!");
                            System.out.println("Pontuação final: " + pontos);
                            System.out.println("\nPressione qualquer tecla para voltar ao menu");
                            obtemTeclaPressionada();
                            return;
                        }
                    }
                }
            }

            try {
                Thread.sleep(espera);
            } catch (Exception e) {
            }
        }
    }

    public static void menuDificuldade() {
        String[] dificuldades = new String[4];
        dificuldades[0] = "Fácil       (1.00s)    ";
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
                    setColor(2);
                    System.out.print(dificuldades[i]);
                    resetColor();
                    System.out.println(" ║");
                } else {
                    System.out.println("║  " + dificuldades[i] + " ║");
                }
            }

            System.out.println("╚══════════════════════════╝");
            System.out.println();
            System.out.println("Use (W/S) para navegar e (ENTER) para selecionar. (ESC) para voltar.");

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

                    case 13:
                        iniciarJogoComDificuldade(opc);
                        return;

                    case 27:
                        return;
                }
            }
        }
    }

    public static void setColor(int cor) {
        String s = "[0m";
        switch (cor) {
            case 0:
                s = "[30m";
                break;
            case 1:
                s = "[31m";
                break;
            case 2:
                s = "[32m";
                break;
            case 3:
                s = "[33m";
                break;
            case 4:
                s = "[34m";
                break;
            case 5:
                s = "[35m";
                break;
            case 6:
                s = "[36m";
                break;
            case 7:
                s = "[97m";
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
                setColor(2);
                System.out.print(opcoes[i]);
                setColor(7);
                System.out.println("  ║");
            } else {
                System.out.println("║  " + opcoes[i] + "  ║");
            }
        }
        System.out.println("╚═════════════════════╝");
        System.out.println();
        System.out.println("Use (W/S) para navegar e (ENTER) para selecionar. (ESC) para sair.");

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
                setColor(9);
                System.out.println("Código realizado por João Pedro Peres da Silva e Laura Mayumi Benedito Assakura.");
                System.out.println("Este codigo foi inspirado em guitar hero e friday night funkin'\n\n");
                resetColor();
                break;
            default:
                System.out.println("\n\nOpção inválida.\n\n");
                break;
        }
        System.out.println("Pressione uma tecla para continuar...");
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