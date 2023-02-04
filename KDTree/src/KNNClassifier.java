public class KNNClassifier {

    private KDTree kdTree;
    private int k;
    private float[][] points;
    private int[] labels;


    public KNNClassifier(float[][] train_data, int[] labels, int k) {
        kdTree = new KDTree(728);
        kdTree.createTree(train_data, labels);
        this.k = k;
        this.labels = labels;
        this.points = train_data;
    }

    public int classify(float[] data_point) {
        int[] lbls = kdTree.findMNearestLbl(data_point, k);
        int[] counts = new int[10];

        for(int i = 0; i < k; i++) {
            switch (lbls[i]) {
                case 0:
                    counts[0]++;
                    break;
                case 1:
                    counts[1]++;
                    break;
                case 2:
                    counts[2]++;
                    break;
                case 3:
                    counts[3]++;
                    break;
                case 4:
                    counts[4]++;
                    break;
                case 5:
                    counts[5]++;
                    break;
                case 6:
                    counts[6]++;
                    break;
                case 7:
                    counts[7]++;
                    break;
                case 8:
                    counts[8]++;
                    break;
                case 9:
                    counts[9]++;
                    break;
            }
        }
        int max = -1, pos = 0;
        for(int i = 0; i < 10; i++) {
            if(counts[i] > max) {
                max = counts[i];
                pos = i;
            }
        }
        return pos;
    }

    public int[] classifyAll(float[][] data_points) {
        int t = data_points.length;
        int[] lbls = new int[t];
        for(int i = 0; i < t; i++)
            lbls[i] = classify(data_points[i]);
        return lbls;
    }

    public float accuracy(int[] labels_true, int[] labels_predicted) {
        int count = 0;
        for(int i = 0; i < labels_true.length; i++) {
            if(labels_predicted[i] == labels_true[i])
                count++;
        }


        return (float) count / (float) labels_predicted.length;
    }

}
