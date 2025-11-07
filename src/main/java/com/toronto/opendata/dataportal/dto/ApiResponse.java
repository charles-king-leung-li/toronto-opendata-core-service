package com.toronto.opendata.dataportal.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class ApiResponse<T> {
    private T data;
    private Metadata metadata;
    private String message;
    private String status;

    @Data
    @Builder
    public static class Metadata {
        private int totalCount;
        private int pageSize;
        private int pageNumber;
        private int totalPages;
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .data(data)
                .status("success")
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .data(data)
                .message(message)
                .status("success")
                .build();
    }

    public static <T> ApiResponse<T> success(T data, Metadata metadata) {
        return ApiResponse.<T>builder()
                .data(data)
                .metadata(metadata)
                .status("success")
                .build();
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .message(message)
                .status("error")
                .build();
    }
}