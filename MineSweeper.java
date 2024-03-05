package MineSweeper;

import java.util.Random;
import java.util.Scanner;

public class MineSweeper {

    // Gerekli değişkenlerin tanımlanması
    Scanner input = new Scanner(System.in);
    Random randomMineNumber = new Random();
    int row;
    int column;
    String[][] map; // Mayın haritasını tutar
    String[][] frame;// Kullanıcıya gösterilecek oyun alanını tutar
    int mineNumber; // Toplam mayın sayısı

    public void run() {
        // Oyun başlangıcında kullanıcıdan oyun alanının boyutlarını alır
        System.out.println(" *** Welcome to Minesweeper Game *** ");
        System.out.print("Number of Row    : ");
        row = input.nextInt(); // Satır sayısı

        System.out.print("Number of Column : ");
        column = input.nextInt(); // Sütun sayısı
        // Mayın sayısını hesaplar, toplam hücre sayısının çeyreği kadardır.
        mineNumber = (row * column) / 4;

        // Harita ve çerçeve dizilerini başlatır, (girilen satır ve sütun sayı kadar).
        map = new String[row][column];
        frame = new String[row][column];

        // Harita ve çerçeveyi "-" ile doldurur, boş alanları temsil eder.
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                frame[i][j] = "-";
                map[i][j] = "-";
            }
        }

        while (mineNumber > 0) { // Mayın sayısı kadar döngü çalışmaya devam eder
            int rowMineNumber = randomMineNumber.nextInt(row); // satır sayısı büyüklüğü kadar rastgele sayı üretir.
            int columnMineNumber = randomMineNumber.nextInt(column); // sütun sayısı büyüklüğü kadar rastgele sayı
                                                                     // üretir.

            if (map[rowMineNumber][columnMineNumber].equals("-")) {
                map[rowMineNumber][columnMineNumber] = "*"; // üretilen satır ve sütun sayısına denk gelen kısımlara
                                                            // mayınlar yerleştirilir.
                mineNumber--; // mayın yerleştirildikçe azaltılır ve bitene kadar bu döngü devam eder.
            }

        }

        printFrame(); // Oyun çerçevesini gösterir.
        playCheck(); // playCheck metodunu çağırdık ve oyunu başlattık.
    }

    public void playCheck() {
        boolean finish = false;
        // Mayına basmadığı sürece kullanıcıdan işaretlemesi gereken satır ve sütun
        // sorulacaktır.
        while (!finish) {
            System.out.print("Number of Row    : ");
            int selectedRow = input.nextInt();
            System.out.print("Number of Column : ");
            int selectedColumn = input.nextInt();

            // Seçilen hücrenin çevresindeki mayın sayısını tutacak değişken
            int mineNumber = 0;
            // Seçilen hücrenin geçerli bir hücre olup olmadığı kontrol edilir.
            if (selectedRow < row && selectedColumn < column) {
                // Eğer seçilen hücre daha önce seçilmemişse ve mayın değilse
                if (map[selectedRow][selectedColumn].equals("-") && frame[selectedRow][selectedColumn].equals("-")) {
                    // Seçilen hücrenin çevresindeki hücreleri kontrol eder(yakındaki mayınları
                    // öğrenebilmek için).
                    for (int i = selectedRow - 1; i < selectedRow + 2; i++) {
                        for (int j = selectedColumn - 1; j < selectedColumn + 2; j++) {
                            // Eğer çevredeki hücre geçerli bir hücreyse ve mayın içeriyorsa
                            if (i >= 0 && j >= 0 && i < row && j < column && map[i][j].equals("*")) {
                                // Mayın sayısını arttır.
                                mineNumber++;
                                // Hesaplanan mayın sayısı, seçilen hücreye yazılır.
                                frame[selectedRow][selectedColumn] = Integer.toString(mineNumber);
                            } else {
                                frame[selectedRow][selectedColumn] = Integer.toString(mineNumber);
                            }

                        }
                    }
                    // Oyun alanı yeniden yazdırılır son hali ile
                    printFrame();

                    // Oyunun kazanılıp kazanılmadığı kontrol edilir.
                    if (!checkWin()) {
                        // Eğer kazanılmışsa, kazanma mesajı gösterilir ve oyun bitirilir.
                        System.out.println("Congratulations! You won the game...");
                        printMap();
                        finish = true;
                    }

                } else if (map[selectedRow][selectedColumn].equals("*")) {
                    // Eğer seçilen hücre mayınsa, kaybetme mesajı gösterilir ve oyun bitirilir.
                    System.out.println("You lost. You stepped on a mine.!! ");
                    printMap();
                    finish = true;
                    // Eğer seçilen hücre daha önce seçilmişse, uyarı mesajı verilir.
                } else if (!frame[selectedRow][selectedColumn].equals("-")) {
                    System.out.println("Previously entered! Enter a different cell...");
                }

                // Eğer seçilen hücre geçersizse, hata mesajı verilir.
            } else {
                System.out.println("Incorrect entry! Do not choose from outside the playground.");
            }
        }
    }

    public void printMap() {

        // map dizisinin her bir satırını döngü ile geziyor
        for (String[] row : map) {
            // Her bir satırdaki sütunu döngü ile geziyor
            for (String column : row) {
                // Sütun değerini konsola basar (mayın "*" veya boş alan "-")
                System.out.print(column + " ");
            }
            // Her satırın sonunda bir satır alta geçer
            System.out.println();
        }
        System.out.println("====================");
    }

    public void printFrame() {
        // frame dizisinin her bir satırını döngü ile geziyoruz
        for (String[] row : frame) {
            // Her bir satırdaki sütunu döngü ile geziyoruz
            for (String column : row) {
                // Sütun değerini konsola basar (mayın sayısı veya boş alan "-")
                System.out.print(column + " ");
            }
            System.out.println();
        }
        System.out.println("====================");
    }

    boolean checkWin() {
        int emptyCell = 0; // Boş hücrelerin sayısını tutar
        int minedCell = 0; // Mayınlı hücrelerin sayısını tutar

        // frame dizisini döngü ile dolaşır
        for (int i = 0; i < frame.length; i++) {
            for (int j = 0; j < frame[i].length; j++) {
                if (frame[i][j].equals("-")) {
                    emptyCell++;
                }
                // Eğer map'te bir hücre "*" (mayınlı) ise, mayınlı hücre sayısını arttırır
                if (map[i][j].equals("*")) {
                    minedCell++;
                }
            }
        }
        // Eğer boş hücre sayısı mayınlı hücre sayısına eşitse, tüm boş hücreler açılmış
        // demektir
        // bu da kazanılmış demekttir
        if (emptyCell == minedCell) {
            return false;
        }
        return true;
    }
}