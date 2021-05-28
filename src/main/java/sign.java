import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Size;
import org.opencv.features2d.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.Scanner;

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
            Mat src_img1 = Imgcodecs.imread("F:\\SIFT\\01.jpg");
            Mat img1 = new Mat();
            Mat outimg1=new Mat();
            Imgproc.resize(src_img1, img1, new Size(512, 512));
            Mat src_img2 = Imgcodecs.imread("F:\\SIFT\\02.jpg");
            Mat img2 = new Mat();
            Imgproc.resize(src_img2, img2, new Size(512, 512));
            Features2d fd=new Features2d();
            SIFT sift = SIFT.create();
            MatOfKeyPoint mkp1 =new MatOfKeyPoint();
            MatOfKeyPoint mkp2 =new MatOfKeyPoint();
            sift.detect(img1,mkp1);
            sift.detect(img2,mkp2);
            Mat descriptor1= new Mat();
            Mat descriptor2 = new Mat();
            sift.compute(img1, mkp1, descriptor1);
            sift.compute(img2, mkp2, descriptor2);
            //FlannBasedMatcher bm=new FlannBasedMatcher();
            //bm
            DescriptorMatcher matcher=DescriptorMatcher.create("BruteForce");
            Mat img_matches=new Mat();
            MatOfDMatch v=new MatOfDMatch();
            matcher.match(descriptor1, descriptor2,v);
            Features2d.drawMatches(img1, mkp1, img2, mkp2, v, img_matches);
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
