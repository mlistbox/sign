import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class sign {
    public static double Variance(double[] x) {
        int m=x.length;
        double sum=0;
        for(int i=0;i<m;i++){//求和
            sum+=x[i];
        }
        double dAve=sum/m;//求平均值
        double dVar=0;
        for(int i=0;i<m;i++){//求方差
            dVar+=(x[i]-dAve)*(x[i]-dAve);
        }
        return dVar/m;
    }

    //标准差σ=sqrt(s^2)
    public static double StandardDiviation(List<Float> x) {
        int m=x.size();
        double sum=0;
        for(int i=0;i<m;i++){//求和
            sum+=x.get(i);
        }
        double dAve=sum/m;//求平均值
        double dVar=0;
        for(int i=0;i<m;i++){//求方差
            dVar+=(x.get(i)-dAve)*(x.get(i)-dAve);
        }
        //reture Math.sqrt(dVar/(m-1));
        return Math.sqrt(dVar/m);
    }
    static
    {

        System.load("G:\\opencv\\opencv\\build\\java\\x64\\opencv_java452.dll");
        //System.loadLibrary("opencv_java452");
    }


    public static void main(String[] args)
    {
        try {
            //URL url = ClassLoader.getSystemResource("G:\\opencv\\opencv\\build\\java\\x64\\opencv_java452.dll");
            //System.load(url.getPath());
            Mat src_img1 = Imgcodecs.imread("F:\\SIFT\\05.jpg");
            Mat img1 = new Mat();
            Mat outimg1=new Mat();
            Imgproc.resize(src_img1, img1, new Size(512, 512));
            Mat src_img2 = Imgcodecs.imread("F:\\SIFT\\06.jpg");
            Mat img2 = new Mat();
            Imgproc.resize(src_img2, img2, new Size(512, 512));
            Features2d fd=new Features2d();
            SIFT sift = SIFT.create();
            MatOfKeyPoint mkp1 =new MatOfKeyPoint();
            MatOfKeyPoint mkp2 =new MatOfKeyPoint();
            //sift.detect(img1,mkp1);
            //sift.detect(img2,mkp2);
            Mat descriptor1= new Mat();
            Mat descriptor2 = new Mat();
            //sift.compute(img1, mkp1, descriptor1);
            //sift.compute(img2, mkp2, descriptor2);
            sift.detectAndCompute(img1,new Mat(),mkp1,descriptor1);
            sift.detectAndCompute(img2,new Mat(),mkp2,descriptor2);
            //FlannBasedMatcher bm=new FlannBasedMatcher();
            //bm
            //DescriptorMatcher matcher=DescriptorMatcher.create("BruteForce");
            //FlannBasedMatcher matcher=new FlannBasedMatcher();
            BFMatcher matcher=new BFMatcher();
            Mat img_matches=new Mat();
            MatOfDMatch matrix=new MatOfDMatch();
            MatOfDMatch goodmatrix=new MatOfDMatch();
            matcher.match(descriptor1, descriptor2,matrix);
            //Features2d.drawMatches(img1, mkp1, img2, mkp2, matrix, img_matches);
            List<Float> dis=new ArrayList<Float>();
            double min[]= {(matrix.toList().get(0)).distance,0};
            KMeans km= new KMeans();

            matrix.toList().forEach((v)->{
                System.out.println(v.distance);
                if(min[0]>v.distance) min[0]=v.distance;
                min[1]=min[1]+v.distance;
                dis.add(Float.valueOf(v.distance));
                km.addRecord(v.distance);
                //sum[0]=sum[0]+v.distance;
            });
            //System.out.println("------------------");
            km.setK(10);
            List<List<Float>> cresult = km.clustering();
            List<Float> center = km.getClusteringCenterT();
            //int i[]={0};
            //System.out.println("================================");
            //center.forEach((v1)->{System.out.println("v1:"+v1+";cresult:"+cresult.get(i[0]).size()+"k1:"+center.);});
            List<Float> center1=center.stream().sorted().collect(Collectors.toList());

            System.out.println("================================"+center.toString());
            Double deviation=StandardDiviation(dis);
            System.out.println("AVG:"+min[1]/matrix.toList().size()+"deviation:"+deviation);
            System.out.println("++++++++++++++++++++++++++++++++++++++");
            min[1]=min[1]/matrix.toList().size();
            List<DMatch> ldm=new ArrayList<DMatch>();
            for(int j=0;j<3;j++)
            {
                List<Float> f = cresult.get(center.indexOf(center1.get(j)));
                f.forEach((k) -> {
                    ;
                    matrix.toList().forEach((v) -> {
                        //if(v.distance<(1.5*min[0]<0.02?0.02:1.5*min[0]))ldm.add(v);
                        if (v.distance == k) ldm.add(v);
                        // if(v.distance<50)ldm.add(v);
                    });
                    System.out.println(k);
                });
                System.out.println("j="+j+";"+center1.get(j));
            }

            System.out.println(String.valueOf(min[0])+","+ldm.size()+";"+(double)ldm.size()/matrix.toList().size());
            goodmatrix.fromList(ldm);
            Features2d.drawMatches(img1, mkp1, img2, mkp2, goodmatrix, img_matches);
            //fd.drawKeypoints(img1,mkp1,outimg1);
            HighGui.imshow("image after", img_matches);
            HighGui.waitKey(0);
            //System.out.println("+++++++++++++++++");
            //Scanner userInput = new Scanner(System.in);
            //String input = userInput.nextLine();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
