package it.fileupload;

import org.apache.commons.fileupload.ProgressListener;
public class FileUploadListener implements ProgressListener{
	private volatile long bytesRead = 0L, contentLength = 0L, item = 0L;   
	private volatile boolean endForMax = false;
	public FileUploadListener() {
		super();
	}

	public void update(long aBytesRead, long aContentLength, int anItem) {
		bytesRead = aBytesRead;
		contentLength = aContentLength;
		item = anItem;
	}

	public long getBytesRead() {
		return bytesRead;
	}

	public long getContentLength() {
		return contentLength;
	}

	public long getItem() {
		return item;
	}

	/**
	 * @return the endForMax
	 */
	public boolean isEndForMax() {
		return endForMax;
	}

	/**
	 * @param endForMax the endForMax to set
	 */
	public void setEndForMax(boolean endForMax) {
		this.endForMax = endForMax;
	}
	
}
