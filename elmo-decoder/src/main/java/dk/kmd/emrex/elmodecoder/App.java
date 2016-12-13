package dk.kmd.emrex.elmodecoder;

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

public class App {
	public static void main(String[] args) {
		if (args.length < 1) {
			commandUsage();
		}
		try {
			String fileContent = Files.lines(Paths.get(args[0])).reduce((a, b) -> a + b).orElse("");
			byte[] decodedElmo = Base64.getDecoder().decode(fileContent);
			decodedElmo = uncompressGzip(decodedElmo);
			Pattern pattern = Pattern.compile("content[^>]*>.*base64,([^<]*)</content>");
			Matcher matcher = pattern.matcher((new String(decodedElmo)).replace("\n", "").replace("\r", ""));
			boolean found = matcher.find();
			if (found) {
				byte[] decodedPDF = Base64.getDecoder().decode(matcher.group(1));
				try (FileOutputStream fos = new FileOutputStream("out.pdf")) {
					fos.write(decodedPDF);
					fos.close();
					System.out.println("Output written to file out.pdf");
				} catch (IOException ioe) {
					System.out.println("Unable to write to the file out.pdf");
				}
			} else {
				System.out.println("Unable to find pattern " + pattern.pattern() + " in base64 encoded content");
			}
		} catch (IOException e) {
			System.out.println("An error happened");
			commandUsage();
		}
	}

	private static byte[] uncompressGzip(byte[] in) throws IOException {
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

	private static void commandUsage() {
		System.out.println("Command usage java -jar elmo-decoder.jar <filename>");
		System.out.println("File is suppposed to be a base64 encoded text file");
		System.exit(0);
	}
}
