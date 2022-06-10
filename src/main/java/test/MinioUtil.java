//package test;
//
//import io.minio.*;
//import io.minio.http.Method;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//@Slf4j
//@Component
//public class MinioUtil {
//    @Value("${custom.minio.url}")
//    private String minioUrl;
//
//    @Value("${custom.minio.accessKey}")
//    private String accessKey;
//
//    @Value("${custom.minio.secretKey}")
//    private String secretKey;
//
//    @Value("${custom.minio.bucketName}")
//    private String bucketName;
//
//    private MinioClient minioClient;
//
//    /**
//     * 若文件已经存在，则直接覆盖
//     * @param inputStream
//     * @param fileName
//     * @return 文件的url
//     */
//    public String uploadFileOverride(InputStream inputStream, String fileName) {
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type", "text/plain; charset=utf-8");
//
//        PutObjectArgs putObjectArgs;
//        try {
//            putObjectArgs = PutObjectArgs.builder()
//                    .bucket(bucketName)
//                    .object(fileName)
//                    .stream(inputStream, inputStream.available(), -1)
//                    .headers(headers)
//                    .build();
//        } catch (IOException e) {
//            throw new RuntimeException("Minio：检查输入流是否可用失败", e);
//        }
//
//        try {
//            minioClient.putObject(putObjectArgs);
//        } catch (Exception e) {
//            throw new RuntimeException("Minio：存放对象失败", e);
//        }
//
//        GetPresignedObjectUrlArgs urlArgs = GetPresignedObjectUrlArgs.builder()
//                .bucket(bucketName)
//                .object(fileName)
//                .method(Method.GET)
//                //.expiry(24, TimeUnit.HOURS)
//                .build();
//        String url = null;
//        try {
//            url = minioClient.getPresignedObjectUrl(urlArgs);
//        } catch (Exception e) {
//            throw new RuntimeException("Minio：获得对象URL失败", e);
//        }
//        return url;
//    }
//
//    public void removeFile(String fileName){
//        try {
//            minioClient.removeObject(
//                    RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build()
//            );
//        } catch (Exception e) {
//            throw new RuntimeException("Minio：删除对象失败", e);
//        }
//    }
//
//    @PostConstruct
//    private void init() {
//        minioClient = MinioClient.builder()
//                .endpoint(minioUrl)
//                .credentials(accessKey, secretKey)
//                .build();
//        createBucket();
//    }
//
//    private void createBucket() {
//        boolean isExist = false;
//        try {
//            isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
//        } catch (Exception e) {
//            log.error("检查Minio桶（{}）是否存在失败", bucketName);
//            e.printStackTrace();
//        }
//
//        if (!isExist) {
//            try {
//                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
//            } catch (Exception e) {
//                log.error("创建Minio桶（{}）失败", bucketName);
//                e.printStackTrace();
//            }
//        }
//    }
//}