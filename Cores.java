public class Cores {
    
    /**
     * Define a cor do texto do console usando o modo de 256 cores.
     * @param cor O número da cor (0-255).
     */
    public static void setCor256(int cor) {
        if (cor < 0 || cor > 255) {
            resetColor();
            return;
        }
        System.out.print("\u001b[38;5;" + cor + "m");
    }

    /**
     * Define a cor de FUNDO do console usando o modo de 256 cores.
     * @param cor O número da cor (0-255).
     */
    public static void setCorFundo256(int cor) {
        if (cor < 0 || cor > 255) {
            resetColor();
            return;
        }        
        System.out.print("\u001b[48;5;" + cor + "m");
    }

    /**
     * Reseta a cor de volta para o padrão do terminal.
     */
    public static void resetColor() {
        System.out.print("\u001b[0m");
    }

    // Testando as cores

    public static void main(String[] args) {
        
        System.out.println("Cores de 0 a 255:");
        for (int i = 0; i < 256; i++) {
            setCor256(i);
            System.out.printf("Cor %-3d ", i);
            if (i % 8 == 7) System.out.println();
        }
        resetColor();

  
   

        System.out.println("\nEscala de Cinza (232-255):");
        for (int i = 232; i <= 255; i++) {
            setCor256(i);
            System.out.printf("C%d ", i);
        }
        resetColor();
        System.out.println("\n");
        

        // Exemplo de uso
        setCor256(197); // Um rosa forte
        System.out.println("Este texto é rosa!");
        
        setCor256(46); // Um verde-limão
        System.out.println("Este texto é verde!");
        
        setCorFundo256(226); // Fundo amarelo claro
        setCor256(21); // Texto azul escuro
        System.out.println(" Combinações de fundo e texto! ");
        
        resetColor();
        System.out.println("Voltou ao normal.");
    }
}