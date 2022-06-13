package flink;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamWordCount {
    final static Logger logger = LoggerFactory.getLogger(StreamWordCount.class);

    public static void main(String[] args) throws Exception {
        // stream exe env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setParallelism(8); // in cluster using config

//        String inputPath = "/Users/leiliang/My-project/Git/flink-minio/src/main/resources/hello.txt";;
//        DataStream<String> inputDataStream = env.readTextFile(inputPath);

        // parameter tool, read from configuration
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String host = parameterTool.get("host");
        int port = parameterTool.getInt("port");

        // netcat, socket text stream
//        env.socketTextStream("localhost", 9000).print();
//        DataStream<String> inputDataStream = env.fromElements("hello world");
        DataStream<String> inputDataStream = env.socketTextStream(host, port);


        SingleOutputStreamOperator<Tuple2<String, Integer>> resultStream = inputDataStream.flatMap(new WordCount.MyFlatMapper())
                .keyBy(tuple -> tuple.f0)
                .sum(1).setParallelism(2);

        resultStream.print().setParallelism(1);

        // execute
        env.execute();
    }
}
