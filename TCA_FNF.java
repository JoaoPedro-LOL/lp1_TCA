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

  
    public static void setCorFundo256(int cor) {
        if (cor < 0 || cor > 255) {
            resetColor();
            return;
        }        
        System.out.print("\u001b[48;5;" + cor + "m");
    }

  
    public static void resetColor() {
        System.out.print("\u001b[0m");
    }


    public interface Crt extends Library {
        Crt INSTANCE = Native.load("msvcrt", Crt.class);                
        int _kbhit();
        int _getch();
    }

    public static boolean pressionouTecla(){
        return Crt.INSTANCE._kbhit() != 0;
    }
    
    public static int obtemTeclaPressionada(){
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
                System.out.println("║  " + opcoes[i] +"  ║");
            }
        }
        System.out.println("╚═════════════════════╝");
        System.out.println();
        System.out.println("Use W/S para navegar e ENTER para selecionar. ESC para sair.");

    }

    
    public static double definirDificuldade(int Dificult) {
        double velocidade = 0;
        switch (Dificult) {
            case 1:
                velocidade = 1;
                break;
            case 2:
                velocidade = 0.5;
                break;
            case 3:
                velocidade = 0.25;
                break;
            default:
            System.out.println("Clique em um botão valido");
            
        }

        return velocidade;
    }

    public static void executarAcaoMenu(int opcaoSelecionada) {
        
        limparConsole();

        switch (opcaoSelecionada) {
            case 0:
                System.out.println("\n\nJogar selecionado.\n\n");

                break;
            case 1:
                System.out.println("\n\nConfigurações selecionado.\n\n");


                break;
            case 2:
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

        double dificuladade = definirDificuldade();


        String [] opcoes = {            
            "Jogar            ",
            "Configuracoes    ",
            "Sobre            ",
        };

        int opcaoSelecionada = 0;
        atualizarMenu(  opcaoSelecionada, opcoes);
        
        while (true) {            
            if ( pressionouTecla()) {
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
                        atualizarMenu(  opcaoSelecionada, opcoes);
                        break;

                    case 27: 
                        return; 
                }
                
            }
            
        }
    }
}