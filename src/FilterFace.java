import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class FilterFace {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new Camera();
	}
}
