package ml.jc.magneto;

public class MainClass {

    public static void main(String[] args)
    {
        String[] dna = {"ATGAGA","CAATGC","TAATGT","AGAAOG","OCCCTA","TCACTG"};
        for (String s : dna) {
            System.out.println(s);
        }
        System.out.println(isMutant( dna ));

    }

    public static boolean isMutant(String[] dna)
    {
        int total_4_sequences = 0;
        int n = dna.length;

        //Evaluar horizontalmente
        for (String row : dna) {
            char x = row.charAt(0);
            int count_genome = 1;
            for (int i = 1; i < n; i++) {
                if(x == row.charAt(i))
                    count_genome++;
                else if (i == n - 3)
                    break;
                else
                    count_genome = 1;

                if(count_genome >= 4)
                    total_4_sequences++;

                if(total_4_sequences >= 2)
                    return true;
            }
        }

        //Evaluar verticalmente
        for (int i = 0; i < dna.length; i++) {
            char x = dna[0].charAt(i);
            int count_genome = 1;
            for (int j = 1; j < n; j++) {

                if(x == dna[j].charAt(i))
                    count_genome++;
                else if (j == n - 3)
                    break;
                else
                    count_genome = 1;

                if(count_genome >= 4)
                    total_4_sequences++;

                if(total_4_sequences >= 2)
                    return true;
            }
        }

        //Evaluar diagonales principales
        int i = n - 4;
        int j = 0;
        while(!(i == 0 && j == n-4))
        {
            int limit = n - j;
            if (i > 0)
                limit = n - i;

            char x = dna[i].charAt(j);
            int count_genome = 1;
            for (int k = 1; k < limit; k++) {
                if(x == dna[i + k].charAt(j + k))
                    count_genome++;
                else if (k == limit - 3)
                    break;
                else
                    count_genome = 1;

                if(count_genome >= 4)
                    total_4_sequences++;

                if(total_4_sequences >= 2)
                    return true;
            }

            if(i > 0)
                i--;
            else if(j < n)
                j++;
        }

        //Evaluar diagonales secundarias
        i = 3;
        j = 0;
        while(!(i == n - 1 && j == 2))
        {
            int limit = i + 1;
            if (i  == n - 1)
                limit = n - j;

            char x = dna[i].charAt(j);
            int count_genome = 1;
            for (int k = 1; k < limit; k++) {
                if(x == dna[i - k].charAt(j + k))
                    count_genome++;
                else if (k == limit - 3)
                    break;
                else
                    count_genome = 1;

                if(count_genome >= 4)
                    total_4_sequences++;

                if(total_4_sequences >= 2)
                    return true;
            }

            if(i < n - 1)
                i++;
            else if(j < n)
                j++;
        }

        return false;
    }


}
