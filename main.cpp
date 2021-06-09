#include "opencv2/opencv.hpp"
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2\imgproc\imgproc.hpp>
#include <iostream>
#include <opencv2/core/types.hpp>
#include <opencv2/features2d.hpp>
#include<math.h>

using namespace cv;
 static double StandardDiviation(std::vector< DMatch > x) {
	int m = x.size();
	double sum = 0;
	for (int i = 0; i < m; i++) {//求和
		sum += x[i].distance;
	}
	double dAve = sum / m;//求平均值
	double dVar = 0;
	for (int i = 0; i < m; i++) {//求方差
		dVar += (x[i].distance - dAve) * (x[i].distance - dAve);
	}
	//reture Math.sqrt(dVar/(m-1));
	return sqrt(dVar / m);
}

int main(int argc, char** argv) {
	try {
		Mat src_img1 = imread("F:\\SIFT\\06.jpg", cv::IMREAD_GRAYSCALE);
		Mat src_img2 = imread("F:\\SIFT\\05.jpg", cv::IMREAD_GRAYSCALE);
		Mat out1, out2, outimg1, outimg2;
		

		cv::Size sz;
		sz.width = 1024;
		sz.height= sz.width /src_img1.cols * src_img1.rows;
		Mat img1(sz, src_img1.type());
		resize(src_img1, img1, img1.size(), 0, 0, INTER_LINEAR);
		sz.height = sz.width / src_img2.cols * src_img2.rows;
		Mat img2(sz, src_img2.type());
		resize(src_img2, img2, img2.size(), 0, 0, INTER_LINEAR);

		cv::threshold(img1, out1, 150, 255, cv::THRESH_BINARY);
		cv::threshold(img2, out2, 150, 255, cv::THRESH_BINARY);

		cv::Canny(out1, outimg1, 1, 2, 3, false);
		cv::Canny(out2, outimg2, 1, 2, 3, false);

		//imshow("test_opencv_srtup", outimg2);
		//waitKey(0);
		
		cv::Ptr<SiftFeatureDetector> siftdtc = SiftFeatureDetector::create(0, 4, 0.1, 6, 1);
		std::vector<cv::KeyPoint> kp1, kp2;
		Mat  descriptor1, descriptor2;
		siftdtc->detect(outimg1, kp1);
		//cv::Ptr<SiftDescriptorExtractor> extractor = SiftDescriptorExtractor::create();
		siftdtc->compute(outimg1, kp1, descriptor1);
		siftdtc->detect(outimg2, kp2);
		//cv::Ptr<SiftDescriptorExtractor> extractor = SiftDescriptorExtractor::create();
		siftdtc->compute(outimg2, kp2, descriptor2);
		//cv::Ptr<cv::DescriptorMatcher> matcher = cv::DescriptorMatcher::create("BruteForce");
		cv::Ptr<cv::DescriptorMatcher> matcher = cv::DescriptorMatcher::create(cv::DescriptorMatcher::MatcherType::FLANNBASED);
		std::vector< DMatch > matches;
		std::vector< DMatch > goodmatches;
		
		Mat img_matches;
		matcher->match(descriptor1, descriptor2, matches);
		int i = 0;
		float d = matches[0].distance;
		double avg = 0.0f;
		//double sd=StandardDiviation(matches);
		for ( i = 0; i < matches.size(); i++)
		{
			if(d>matches[i].distance) d= matches[i].distance;
			
		}
		
		for (i = 0; i < matches.size(); i++)
		{
			if (0.0f == matches[i].distance)
			{
				goodmatches.push_back(matches[i]);
				avg += matches[i].distance;
			}
		}
		avg = avg / goodmatches.size();
		double sd = StandardDiviation(goodmatches);
		printf("avg:%f;sd:%f;size:%d;totally conform:%f" , avg, sd, goodmatches.size(), (double)goodmatches.size()/ matches.size());
		cv::drawMatches(outimg1, kp1, outimg2, kp2, goodmatches, img_matches);
		//namedWindow("test_opencv_setup", 0);

		imshow("test_opencv_srtup", img_matches);

		waitKey(0);
		//delete(descriptor1);
		//	descriptor1 = NULL;

		return 0;
	}
	catch (Exception e)
	{
		printf(e.what());
	}

}
