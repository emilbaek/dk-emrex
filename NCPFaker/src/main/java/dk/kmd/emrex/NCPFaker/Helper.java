package dk.kmd.emrex.NCPFaker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Helper {
	
	public static byte[] uncompressGzip(byte[] in) throws IOException {
		ByteArrayInputStream bytein = new ByteArrayInputStream(in);
		GZIPInputStream gzin = new GZIPInputStream(bytein);
		ByteArrayOutputStream byteout = new ByteArrayOutputStream();
		int res = 0;
		byte buf[] = new byte[1024];
		while (res >= 0) {
		    res = gzin.read(buf, 0, buf.length);
		    if (res > 0) {
		        byteout.write(buf, 0, res);
		    }
		}
		byte uncompressed[] = byteout.toByteArray();
		return uncompressed; 
	}
	
	public static byte[] decode64(String encoded){
		byte[] decoded = Base64.getDecoder().decode(encoded); 
		return decoded; 
	}
	
	public static byte[] decode64(byte[] encoded){
		byte[] decoded = Base64.getDecoder().decode(encoded);
		return decoded;
	}
	public static byte[] getPdfFromElmoXml(byte[] elmoXml){
		Pattern pattern = Pattern.compile("content[^>]*>.*base64,([^<]*)</content>"); 
		Matcher matcher = pattern.matcher((new String(elmoXml)).replace("\n","").replace("\r","")); 
		boolean found = matcher.find(); 
		if (found){
			byte[] decodedPDF = Base64.getDecoder().decode(matcher.group(1));
			return decodedPDF; 
		}else{
			log.error("Unable to get PDF from elmo XML");
			return null;
		}
	}
	
	public static void writePdftoDisk(byte[] pdfBytes){
		try(FileOutputStream fos = new FileOutputStream("out.pdf",false)){
			fos.write(pdfBytes);
			fos.close();
		} catch (IOException e) {
			log.error("Unable to write PDF to disk", e);
		}
	}
	
	public static byte[] readPdfFromDisk(){
		try {
			byte[] fileContent = Files.readAllBytes(Paths.get("out.pdf"));
			return fileContent; 
		} catch (IOException e) {
			log.error("Unable to read PDF to disk", e);
			return null;
		} 
	}
}
