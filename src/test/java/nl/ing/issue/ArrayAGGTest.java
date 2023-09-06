package nl.ing.issue;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.planner.factories.TestValuesTableFactory;
import org.apache.flink.test.junit5.MiniClusterExtension;
import org.apache.flink.types.Row;
import org.apache.flink.types.RowKind;
import org.apache.flink.util.CloseableIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

class ArrayAGGTest {
    protected StreamExecutionEnvironment env;
    protected StreamTableEnvironment tEnv;

    @RegisterExtension
    public static final MiniClusterExtension flinkCluster =
            new MiniClusterExtension(
                    new MiniClusterResourceConfiguration.Builder()
                            .setNumberTaskManagers(2)
                            .setNumberTaskManagers(1)
                            .setConfiguration(new Configuration())
                            .build());

    @BeforeEach
    public void setUp() {
        TestValuesTableFactory.clearAllData();
        env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().setRestartStrategy(RestartStrategies.noRestart());
        env.setStateBackend(new HashMapStateBackend());

        tEnv = StreamTableEnvironment.create(env, EnvironmentSettings.newInstance().inStreamingMode().build());
        tEnv.createTemporaryFunction("ArrayAgg", ArrayAGG.class);
    }

    @Test
    public void stringAggregation() throws Exception {
        String dataId = TestValuesTableFactory.registerData(List.of(
                Row.of("john", 35, "NL"),
                Row.of("alice", 32, "NL"),
                Row.of("bob", 35, "BE"),
                Row.of("sarah", 32, "BE")
        ));

        tEnv.executeSql(String.format(
                "CREATE TABLE input (`name` STRING, `age` INTEGER, `place` STRING, `ambi` AS ROW(name,place))%n" +
                        "WITH ('connector' = 'values', 'data-id' = '%s')",
                dataId)
        );

        TableResult resultTable = tEnv.executeSql("SELECT age, ArrayAgg(DISTINCT name) FROM input GROUP BY age");

        List<Row> actualResult;
        try (CloseableIterator<Row> rowCloseableIterator = resultTable.collect()) {
            List<Row> results = new ArrayList<>();
            rowCloseableIterator.forEachRemaining(results::add);
            actualResult = results;
        }
        Row[] expectedResult = {
                Row.ofKind(RowKind.INSERT, 35, new String[]{"john"}),
                Row.ofKind(RowKind.UPDATE_BEFORE, 35, new String[]{"john"}),
                Row.ofKind(RowKind.UPDATE_AFTER, 35, new String[]{"john", "bob"}),
                Row.ofKind(RowKind.INSERT, 32, new String[]{"alice"}),
                Row.ofKind(RowKind.UPDATE_BEFORE, 32, new String[]{"alice"}),
                Row.ofKind(RowKind.UPDATE_AFTER, 32, new String[]{"alice", "sarah"})
        };

        assertThat(actualResult, containsInAnyOrder(expectedResult));
    }
}