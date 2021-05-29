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
            Mat src_img2 = Imgcodecs.imread("F:\\SIFT\\07.jpg");
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
            FlannBasedMatcher matcher=new FlannBasedMatcher();
            Mat img_matches=new Mat();
            MatOfDMatch matrix=new MatOfDMatch();
            MatOfDMatch goodmatrix=new MatOfDMatch();
            matcher.match(descriptor1, descriptor2,matrix);
            //Features2d.drawMatches(img1, mkp1, img2, mkp2, matrix, img_matches);
            double min[]= {(matrix.toList().get(0)).distance};
            matrix.toList().forEach((v)->{
                System.out.println(v.distance);
                if(min[0]>v.distance) min[0]=v.distance;
                //sum[0]=sum[0]+v.distance;
            });
            //System.out.println("------------------");
            //System.out.println("AVG:"+sum[0]/matrix.toList().size());
            List<DMatch> ldm=new ArrayList<DMatch>();
            matrix.toList().forEach((v)->{
            if(v.distance<(3*min[0]<0.02?0.02:3*min[0]))ldm.add(v);
            });
            System.out.println(String.valueOf(min[0])+","+ldm.size());
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
