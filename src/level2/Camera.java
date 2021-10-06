package level2;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class Camera extends JFrame {
//	VideoCapture videoCapture = new VideoCapture(0);
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
	ImageIcon icon;
	Mat glass;
	ImageIcon glassImage;
	int xGlass = 0, yGlass = 0;
	int sizeFace;

	public Camera() {
		setLayout(new BorderLayout());
		glassImage = new ImageIcon("glass2.png");

//		add(frame = new JLabel());
		add(new Draw());

//		add(bt = new JButton("s"));
//		bt.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				scale += 0.1;
//				System.out.println(scale);
//				starCamera();
//
//			}
//		});

		setSize(500, 500);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		starCamera();
//		Timer t = new Timer(1000 / 60, new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				starCamera();
//			}
//		});
//		t.start();
	}

	class Draw extends JPanel {
		public Draw() {
			setOpaque(false);
		}

		@Override
		public void paint(Graphics g) {
			// TODO Auto-generated method stub
			super.paint(g);

			g.drawImage(icon.getImage(), 0, 0, null);
			g.drawImage(glassImage.getImage(), xGlass, yGlass, null);
		}
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

		image = Imgcodecs.imread("cryjpeg.jpeg");
		Mat glass = Imgcodecs.imread("glass2.png");
//		videoCapture.read(image);
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
			int size = face.width / glass.width();
			xGlass = face.x;
			yGlass = (int) (face.y + face.height * 0.3);
			sizeFace = face.width;
//			Imgproc.resize(glass, glass, new Size(face.width, 30));

		}
		eyeDetect.detectMultiScale(grayFrame, eyes, 1.3, 5);
		System.out.println(String.format("Detected %s eyes", eyes.toArray().length));
		for (Rect eye : eyes.toArray()) {
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
//		Imgproc.filter2D(image, glass, 1, new Mat());

		Imgcodecs.imencode(".jpg", image, buf);
		MatOfByte byteGlass = new MatOfByte();
		Imgcodecs.imencode(".jpg", glass, byteGlass);
		imageData = buf.toArray();
		icon = new ImageIcon(imageData);
//		byte[] glassDate = byteGlass.toArray();
//		glassImage = new ImageIcon(glassDate);
		repaint();
	}

}
