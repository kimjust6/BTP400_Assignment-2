package QRCode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;




	
public class QRCodeGenerator {

	public static BitMatrix generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        
        
        Path path = FileSystems.getDefault().getPath(filePath);
        
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        return bitMatrix;
       
    }
	
	public static String generateQRCodeImage(String barcodeText, int height, int width) throws Exception {
	    QRCodeWriter barcodeWriter = new QRCodeWriter();
	    BitMatrix bitMatrix = 
	      barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, height, width);
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    MatrixToImageWriter.writeToStream(bitMatrix, "png", byteArrayOutputStream);
	    return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
	}
	    
	

	
	/**
	 * @param args
	 */
	public static void main(String args[])
	{
//		System.out.println("test");
//		try {
//			generateQRCodeImage("Nice",250,250,"D:\\imagse.png");
//		} catch (WriterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}


}
