public class TNode {
    Point point;
    public TNode right , left;


    public TNode (float[] coordinate) {
        point = new Point(coordinate);
        right = left = null;
    }

    //for knn
    public TNode (float[] coordinate, int lbl) {
        point = new Point(coordinate, lbl);
        right = left = null;
    }

    public void showPoint() {
        String s = "(";
        for(int i = 0; i < point.getCoordinate().length; i++){
            s += point.getCoordinate()[i] + ", ";
        }
        s += ")";
        System.out.println(s);
    }

    public float distance(float[] point) {
        float sum = 0;
        for(int i = 0; i < point.length; i++)
            sum += Math.pow(point[i] - this.point.getCoordinate()[i], 2);
        return (float) Math.sqrt(sum);
    }

    public float[] getCoordinate() {
        return this.point.getCoordinate();
    }

    public boolean equals(TNode t) {
        for(int i = 0; i < t.getCoordinate().length; i++)
            if(t.getCoordinate()[i] != this.getCoordinate()[i])
                return false;
        return true;
    }
}
