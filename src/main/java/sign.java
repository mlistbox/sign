import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.CvType;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.opencv.core.Core.BORDER_DEFAULT;

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
            Mat src_img1 = Imgcodecs.imread("F:\\SIFT\\01.jpg",Imgcodecs.IMREAD_GRAYSCALE);
            Mat img1 = new Mat();
            Mat outimg1=new Mat();
            Mat outtimg1=new Mat();
            Mat descriptor1= new Mat();
            Mat descriptor2 = new Mat();
            Imgproc.resize(src_img1, img1, new Size(640, 426));
            //Imgproc.cvtColor(img1,outimg1,Imgproc.COLOR_BayerGB2GRAY);
            //Imgproc.threshold(outimg1,outtimg1,150,255,Imgproc.THRESH_BINARY);

            //Imgproc.cornerHarris(outtimg1,descriptor1, 2, 3, 0.04, BORDER_DEFAULT);
            //Core.normalize(descriptor1,descriptor1,1.0,0.0,Core.NORM_MINMAX);
            //descriptor1.convertTo(descriptor2,CvType.CV_8UC1,255,0);
            //Imgproc.threshold(descriptor2,outimg1,50,255,Imgproc.THRESH_BINARY);
            outimg1=canny(img1);
            //HighGui.imshow("image after", outimg1);
            //HighGui.waitKey(0);

            Mat src_img2 = Imgcodecs.imread("F:\\SIFT\\02.jpg",Imgcodecs.IMREAD_GRAYSCALE);
            Mat img2 = new Mat();
            Mat outimg2=new Mat();
            Mat outtimg2=new Mat();
            Imgproc.resize(src_img2, img2, new Size(640, 426));
            //Imgproc.cvtColor(img2,outimg2,Imgproc.COLOR_BayerGB2GRAY);
            //Imgproc.threshold(outimg2,outtimg2,170,255,Imgproc.THRESH_BINARY);
            outimg2=canny(img2);
            Features2d fd=new Features2d();
            //SIFT sift = SIFT.create(0,10,0.1,6,1);
            SIFT sift = SIFT.create();
            MatOfKeyPoint mkp1 =new MatOfKeyPoint();
            MatOfKeyPoint mkp2 =new MatOfKeyPoint();
            //sift.detect(img1,mkp1);
            //sift.detect(img2,mkp2);

            //sift.compute(img1, mkp1, descriptor1);
            //sift.compute(img2, mkp2, descriptor2);
            sift.detectAndCompute(outimg1,new Mat(),mkp1,descriptor1);
            sift.detectAndCompute(outimg2,new Mat(),mkp2,descriptor2);
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
            double min[]= {(matrix.toList().get(0)).distance};
            KMeans km= new KMeans();

            matrix.toList().forEach((v)->{
                System.out.println(v.distance);
                if(min[0]>v.distance) min[0]=v.distance;
                //min[1]=min[1]+v.distance;
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
            cresult.forEach((v2)->{System.out.println(v2.size());});
            System.out.println("================================"+center.toString());
            Double deviation=StandardDiviation(dis);
            //System.out.println("AVG:"+min[1]/matrix.toList().size()+"deviation:"+deviation);

            //min[1]=min[1]/matrix.toList().size();
            List<DMatch> ldm=new ArrayList<DMatch>();
            /*
            for(int j=0;j<1;j++)
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
                System.out.println("j="+j+";"+(double)f.size()/matrix.toList().size()+";"+center1.get(j));
            }
            */

            matrix.toList().forEach((v) -> {
                if(min[0]*3 >=v.distance) ldm.add(v);
                    });
            System.out.println("++++++++++++++++++++++++++++++++++++++");
            System.out.println(String.valueOf(min[0])+","+ldm.size()+";"+(double)ldm.size()/matrix.toList().size()+";"+matrix.toList().size());
            goodmatrix.fromList(ldm);
            Features2d.drawMatches(outimg1, mkp1, outimg2, mkp2, goodmatrix, img_matches);
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
    static Mat canny(Mat src)
    {
        try {
            Mat outimg = new Mat();
            Mat dis = new Mat();
            Imgproc.cvtColor(src, outimg, Imgproc.COLOR_BayerGB2GRAY);
            Imgproc.threshold(outimg, dis, 150, 255, Imgproc.THRESH_BINARY);
            Imgproc.Canny(dis,dis,150, 100,3 );
            return dis;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
