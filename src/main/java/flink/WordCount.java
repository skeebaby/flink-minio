package flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

// batch processing
public class WordCount {
    public static void main(String[] args) throws Exception {
        // execute env
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // read data from file
        String inputPath = "/Users/leiliang/My-project/Git/flink-minio/src/main/resources/hello.txt";
        DataSource<String> inputDataSet = env.readTextFile(inputPath);

        // process, split by " " -> (word, 1)
        DataSet<Tuple2<String, Integer>> resultSet = inputDataSet.flatMap(new MyFlatMapper())
                .groupBy(0) // 按照第一个位置的word分组
                .sum(1); // 按第二个位置求和

        resultSet.print();
    }

    public static class MyFlatMapper implements FlatMapFunction<String, Tuple2<String, Integer>> {
        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
            String[] words = value.split(" ");

            for(String word : words) {
                out.collect(new Tuple2<>(word, 1));
            }
        }
    }
}
