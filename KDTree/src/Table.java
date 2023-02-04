public class Table {

    private KDTree kdTree;


    public Table (float[][] recs) {
        kdTree = new KDTree(recs[0].length);
        kdTree.createTree(recs);
    }

    public float[][] search(float[] lower_bounds, float[] upper_bounds) {
        return kdTree.searchRange(lower_bounds, upper_bounds);
    }
}
