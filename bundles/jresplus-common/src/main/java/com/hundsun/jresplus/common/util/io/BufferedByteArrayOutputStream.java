/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hundsun.jresplus.common.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 胡凌 (Leo) sagahl hulingy@gmail.com  copy by fish
 */
public class BufferedByteArrayOutputStream extends OutputStream {

	/** A singleton empty byte array. */
	private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

	/** The list of buffers, which grows and never reduces. */
	private List<byte[]> buffers = new ArrayList<byte[]>();
	/** The index of the current buffer. */
	private int currentBufferIndex;
	/** The total count of bytes in all the filled buffers. */
	private int filledBufferSum;
	/** The current buffer. */
	private byte[] currentBuffer;
	/** The total count of bytes written. */
	private int count;

	/**
	 * Creates a new byte array output stream. The buffer capacity is initially
	 * 1024 bytes, though its size increases if necessary.
	 */
	public BufferedByteArrayOutputStream() {
		this(8192);
	}

	/**
	 * Creates a new byte array output stream, with a buffer capacity of the
	 * specified size, in bytes.
	 * 
	 * @param size
	 *            the initial size
	 * @throws IllegalArgumentException
	 *             if size is negative
	 */
	public BufferedByteArrayOutputStream(int size) {
		if (size < 0) {
			throw new IllegalArgumentException("Negative initial size: " + size);
		}
		needNewBuffer(size);
	}

	/**
	 * Return the appropriate <code>byte[]</code> buffer specified by index.
	 * 
	 * @param index
	 *            the index of the buffer required
	 * @return the buffer
	 */
	private byte[] getBuffer(int index) {
		return buffers.get(index);
	}

	/**
	 * Makes a new buffer available either by allocating a new one or re-cycling
	 * an existing one.
	 * 
	 * @param newcount
	 *            the size of the buffer if one is created
	 */
	private void needNewBuffer(int newcount) {
		if (currentBufferIndex < buffers.size() - 1) {
			// Recycling old buffer
			filledBufferSum += currentBuffer.length;

			currentBufferIndex++;
			currentBuffer = getBuffer(currentBufferIndex);
		} else {
			// Creating new buffer
			int newBufferSize;
			if (currentBuffer == null) {
				newBufferSize = newcount;
				filledBufferSum = 0;
			} else {
				newBufferSize = Math.max(currentBuffer.length << 1, newcount
						- filledBufferSum);
				filledBufferSum += currentBuffer.length;
			}

			currentBufferIndex++;
			currentBuffer = new byte[newBufferSize];
			buffers.add(currentBuffer);
		}
	}

	/**
	 * Write the bytes to byte array.
	 * 
	 * @param b
	 *            the bytes to write
	 * @param off
	 *            The start offset
	 * @param len
	 *            The number of bytes to write
	 */
	public void write(byte[] b, int off, int len) {
		if ((off < 0) || (off > b.length) || (len < 0)
				|| ((off + len) > b.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}

		int newcount = count + len;
		int remaining = len;
		int inBufferPos = count - filledBufferSum;
		while (remaining > 0) {
			int part = Math.min(remaining, currentBuffer.length - inBufferPos);
			System.arraycopy(b, off + len - remaining, currentBuffer,
					inBufferPos, part);
			remaining -= part;
			if (remaining > 0) {
				needNewBuffer(newcount);
				inBufferPos = 0;
			}
		}
		count = newcount;
	}

	/**
	 * Write a byte to byte array.
	 * 
	 * @param b
	 *            the byte to write
	 */
	public void write(int b) {
		int inBufferPos = count - filledBufferSum;
		if (inBufferPos == currentBuffer.length) {
			needNewBuffer(count + 1);
			inBufferPos = 0;
		}
		currentBuffer[inBufferPos] = (byte) b;
		count++;
	}

	/**
	 * Writes the entire contents of the specified input stream to this byte
	 * stream. Bytes from the input stream are read directly into the internal
	 * buffers of this streams.
	 * 
	 * @param in
	 *            the input stream to read from
	 * @return total number of bytes read from the input stream (and written to
	 *         this stream)
	 * @throws IOException
	 *             if an I/O error occurs while reading the input stream
	 * @since Commons IO 1.4
	 */
	public int write(InputStream in) throws IOException {
		int readCount = 0;
		int inBufferPos = count - filledBufferSum;
		int n = in.read(currentBuffer, inBufferPos, currentBuffer.length
				- inBufferPos);
		while (n != -1) {
			readCount += n;
			inBufferPos += n;
			count += n;
			if (inBufferPos == currentBuffer.length) {
				needNewBuffer(currentBuffer.length);
				inBufferPos = 0;
			}
			n = in.read(currentBuffer, inBufferPos, currentBuffer.length
					- inBufferPos);
		}
		return readCount;
	}

	/**
	 * Return the current size of the byte array.
	 * 
	 * @return the current size of the byte array
	 */
	public int size() {
		return count;
	}

	/**
	 * Closing a <tt>BufferedByteArrayOutputStream</tt> has no effect. The methods in
	 * this class can be called after the stream has been closed without
	 * generating an <tt>IOException</tt>.
	 * 
	 * @throws IOException
	 *             never (this method should not declare this exception but it
	 *             has to now due to backwards compatability)
	 */
	public void close() throws IOException {
		// nop
	}

	/**
	 * @see java.io.ByteArrayOutputStream#reset()
	 */
	public void reset() {
		count = 0;
		filledBufferSum = 0;
		currentBufferIndex = 0;
		currentBuffer = getBuffer(currentBufferIndex);
	}

	public void reset(int n) {
		count = 0;
		filledBufferSum = 0;
		currentBufferIndex = 0;
		currentBuffer = getBuffer(currentBufferIndex);
		curtailBuffer(n);
	}

	private void curtailBuffer(int n) {
		if (this.buffers.size() <= n) {
			return;
		}
		for (int i = this.buffers.size() - 1; i >= n; i--) {
			this.buffers.remove(i);
		}
	}

	/**
	 * Writes the entire contents of this byte stream to the specified output
	 * stream.
	 * 
	 * @param out
	 *            the output stream to write to
	 * @throws IOException
	 *             if an I/O error occurs, such as if the stream is closed
	 * @see java.io.ByteArrayOutputStream#writeTo(OutputStream)
	 */
	public void writeTo(OutputStream out) throws IOException {
		int remaining = count;
		for (int i = 0; i < buffers.size(); i++) {
			byte[] buf = getBuffer(i);
			int c = Math.min(buf.length, remaining);
			out.write(buf, 0, c);
			remaining -= c;
			if (remaining == 0) {
				break;
			}
		}
	}

	/**
	 * Gets the curent contents of this byte stream as a byte array. The result
	 * is independent of this stream.
	 * 
	 * @return the current contents of this output stream, as a byte array
	 * @see java.io.ByteArrayOutputStream#toByteArray()
	 */
	public byte[] toByteArray() {
		int remaining = count;
		if (remaining == 0) {
			return EMPTY_BYTE_ARRAY;
		}
		byte newbuf[] = new byte[remaining];
		int pos = 0;
		for (int i = 0; i < buffers.size(); i++) {
			byte[] buf = getBuffer(i);
			int c = Math.min(buf.length, remaining);
			System.arraycopy(buf, 0, newbuf, pos, c);
			pos += c;
			remaining -= c;
			if (remaining == 0) {
				break;
			}
		}
		return newbuf;
	}

	/**
	 * Gets the curent contents of this byte stream as a string.
	 * 
	 * @return the contents of the byte array as a String
	 * @see java.io.ByteArrayOutputStream#toString()
	 */
	public String toString() {
		return new String(toByteArray());
	}

	/**
	 * Gets the curent contents of this byte stream as a string using the
	 * specified encoding.
	 * 
	 * @param enc
	 *            the name of the character encoding
	 * @return the string converted from the byte array
	 * @throws UnsupportedEncodingException
	 *             if the encoding is not supported
	 * @see java.io.ByteArrayOutputStream#toString(String)
	 */
	public String toString(String enc) throws UnsupportedEncodingException {
		return new String(toByteArray(), enc);
	}

}
