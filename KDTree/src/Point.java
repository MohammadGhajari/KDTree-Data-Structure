public class Point {

    private float[] coordinate;
    public int label;

    public Point(float[] coordinate) {
        this.coordinate = coordinate;
    }

    //for knn
    public Point(float[] coordinate, int label) {
        this.coordinate = coordinate;
        this.label = label;
    }

    public float[] getCoordinate() {
        return coordinate;
    }
}
