export MAVEN_OPTS='-Xmx1500m -Xms300m'
mvn exec:java -Dexec.mainClass="com.billybyte.spanjava.mains.RunBuildSettlesImpVolFromSpan" -Dexec.args="runBuildSettlesImpVolsFromSpanArgs.xml"

