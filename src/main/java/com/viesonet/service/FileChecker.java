package com.viesonet.service;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Service
public class FileChecker {

	public boolean getFileType(String filePath) {
		try {
			File file = new File(filePath);
			String mimeType = Arrays.asList(ImageIO.getReaderMIMETypes()).stream().filter(type -> {
				try {
					return ImageIO.createImageInputStream(file) != null;
				} catch (IOException e) {
					return false;
				}
			}).findFirst().orElse(null);

			if (mimeType != null) {
				if (mimeType.startsWith("image")) {
					return true;
				} else if (mimeType.startsWith("video")) {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
