package eu.europeana.service.ir.image.domain;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;

import org.apache.commons.codec.net.URLCodec;

public class Tools {

	public static byte[] inputStream2ByteArray(InputStream is)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		try {
			while ((i = is.read()) != -1) {
				baos.write(i);
			}
		} finally {
			if (baos != null)
				baos.close();
		}
		return baos.toByteArray();
	}

	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		// Get the size of the file
		long length = file.length();
		// You cannot create an array using a long type.
		// It needs to be an int type. // Before converting to an int type,
		// check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			// File is too large
		} // Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];
		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		try {
			while (offset < bytes.length
					&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}
			// Ensure all the bytes have been read in
			if (offset < bytes.length) {
				throw new IOException("Could not completely read file "
						+ file.getName());
			}
		} finally {
			// Close the input stream and return bytes
			if (is != null)
				is.close();
		}
		return bytes;
	}

	public static String[][] getAllOrderedProperties(File propertyFile)
			throws IOException {

		BufferedReader bufferedReader = null;
		ArrayList<String[]> temp = new ArrayList<String[]>();
		String[][] orderedProperties = null;
		try {

			// Construct the BufferedReader object
			bufferedReader = new BufferedReader(new FileReader(propertyFile));

			String line = null;

			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();
				if (!line.startsWith("#")) {
					int equalsIndex = line.indexOf("=");
					if (equalsIndex != -1) {
						String[] property = {
								line.substring(0, equalsIndex).trim(),
								line.substring(equalsIndex + 1).trim() };
						temp.add(property);
					}
				}
				// Process the data, here we just print it out
			}
			orderedProperties = new String[temp.size()][2];
			temp.toArray(orderedProperties);
		} finally {
			// Close the BufferedReader
			if (bufferedReader != null)
				bufferedReader.close();
		}
		return orderedProperties;
	}

	protected static final BitSet WWW_FORM_URL = new BitSet(256);
	// Static initializer for www_form_url
	static {
		// alpha characters
		for (int i = 'a'; i <= 'z'; i++) {
			WWW_FORM_URL.set(i);
		}
		for (int i = 'A'; i <= 'Z'; i++) {
			WWW_FORM_URL.set(i);
		}
		// numeric characters
		for (int i = '0'; i <= '9'; i++) {
			WWW_FORM_URL.set(i);
		}
		// special chars
		WWW_FORM_URL.set('-');
		WWW_FORM_URL.set('_');
		WWW_FORM_URL.set('.');
		WWW_FORM_URL.set('*');
		WWW_FORM_URL.set('/');
		WWW_FORM_URL.set(':');
	}

	public static String encodeUrl(String url) {
		String encodedUrl = null;
		if (url != null) {
			encodedUrl = new String(URLCodec.encodeUrl(WWW_FORM_URL,
					url.getBytes()));
		}
		return encodedUrl;
	}

}
