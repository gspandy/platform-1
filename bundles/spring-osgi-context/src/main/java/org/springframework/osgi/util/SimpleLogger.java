/*
 * Copyright 2006-2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.osgi.util;

import org.apache.commons.logging.Log;

/**
 * Simple Logger implementation used as fall back to degrade gracefully in case
 * the LogFactory implementation is not configured properly.
 * 
 * @author Costin Leau
 * 
 */
class SimpleLogger implements Log {

	public void debug(Object message) {
		System.out.println(message);
	}

	public void debug(Object message, Throwable th) {
		System.out.println(message);
		th.printStackTrace(System.out);
	}

	public void error(Object message) {
		System.err.println(message);
	}

	public void error(Object message, Throwable th) {
		System.err.println(message);
		th.printStackTrace(System.err);
	}

	public void fatal(Object message) {
		System.err.println(message);
	}

	public void fatal(Object message, Throwable th) {
		System.err.println(message);
		th.printStackTrace(System.err);
	}

	public int getLogLevel() {
		return 0;
	}

	public void info(Object message) {
		System.out.println(message);
	}

	public void info(Object message, Throwable th) {
		System.out.println(message);
		th.printStackTrace(System.out);
	}

	public boolean isDebugEnabled() {
		return true;
	}

	public boolean isErrorEnabled() {
		return true;
	}

	public boolean isFatalEnabled() {
		return true;
	}

	public boolean isInfoEnabled() {
		return true;
	}

	public boolean isTraceEnabled() {
		return true;
	}

	public boolean isWarnEnabled() {
		return true;
	}

	public void trace(Object message) {
		System.out.println(message);
	}

	public void trace(Object message, Throwable th) {
		System.out.println(message);
		th.printStackTrace(System.out);
	}

	public void warn(Object message) {
		System.out.println(message);
	}

	public void warn(Object message, Throwable th) {
		System.out.println(message);
		th.printStackTrace(System.out);
	}
}
