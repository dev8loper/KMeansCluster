import java.util.ArrayList;
import java.util.Collections;

public class KMeans {
    final static int DONGU_LIMIT = 5000; // döngü limit(max)

    public static void main(String[] args) {
        int kumeSayisi = 3; //Küme Sayısı
        double[][] veriseti = { // Verisetimiz
            {10, 7},
            {2, 6},
            {3, 6},
            {1, 7},
            {2, 7},
            {9, 4},
            {5, 3},
            {3, 2},
            {8, 5},
            {10, 5},
            {4, 4},
            {6, 2},
            {3, 8},
            {9, 6},
            {4, 3},
            {6, 4},
            {2, 5},
        };

        //Veriyi Arraylist'e aktar
        ArrayList<double[]> veri = new ArrayList<>();
        Collections.addAll(veri, veriseti);

        //Başlangıçta veriseti
        System.out.println("Veri Seti (X,Y)");
        for (double[] nokta:veri) {
            System.out.println("X: " + nokta[0] + ", Y: " + nokta[1]);
        }
        System.out.println("\n");

        //Kümelenmiş veri
        ArrayList<ArrayList<double[]>> kumeler = kumeler(veri, kumeSayisi);

        //Kümelenmiş veriyi ekrana bastır
        int i = 1;
        for (ArrayList<double[]> kume : kumeler) {
            System.out.println(i + ".Küme: ");
            for (double[] nokta : kume) {
                System.out.println("X: " + nokta[0] + ", Y: " + nokta[1]);
            }
            System.out.println("\n");
            i++;
        }

    }

    //K-Means algoritması
    public static ArrayList<ArrayList<double[]>> kumeler(ArrayList<double[]> veri, int kumeSayisi) {
        ArrayList<ArrayList<double[]>> kumeler = new ArrayList<>();
        int limit = DONGU_LIMIT;

        //Veriyi karıştır
        Collections.shuffle(veri);

        //Veriyi rastgele kümelere ata
        int grubLimit = veri.size() / kumeSayisi;
        for (int i = 0, k = 0; kumeSayisi > i; i++) {
            ArrayList<double[]> kume = new ArrayList<>();
            if (kumeSayisi == (i + 1))
                grubLimit += veri.size() % kumeSayisi;
            for (int j = 0; grubLimit > j; j++, k++) {
                kume.add(veri.get(k));
            }
            kumeler.add(kume);
        }


        // Döngü bayrağı
        boolean loopStatus = true;

        // Öklid bağlantısı ile noktaları en yakın merkeze göre güncelle
        while (limit > 0 && loopStatus) {
            ArrayList<ArrayList<double[]>> temp = new ArrayList<>();
            double[][] merkezNoktalar = new double[kumeSayisi][2];

            // kümelerin merkez noktalarını hesapla
            int i = 0;
            for (ArrayList<double[]> kume : kumeler) {
                ArrayList<double[]> tempKume = new ArrayList<>();
                for (double[] nokta : kume) {
                    merkezNoktalar[i][0] += nokta[0];
                    merkezNoktalar[i][1] += nokta[1];
                }
                merkezNoktalar[i][0] = merkezNoktalar[i][0] / kume.size();
                merkezNoktalar[i][1] = merkezNoktalar[i][1] / kume.size();

                temp.add(tempKume);
                i++;
            }

            //Öklid uzaklığına göre en yakın gruba ekle
            for (double[] nokta: veri) {
                int merkezBul = 0;
                double enYakin = oklid(nokta, merkezNoktalar[0]);
                double oklid;
                int l = 0;
                for (double[] merkez: merkezNoktalar) {
                    oklid = oklid(nokta, merkez);
                    if (oklid < enYakin) {
                        enYakin = oklid;
                        merkezBul = l;
                    }
                    l++;
                }

                ArrayList<double[]> tempGrub = temp.get(merkezBul);
                tempGrub.add(nokta);
                temp.set(merkezBul, tempGrub);
            }

            //Eski ve yeni kümeler eşitse döngü bayrağını indir
            if(kumeler.containsAll(temp) && temp.containsAll(kumeler))
                loopStatus = false;

            kumeler = temp;

            limit--;
        }

        return kumeler;
    }

    //Öklid uzaklığını ölçer
    public static double oklid(double[] nokta1, double[] nokta2) {
        if (nokta1.length != nokta2.length) return 0;
        double toplam = 0.0;
        for (int i = 0; i < nokta1.length; i++) {
            toplam += Math.abs((nokta1[i] - nokta2[i]) * (nokta1[i] - nokta2[i]));
        }
        return Math.sqrt(toplam);
    }
}
