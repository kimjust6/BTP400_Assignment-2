package QRCode;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;




	
public class QRCodeGenerator {

	public static BitMatrix generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        
        File file = new File(filePath);
        Path path = FileSystems.getDefault().getPath(filePath);
        
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        return bitMatrix;
       
    }
	
	/**
	 * @param args
	 */
	public static void main(String args[])
	{
		System.out.println("test");
		try {
			generateQRCodeImage("991A38BED0D022D6622E9AD47513E2A14AC0DA58F15D8AFC81075DEC11CAF29D",250,250,"D:\\imagse.png");
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
