# Project to reproduce the error in Unit Test using Flink 1.17.1

When migrating a Custom UDF project from Flink 1.16.2 to Flink 1.17.1 we get the following [error](stacktrace.txt) in the Unit Test.
<details>
<summary><b>java.lang.IllegalArgumentException: fromIndex(2) > toIndex(0)</b></summary>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at java.base/java.util.AbstractList.subListRangeCheck(AbstractList.java:509)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at java.base/java.util.AbstractList.subList(AbstractList.java:497)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.calcite.rel.metadata.janino.CacheGeneratorUtil$CacheKeyStrategy$1.safeArgList(CacheGeneratorUtil.java:213)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.calcite.rel.metadata.janino.CacheGeneratorUtil$CacheKeyStrategy$1.cacheKeyBlock(CacheGeneratorUtil.java:205)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.calcite.rel.metadata.janino.CacheGeneratorUtil.cachedMethod(CacheGeneratorUtil.java:68)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.calcite.rel.metadata.janino.RelMetadataHandlerGeneratorUtil.generateHandler(RelMetadataHandlerGeneratorUtil.java:121)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.calcite.rel.metadata.JaninoRelMetadataProvider.generateCompileAndInstantiate(JaninoRelMetadataProvider.java:138)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.calcite.rel.metadata.JaninoRelMetadataProvider.lambda$static$0(JaninoRelMetadataProvider.java:73)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.flink.calcite.shaded.com.google.common.cache.CacheLoader$FunctionToCacheLoader.load(CacheLoader.java:165)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.flink.calcite.shaded.com.google.common.cache.LocalCache$LoadingValueReference.loadFuture(LocalCache.java:3529)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.flink.calcite.shaded.com.google.common.cache.LocalCache$Segment.loadSync(LocalCache.java:2278)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.flink.calcite.shaded.com.google.common.cache.LocalCache$Segment.lockedGetOrLoad(LocalCache.java:2155)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.flink.calcite.shaded.com.google.common.cache.LocalCache$Segment.get(LocalCache.java:2045)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.flink.calcite.shaded.com.google.common.cache.LocalCache.get(LocalCache.java:3951)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.flink.calcite.shaded.com.google.common.cache.LocalCache.getOrLoad(LocalCache.java:3974)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.flink.calcite.shaded.com.google.common.cache.LocalCache$LocalLoadingCache.get(LocalCache.java:4958)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.calcite.rel.metadata.JaninoRelMetadataProvider.revise(JaninoRelMetadataProvider.java:197)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.calcite.rel.metadata.RelMetadataQueryBase.revise(RelMetadataQueryBase.java:118)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.calcite.rel.metadata.RelMetadataQuery.getPulledUpPredicates(RelMetadataQuery.java:844)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.calcite.rel.rules.ReduceExpressionsRule$ProjectReduceExpressionsRule.onMatch(ReduceExpressionsRule.java:307)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.calcite.plan.AbstractRelOptPlanner.fireRule(AbstractRelOptPlanner.java:337)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.calcite.plan.hep.HepPlanner.applyRule(HepPlanner.java:565)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.calcite.plan.hep.HepPlanner.applyRules(HepPlanner.java:428)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.calcite.plan.hep.HepPlanner.executeInstruction(HepPlanner.java:251)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.calcite.plan.hep.HepInstruction$RuleInstance.execute(HepInstruction.java:130)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.calcite.plan.hep.HepPlanner.executeProgram(HepPlanner.java:208)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.calcite.plan.hep.HepPlanner.findBestExp(HepPlanner.java:195)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.flink.table.planner.plan.optimize.program.FlinkHepProgram.optimize(FlinkHepProgram.scala:64)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.flink.table.planner.plan.optimize.program.FlinkHepRuleSetProgram.optimize(FlinkHepRuleSetProgram.scala:78)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.flink.table.planner.plan.optimize.program.FlinkChainedProgram.$anonfun$optimize$1(FlinkChainedProgram.scala:59)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at scala.collection.TraversableOnce.$anonfun$foldLeft$1(TraversableOnce.scala:156)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at scala.collection.TraversableOnce.$anonfun$foldLeft$1$adapted(TraversableOnce.scala:156)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at scala.collection.Iterator.foreach(Iterator.scala:937)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at scala.collection.Iterator.foreach$(Iterator.scala:937)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at scala.collection.AbstractIterator.foreach(Iterator.scala:1425)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at scala.collection.IterableLike.foreach(IterableLike.scala:70)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at scala.collection.IterableLike.foreach$(IterableLike.scala:69)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at scala.collection.AbstractIterable.foreach(Iterable.scala:54)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at scala.collection.TraversableOnce.foldLeft(TraversableOnce.scala:156)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at scala.collection.TraversableOnce.foldLeft$(TraversableOnce.scala:154)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at scala.collection.AbstractTraversable.foldLeft(Traversable.scala:104)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.flink.table.planner.plan.optimize.program.FlinkChainedProgram.optimize(FlinkChainedProgram.scala:55)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.flink.table.planner.plan.optimize.StreamCommonSubGraphBasedOptimizer.optimizeTree(StreamCommonSubGraphBasedOptimizer.scala:176)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.flink.table.planner.plan.optimize.StreamCommonSubGraphBasedOptimizer.doOptimize(StreamCommonSubGraphBasedOptimizer.scala:83)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.flink.table.planner.plan.optimize.CommonSubGraphBasedOptimizer.optimize(CommonSubGraphBasedOptimizer.scala:87)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.flink.table.planner.delegation.PlannerBase.optimize(PlannerBase.scala:329)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.flink.table.planner.delegation.PlannerBase.translate(PlannerBase.scala:195)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.flink.table.api.internal.TableEnvironmentImpl.translate(TableEnvironmentImpl.java:1803)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.flink.table.api.internal.TableEnvironmentImpl.executeQueryOperation(TableEnvironmentImpl.java:945)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.flink.table.api.internal.TableEnvironmentImpl.executeInternal(TableEnvironmentImpl.java:1422)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at org.apache.flink.table.api.internal.TableEnvironmentImpl.executeSql(TableEnvironmentImpl.java:765)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at nl.ing.issue.ArrayAGGTest.executeSql(ArrayAGGTest.java:83)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at nl.ing.issue.ArrayAGGTest.stringAggregation(ArrayAGGTest.java:103)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at java.base/java.lang.reflect.Method.invoke(Method.java:566)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)<br/>
</details>

It works fine when building this project 
- using Flink 1.16.2
- using Flink 1.17.1 and disable the code coverage (`-Djacoco.skip=true`)

## Building the project using Flink 1.16.2 succeeds without any error.

> mvn clean install -Dflink.version=1.16.2

[recorded output 1.16.2](recorded-output-1.16.2.ansi)

## Building the project using Flink 1.17.1 fails with an error.

> mvn clean install -Dflink.version=1.17.1

[recorded output 1.17.1](recorded-output-1.17.1.ansi)

## Building the project using Flink 1.17.1 without code coverage succeeds without any error.

> mvn clean install -Dflink.version=1.17.1 -Djacoco.skip=true

[recorded output 1.17.1 without code coverage](recorded-output-1.17.1-jacoco.skip.ansi)
