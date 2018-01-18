package com.example.myapplication.service.core;

import android.content.Context;

import lombok.Builder;
import lombok.Value;

/**
 * See this at
 *
 * @author QuyDP
 */
@Value
@Builder
public class ApiConfig {
    private Context context;
    private String baseUrl;
    private String auth;
}
