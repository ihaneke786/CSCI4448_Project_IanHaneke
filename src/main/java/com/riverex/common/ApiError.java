package com.riverex.common;

import java.time.Instant;
import java.util.List;

public record ApiError(
		int status,
		String error,
		String message,
		Instant timestamp,
		List<String> details
) {

	public static ApiError of(int status, String error, String message, List<String> details) {
		return new ApiError(status, error, message, Instant.now(), details);
	}

}
