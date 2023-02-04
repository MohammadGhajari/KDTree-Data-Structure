import java.util.Stack;

public class KDTree {
    public TNode Root;
    private final int dimension;
    private TNode best = null;
    private double bestDistance = 0;
    private int visited = 0;
    private float[][] coordinates;
    private int[] Labels;


    public KDTree (int dimension) {
        Root = null;
        this.dimension = dimension;
    }

    //for knn
    public void createTree (float[][] points, int[] labels) {
        this.Labels = labels;
        for(int i = 0; i < points.length; i++)
            insert(points[i], labels[i]);
        coordinates = points;
    }

    //for knn
    public void insert(float[] point, int lbl) {
        TNode q = new TNode(point, lbl);
        if(Root == null)
            Root = q;
        else  {
            TNode p = Root;
            int d = 0;
            TNode t = null;
            int now = 0;
            while (p != null) {
                now = d % dimension;
                d++;
                t = p;
                if(p.point.getCoordinate()[now] > q.point.getCoordinate()[now])
                    p = p.left;
                else
                    p = p.right;
            }

            if(t.point.getCoordinate()[now] > q.point.getCoordinate()[now])
                t.left = q;
            else
                t.right = q;
        }
    }

    public void createTree (float[][] points) {
        for(int i = 0; i < points.length; i++)
            insert(points[i]);
        coordinates = points;
    }

    public void insert(float[] point) {
        TNode q = new TNode(point);
        if(Root == null)
            Root = q;
        else  {
            TNode p = Root;
            int d = 0;
            TNode t = null;
            int now = 0;
            while (p != null) {
                now = d % dimension;
                d++;
                t = p;
                if(p.point.getCoordinate()[now] > q.point.getCoordinate()[now])
                    p = p.left;
                else
                    p = p.right;
            }

            if(t.point.getCoordinate()[now] > q.point.getCoordinate()[now])
                t.left = q;
            else
                t.right = q;
        }
    }

    public void inOrderTraversal(TNode k) {
        TNode p = k;
        if(p != null) {
            inOrderTraversal(p.left);
            p.showPoint();
            inOrderTraversal(p.right);
        }
    }

    public float[] findNearest(float[] point) {
        if (Root == null)
            throw new IllegalStateException("Tree is empty!");
        best = null;
        visited = 0;
        bestDistance = 0;
        TNode q = new TNode(point);
        nearest(Root, q, 0);
        return best.getCoordinate();
    }

    private void nearest(TNode root, TNode target, int index) {
        if (root == null)
            return;
        ++visited;
        double d = root.distance(target.getCoordinate());
        if (best == null || d < bestDistance) {
            bestDistance = d;
            best = root;
        }
        if (bestDistance == 0)
            return;
        double dx = root.getCoordinate()[index] - target.getCoordinate()[index];
        index = (index + 1) % dimension;
        nearest(dx > 0 ? root.left : root.right, target, index);
        if (dx * dx >= bestDistance)
            return;
        nearest(dx > 0 ? root.right : root.left, target, index);
    }

    public boolean pointExists(float[] point) {
        TNode p = Root;
        Stack<TNode> stack = new Stack<>();
        do {
            while (p != null) {
                stack.push(p);
                p = p.left;
            }
            if(!stack.empty()){
                p = stack.pop();
                float distance = p.distance(point);
                if(distance == 0)
                    return true;
                p = p.right;
            }
        }while (!stack.empty() || p != null);
        return false;
    }

    public float[][] findMNearest(float[] point, int m) {
        TNode p = Root, resultNode = null;
        Stack<TNode> stack = new Stack<>();
        float minDistance = Float.MAX_VALUE;
        int founded = 0;
        float[][] points = new float[m][dimension];

        do {
            while (p != null) {
                stack.push(p);
                p = p.left;
            }
            if(!stack.empty()){
                p = stack.pop();

                if(founded < m) {
                    points[founded] = p.getCoordinate();
                    founded++;
                }else  {
                    points = Replace(points, p, point);
                }

                p = p.right;
            }
        }while (!stack.empty() || p != null);

        return points;
    }

    //for knn
    public int[] findMNearestLbl(float[] point, int m) {
        TNode p = Root, resultNode = null;
        Stack<TNode> stack = new Stack<>();
        float minDistance = Float.MAX_VALUE;
        int founded = 0;
        int[] lbls = new int[m];
        float[][] points = new float[m][768];
        do {
            while (p != null) {
                stack.push(p);
                p = p.left;
            }
            if(!stack.empty()){
                p = stack.pop();

                if(founded < m) {
                    points[founded] = p.getCoordinate();
                    lbls[founded] = p.point.label;
                    founded++;
                }else  {
                    int maxPos = -1;
                    float maxValue = -1;
                    for(int i = 0; i < points.length; i++) {
                        if(Distance(point, points[i]) > maxValue) {
                            maxPos = i;
                            maxValue = Distance(point, points[i]);
                        }
                    }

                    if(p.distance(point) < maxValue) {
                        points[maxPos] = p.getCoordinate();
                        lbls[maxPos] = p.point.label;
                    }

                }


                p = p.right;
            }
        }while (!stack.empty() || p != null);

        return lbls;
    }

    public float[][] searchRange(float[] lower_bounds, float[] upper_bounds) {
        float[][] points = new float[100][dimension];

        int count = 0;
        TNode p = Root, resultNode = null;
        Stack<TNode> stack = new Stack<>();
        float minDistance = Float.MAX_VALUE;
        do {
            while (p != null) {
                stack.push(p);
                p = p.left;
            }
            if(!stack.empty()){
                p = stack.pop();
                if(isBetween(p, lower_bounds, upper_bounds)) {
                    System.out.println("S");
                    points[count] = p.getCoordinate();
                    count++;
                }
                p = p.right;
            }
        }while (!stack.empty() || p != null);


        float[][] pointss = new float[count][dimension];
        for(int i = 0; i < count; i++)
            pointss[i] = points[i];
        return pointss;

    }

    private float[][] Replace(float[][] points, TNode p, float[] point) {

        int maxPos = -1;
        float maxValue = -1;
        for(int i = 0; i < points.length; i++) {
            if(Distance(point, points[i]) > maxValue) {
                maxPos = i;
                maxValue = Distance(point, points[i]);
            }
        }

        if(p.distance(point) < maxValue) {
           points[maxPos] = p.getCoordinate();
        }
        return points;
    }

    private float Distance(float[] a, float[] b ) {
        float sum = 0;
        for(int i = 0; i < a.length; i++)
            sum += (float) Math.pow(a[i] - b[i], 2);
        return (float) Math.sqrt(sum);
    }

    private boolean isBetween(TNode p, float[] lower_bounds, float[] upper_bounds) {
        for(int i = 0; i < lower_bounds.length; i++) {
            if(p.getCoordinate()[i] <= lower_bounds[i] || p.getCoordinate()[i] >= upper_bounds[i])
                return false;
        }
        return true;
    }

    public boolean deletePoint(float[] f) {
        TNode t = new TNode(f);
        if (dimension != t.getCoordinate().length || Root == null)
            return false;
        boolean isExist = this.pointExists(t.getCoordinate());
        if (!isExist)
            return false;
        Root = deletePoint(Root, t, 0);
        return true;
    }

    private TNode deletePoint(TNode current_Root, TNode t, int dim) {
        if (current_Root == null)
            return null;
        dim = dim % dimension;
        if (t.equals(current_Root)) {
            if (current_Root.right != null) {
                TNode min = findMin(current_Root.right, dim, dim + 1);
                current_Root.point = min.point;
                current_Root.right = deletePoint(current_Root.right, min, dim + 1);
            } else if (current_Root.left != null) {
                TNode min = findMin(current_Root.left, dim, dim + 1);
                current_Root.point = min.point;
                current_Root.right = deletePoint(current_Root.left, min, dim + 1);
                current_Root.left = null;
            } else {
                return null;
            }
            if (current_Root.left != null && current_Root.left.getCoordinate()[dim] >= current_Root.getCoordinate()[dim] ||
                    current_Root.right != null && current_Root.right.getCoordinate()[dim] < current_Root.getCoordinate()[dim])
                System.err.println("SOMETHING WENT WRONG");
            return current_Root;
        }
        if (t.getCoordinate()[dim] < current_Root.getCoordinate()[dim]) {
            current_Root.left = deletePoint(current_Root.left, t, dim + 1);
        } else {
            current_Root.right = deletePoint(current_Root.right, t, dim + 1);
        }
        return current_Root;
    }

    private TNode findMin(TNode root, int goalDim, int dim) {
        if (root == null)
            return null;
        if (goalDim == dim) {
            if (root.left == null)
                return root;
            return findMin(root.left, goalDim, (dim + 1) % dimension);
        }
        TNode rightMin = findMin(root.right, goalDim, (dim + 1) % dimension);
        TNode leftMin = findMin(root.left, goalDim, (dim + 1) % dimension);
        TNode result = root;
        if (rightMin != null && rightMin.getCoordinate()[goalDim] < result.getCoordinate()[goalDim])
            result = rightMin;
        if (leftMin != null && leftMin.getCoordinate()[goalDim] < result.getCoordinate()[goalDim])
            result = leftMin;
        return result;
    }

}



