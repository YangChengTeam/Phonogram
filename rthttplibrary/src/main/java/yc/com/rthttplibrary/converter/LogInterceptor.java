package yc.com.rthttplibrary.converter;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.rthttplibrary.request.OKHttpUtil;
import yc.com.rthttplibrary.util.LogUtil;


/**
 * Created by suns  on 2020/7/18 10:47.
 */
public class LogInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();


        String method = request.method();
        String contentType = "text/html; charset=utf-8";

        LogUtil.msg("客户端请求url->" + request.url().toString());


        //重点部分----------针对post请求做处理-----------------------
        if ("POST".equals(method)) {//post请求需要拼接
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                Map<String, String> params = new HashMap<>();
                boolean isrsa = true;
                for (int i = 0; i < body.size(); i++) {
                    if (body.name(i).equals("isrsa")) {
                        isrsa = Boolean.parseBoolean(body.value(i));
                        continue;
                    }
                    params.put(body.name(i), body.value(i));
                }
                LogUtil.msg(" 客户端请求数据->" + new JSONObject(params).toString());
                byte[] data = OKHttpUtil.encodeParams(params, isrsa);

                RequestBody requestBody = RequestBody.create(HttpConfig.MEDIA_TYPE, data);
                request = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .post(requestBody)
                        .build();
            } else if (request.body() instanceof MultipartBody) {
//                Log.e(TAG, "intercept: MultipartBody");
                contentType = "application/json; charset=utf-8";
            }
        } else {//get请求直接打印url
            LogUtil.msg("request params==" + request.url() + "\n 参数==" + request.body().toString());
        }

        long t1 = System.nanoTime();


//        try {
            Response response = chain.proceed(request);
//                .newBuilder()
//                .header("Content-Type", contentType)
////              .addHeader("Content-Encoding", "gzip")
//               .build();
            long t2 = System.nanoTime();

//        LogUtil.msg("客户端请求头信息->" + String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
//                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            if (response.body() != null) {// 深坑！打印body后原ResponseBody会被清空，需要重新设置body
                ResponseBody body = ResponseBody.create(MediaType.parse(contentType), response.body().bytes());
                return response.newBuilder().body(body).build();
            } else {
                return response;
            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            try {
//                return new Response.Builder()
//                        .request(request)
//                        .protocol(Protocol.get(Protocol.HTTP_1_0.name()))
//                        .code(2)
//                        .message("未知错误").build();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//                return null;
//            }
//        }

    }
}
