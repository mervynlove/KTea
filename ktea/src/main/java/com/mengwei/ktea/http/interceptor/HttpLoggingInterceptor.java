
package com.mengwei.ktea.http.interceptor;

import android.net.Uri;
import okhttp3.*;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSource;
import org.json.JSONArray;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import static okhttp3.internal.platform.Platform.INFO;

/**
 * meng wei on 2017/4/11 17:11
 */
public final class HttpLoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public interface Logger {

        void log(String message);

        Logger DEFAULT = new Logger() {
            @Override
            public void log(String message) {
                Platform.get().log(INFO, message, null);
            }
        };
    }

    public HttpLoggingInterceptor() {
        this.logger = Logger.DEFAULT;
    }

    private final Logger logger;

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        String requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol;
        logger.log("============================================================");
        logger.log(requestStartMessage);
        Headers headers = request.headers();
        if (hasRequestBody) {
            if (requestBody.contentType() != null) {
                logger.log("Content-Type: " + requestBody.contentType());
            }

            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    logger.log(name + ": " + headers.value(i));
                }
            }
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            if (isPlaintext(buffer)) {
                //打印请求参数
                logger.log(Uri.decode(buffer.readString(charset)));
            }
        } else {
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    logger.log(name + ": " + headers.value(i));
                }
            }
        }
        logger.log("============================================================");
        logger.log(" ");
        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
            logger.log("============================================================");
        } catch (Exception e) {
            logger.log("============================================================");
            logger.log("<-- error: " + e);
            logger.log("============================================================");
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "未知长度";
        logger.log("<-- " + response.code() + ' ' + response.message() + ' '
                + response.request().url() + " (" + tookMs + "ms" + ", "
                + bodySize + ')');
        if (!HttpHeaders.hasBody(response)) {
            logger.log("<-- 结束");
        } else if (bodyEncoded(response.headers())) {
            logger.log("<-- 结束");
        } else {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    logger.log("error");
                    logger.log("<-- 结束");
                    return response;
                }
            }
            if (!isPlaintext(buffer)) {
                logger.log("<-- 结束");
                return response;
            }
            if (contentLength != 0) {
                String result = buffer.clone().readString(charset);
                String temp = result;
                try {
                    if (result.startsWith("{") && result.endsWith("}")) {
                        org.json.JSONObject jsonObject = new org.json.JSONObject(result);
                        result = jsonObject.toString();
                    } else if (result.startsWith("[") && result.endsWith("]")) {
                        JSONArray jsonArray = new JSONArray(result);
                        result = jsonArray.toString();
                    }
                } catch (Exception e) {
                    result = temp;
                }
                //打印请求结果
                logger.log(result);
            }
            logger.log("<-- 结束");
        }
        logger.log("============================================================");
        return response;
    }

    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false;
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}
