package lpsolve.ext;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Small abstraction from JNI
 */
public final class SystemX {

	private SystemX() {
	}

	public static void loadLibrary(String libName) {
		try {
			System.loadLibrary(libName);
		} catch (UnsatisfiedLinkError ule) {
			try {
				loadLibraryByClassLoader(libName,
						SystemX.class.getClassLoader());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static void loadLibraryByClassLoader(String libName,
			ClassLoader loader) throws IOException {
		File destFile = copyLibraryByClassLoader(libName, loader);
		String fileName = destFile.getAbsolutePath();
		System.load(fileName);
	}

	private static File copyLibraryByClassLoader(String libName,
			ClassLoader loader) throws IOException {
		File tempFile = File.createTempFile("testFile", ".tmp");
		tempFile.deleteOnExit();
		File tempDir = tempFile.getParentFile();
		String osName = System.getProperty("os.name");
		String osArch = System.getProperty("os.arch");
		String libPrefix = "";
		String libEnding = "";
		String subFolder = "";
		if (osName.startsWith("Windows")) {
			libEnding = ".dll";
			if (osArch.equalsIgnoreCase("amd64"))
				subFolder = "win64";
			else
				subFolder = "win32";
		}
		String libFileName = libPrefix + libName + libEnding;
		String path = String.format("native/%s/%s", subFolder, libFileName);
		File randomDir = new File(tempDir, (int) (Math.random() * 10) + "");
		randomDir.mkdir();
		File destFile = new File(randomDir, libFileName);
		destFile.deleteOnExit();
		writeResourceToFile(loader, path, destFile);
		return destFile;
	}

	private static void writeResourceToFile(ClassLoader loader, String path,
			File file) throws IOException {
		InputStream input = loader.getResourceAsStream(path);
		BufferedOutputStream output = new BufferedOutputStream(
				new FileOutputStream(file));
		byte[] buffer = new byte[1024];
		int bytes = 0;
		while ((bytes = input.read(buffer)) > 0)
			output.write(buffer, 0, bytes);
		input.close();
		output.flush();
		output.close();
	}
}