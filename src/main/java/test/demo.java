package test;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.errors.MinioException;

public class demo {
    public static void main(String[] args) throws Exception {
        try {
            //第一步：创建Minio客户端进行连接，共有三个参数 ip:端口，账号，密码
            MinioClient minioClient = new MinioClient("http://127.0.0.1:9000", "minioadmin", "minioadmin");

            //第二步：bucket表示的是文件夹，检查文件夹是否已经存在
            boolean isExist = minioClient.bucketExists("input");
            if(!isExist) {
                //如果input文件夹不存在，则创建一个名为input的文件夹
                minioClient.makeBucket("input");
            }

            /**
             * 第三步：使用putObject()上传一个文件到文件夹中
             * 第一个参数：文件夹；第二个参数：定义的文件名；第三个参数：需要上传文件的文件路径
             */
            minioClient.putObject("input","sample.jpg", "/Users/leiliang/Data/origPic.jpg");

            //第四步：调用statObject()来判断对象(文件)是否存在
            ObjectStat objectStat=minioClient.statObject("input", "sample.jpg");
            if(objectStat!=null){
                System.out.println("存在");
            }

            /**
             * 第五步：使用getObject获取一个文件
             * 第一个参数：文件夹；第二个参数：要获取的文件名；第三个参数：要写入的文件路径
             */
            minioClient.getObject("input", "sample.jpg", "/Users/leiliang/Data/newsample.jpg");
        } catch(MinioException e) {
            System.out.println("使用Minio客户端处理文件出现异常: " + e);
        }
    }
}