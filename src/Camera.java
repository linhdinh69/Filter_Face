import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class Camera extends JFrame {
	VideoCapture videoCapture = new VideoCapture(0);
	Mat image = new Mat();
	JLabel frame;
	String xml = "haarcascade_frontalface_alt.xml";
	String xmlEye = "haarcascade_eye.xml";
	String xmlNose = "haarcascade_mcs_nose.xml";
	String xmlMouth = "haarcascade_mcs_mouth.xml";
	double scale = 1.1;
	JButton bt = new JButton();
	CascadeClassifier faceDetect = new CascadeClassifier(xml);
	CascadeClassifier eyeDetect = new CascadeClassifier(xmlEye);
	CascadeClassifier noseDetect = new CascadeClassifier(xmlNose);
	CascadeClassifier mouthDetect = new CascadeClassifier(xmlMouth);

	public Camera() {
		setLayout(new FlowLayout());

		add(frame = new JLabel());
		add(bt = new JButton("s"));
		bt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				scale += 0.1;
				System.out.println(scale);
				starCamera();

			}
		});

		setSize(500, 500);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Timer t = new Timer(1000 / 60, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				starCamera();
			}
		});
		t.start();
	}

	boolean checkEyeInFace(Rect eye, MatOfRect faces) {
		for (Rect face : faces.toArray()) {
			if (eye.x >= face.x && eye.x <= face.x + face.width && eye.y >= face.y && eye.y <= face.y + face.height)
				return true;
		}
		return false;
	}

	void starCamera() {
		byte[] imageData;

//		image = Imgcodecs.imread("steve2jpg.jpg");
		videoCapture.read(image);
		MatOfByte buf = new MatOfByte();
		MatOfRect faces = new MatOfRect();
		MatOfRect eyes = new MatOfRect();
		MatOfRect noses = new MatOfRect();
		MatOfRect mouths = new MatOfRect();

		Mat grayFrame = new Mat();

		Imgproc.cvtColor(image, grayFrame, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(grayFrame, grayFrame);

		// Detection Face
		faceDetect.detectMultiScale(grayFrame, faces, 1.3, 5);
		System.out.println(String.format("Detected %s faces", faces.toArray().length));
		for (Rect face : faces.toArray()) {
			Imgproc.rectangle(image, face, new Scalar(0, 0, 255));

		}
		eyeDetect.detectMultiScale(grayFrame, eyes, 1.3, 5);
		System.out.println(String.format("Detected %s eyes", eyes.toArray().length));
		for (Rect eye : eyes.toArray()) {
//			eye.x += face.x;
//			eye.y += face.y;
			Imgproc.rectangle(image, eye, new Scalar(255, 0, 0));
		}
		// Detection Eye

//		 Detection Nose
		noseDetect.detectMultiScale(grayFrame, noses, scale, 8);
		System.out.println(String.format("Detected %s noses", noses.toArray().length));
		for (Rect nose : noses.toArray()) {
			Imgproc.rectangle(image, nose, new Scalar(0, 255, 0));

		}
		// Detection Mouth
		mouthDetect.detectMultiScale(grayFrame, mouths, 1.7, 8);
		System.out.println(String.format("Detected %s Monuth", mouths.toArray().length));
		for (Rect mouth : mouths.toArray())
			Imgproc.rectangle(image, mouth, new Scalar(255, 250, 255));

		System.out.println("------------------------------------------------------");

		Imgcodecs.imencode(".jpg", image, buf);
		imageData = buf.toArray();
		ImageIcon icon = new ImageIcon(imageData);
		frame.setIcon(icon);

	}

}
