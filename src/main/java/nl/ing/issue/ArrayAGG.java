package nl.ing.issue;

import org.apache.flink.table.functions.AggregateFunction;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.dataview.ListView;
import org.apache.flink.table.catalog.DataTypeFactory;
import org.apache.flink.table.functions.FunctionDefinition;
import org.apache.flink.table.types.DataType;
import org.apache.flink.table.types.inference.ArgumentCount;
import org.apache.flink.table.types.inference.CallContext;
import org.apache.flink.table.types.inference.ConstantArgumentCount;
import org.apache.flink.table.types.inference.InputTypeStrategy;
import org.apache.flink.table.types.inference.Signature;
import org.apache.flink.table.types.inference.Signature.Argument;
import org.apache.flink.table.types.inference.TypeInference;

/**
 * Aggregate function for collecting rows of type T into an array of elements of type T.
 * This function can be used to collect values from a column or an expression into a list.
 * The order of the elements in the list is not guaranteed.
 * The function supports any data type that can be stored in a ListView.
 */
@SuppressWarnings("unused")
public class ArrayAGG
        extends AggregateFunction<List<Object>, ArrayAGG.ArrayAccumulator> {

    /**
     * The accumulator class for the ArrayAGG function.
     * It contains a ListView to store the values.
     */
    public static class ArrayAccumulator {
        private ListView<Object> list = new ListView<>();


        // get method for list
        public ListView<Object> getList() {
            return this.list;
        }

        public void setList(ListView<Object> list) {
            this.list = list;
        }

        // get method for count

        private void add(Object value) throws Exception {
            this.list.add(value);
        }

        private void addAll(List<Object> other) throws Exception {
            this.list.addAll(other);
        }

        private void remove(Object value) throws Exception {
            this.list.remove(value);
        }

    }

    /**
     * Creates a new accumulator for the ArrayAGG function.
     * @return a new ArrayAccumulator instance with an empty list and zero count.
     */
    @Override
    public ArrayAccumulator createAccumulator() {
        return new ArrayAccumulator();
    }

    /**
     * Accumulates a value into the accumulator's list.
     * If the value is null, it is ignored.
     * @param acc the accumulator to update
     * @param value the value to add to the list
     * @throws Exception if the value cannot be added to the list
     */
    public void accumulate(ArrayAccumulator acc, Object value) throws Exception {
        if (value != null) {
            acc.add(value);
        }
    }


    /**
     * Returns the list of values from the accumulator.
     * @param acc the accumulator to retrieve the list from
     * @return a list of objects that contains the accumulated values
     */
    @Override
    public List<Object> getValue(ArrayAccumulator acc) {
        return acc.getList().getList();
    }

    /**
     * Returns the type inference for the function.
     * The type inference specifies the input, accumulator and output types of the function.
     * The input type is a single argument of any type T.
     * The accumulator type is a structured type field: list
     * The list field is a ListView of type T.
     * The output type is an array of type T.
     * @param typeFactory the type factory to create data types
     * @return a type inference for the function
     */
    @Override
    public TypeInference getTypeInference(DataTypeFactory typeFactory) {
        return TypeInference.newBuilder()
                .inputTypeStrategy(
                        new InputTypeStrategy() {
                            @Override
                            public ArgumentCount getArgumentCount() {
                                return ConstantArgumentCount.of(1);
                            }

                            @Override
                            public Optional<List<DataType>> inferInputTypes(
                                    CallContext callContext, boolean throwOnFailure) {
                                DataType argType = callContext.getArgumentDataTypes().get(0);
                                return Optional.of(Collections.singletonList(argType));
                            }

                            @Override
                            public List<Signature> getExpectedSignatures(FunctionDefinition definition) {
                                return Collections.singletonList(Signature.of(Argument.of("value", "T")));
                            }
                        })
                .accumulatorTypeStrategy(
                        callContext -> {
                            DataType argType = callContext.getArgumentDataTypes().get(0);
                            return Optional.of(
                                    DataTypes.STRUCTURED(
                                            ArrayAccumulator.class,
                                            DataTypes.FIELD("list", ListView.newListViewDataType(argType))));
                        })
                .outputTypeStrategy(
                        callContext -> {
                            DataType argType = callContext.getArgumentDataTypes().get(0);
                            return Optional.of(DataTypes.ARRAY(argType).bridgedTo(List.class));
                        })
                .build();
    }

    /**
     * Retracts a value from the accumulator's list.
     * If the value is null, it is ignored.
     * @param acc the accumulator to update
     * @param value the value to remove from the list
     * @throws Exception if the value cannot be removed from the list
     */
    public void retract(ArrayAccumulator acc, Object value) throws Exception {
        if (value != null) {
            acc.remove(value) ;
        }
    }

    /**
     * Merges a list of accumulators into one accumulator.
     * The list and count fields of the target accumulator are updated with the values from the source accumulators.
     * @param acc the target accumulator to update
     * @param it an iterable of source accumulators to merge
     * @throws Exception if the values cannot be added to the list
     */
    public void merge(ArrayAccumulator acc, Iterable<ArrayAccumulator> it) throws Exception {
        for (ArrayAccumulator other : it) {
            acc.addAll(other.getList().getList()) ;
        }
    }

}