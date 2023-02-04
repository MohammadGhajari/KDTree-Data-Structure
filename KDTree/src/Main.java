import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception{

//this is for class Table
//    Scanner scanner = new Scanner(new File("test.txt"));
//    String s = "", sample = "";
//    int lines = 0;
//    while (scanner.hasNext()) {
//        s += scanner.nextLine() + "\n";
//        lines++;
//        if(lines == 1)
//            sample = s;
//    }
//    s = s.substring(0, s.length() - 1);
//    int second_dimension = 0;
//    for(int i = 0; i < sample.length(); i++)
//        if(sample.charAt(i) == ',')
//            second_dimension++;
//    float[][] records = new float[lines - 1][second_dimension + 1];
//    fillArray(records, s);
//    Table table = new Table(records);
//    float[] upper = {40, 26000, 8}, lower = {20, 1000, 2};
//    float[][] points = table.search(lower, upper);
//    for(int i = 0; i < points.length; i++) {
//        for(int j = 0; j < points[i].length; j++)
//            System.out.print(points[i][j] + " ");
//        System.out.println();
//    }


        //read train file and its labels
        Scanner scanner = new Scanner(new File("train.csv"));
        int lines = 0;
        while (scanner.hasNext()) {
            lines++;
            scanner.nextLine();
        }
        float[][] points = new float[lines][768];
        int[] labels = new int[lines];
        scanner = new Scanner(new File("train.csv"));
        Scanner scanner2 = new Scanner(new File("train_labels.csv"));
        for(int i = 0; i < lines; i++) {
            String[] cof = scanner.nextLine().split(",");
            for(int j = 0; j < 768; j++)
                    points[i][j] = Float.parseFloat(cof[j]);
            labels[i] = Integer.parseInt(scanner2.nextLine());
        }


        KNNClassifier knnClassifier = new KNNClassifier(points, labels, 5);

        //read test file and its labels
        Scanner scanner1 = new Scanner(new File("test.csv"));
        int trues = 0;
        while (scanner1.hasNext()) {
            trues++;
            scanner1.nextLine();
        }
        float[][] Test_points = new float[trues][768];
        int[] true_labels = new int[trues];
        scanner1 = new Scanner(new File("test.csv"));
        Scanner scanner3 = new Scanner(new File("test_labels.csv"));
        for(int i = 0; i < trues; i++) {
            String[] cof = scanner1.nextLine().split(",");
            for(int j = 0; j < 768; j++)
                Test_points[i][j] = Float.parseFloat(cof[j]);
            true_labels[i] = Integer.parseInt(scanner3.nextLine());
        }


        int[] predicted = knnClassifier.classifyAll(Test_points);
        float g = knnClassifier.accuracy(true_labels, predicted);
        System.out.println(g);




    }


    //for Table
    public static void fillArray(float[][] records, String s) {
        Scanner scanner = new Scanner(s);
        scanner.nextLine();
        for(int i = 0; i < records.length; i++) {
            String t = scanner.nextLine();
            int k = 0;
            for(int j = 0; j < records[i].length; j++) {
                String g = "";
                while (k < t.length() && t.charAt(k) != ',') {
                    g += t.charAt(k);
                    k++;
                }//10, 2, 3
                k += 2;
                records[i][j] = Float.parseFloat(g);
            }
        }
    }


}
