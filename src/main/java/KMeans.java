import java.util.List;

public class KMeans  extends KMeansClustering{
    @Override
    public double similarScore(Object o1, Object o2) {
        return Math.sqrt(((Float)o1-(Float) o2)*((Float)o1-(Float) o2))*-1.0;
       /// return 0;
    }

    @Override
    public boolean equals(Object o1, Object o2) {

        return ((Float)o1).equals((Float)o2);
    }

    @Override
    public Object getCenterT(List list) {
        Float sum[]={0.0f};
        list.forEach((v)->{sum[0]+=(Float) v;});

        return sum[0]/list.size();
    }
}
