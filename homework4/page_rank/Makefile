hadoop = hadoop

bases = LeftoverMapper LeftoverReducer NodeInputFormat Node NodeOrDouble NodeOutputFormat NodeRecordReader NodeRecordWriter TrustMapper TrustReducer PageRank
classDir = classes
sourceDir = src
HADOOP_PREFIX=/usr/local/Cellar/hadoop/2.6.0/libexec
javaFiles = src/*.java
classFiles = classes/*.class

CLASS_PATH=$(HADOOP_PREFIX)/share/hadoop/common/*:$(HADOOP_PREFIX)/share/hadoop/yarn/lib/*:$(HADOOP_PREFIX)/share/hadoop/mapreduce/lib/*:$(HADOOP_PREFIX)/share/hadoop/mapreduce/*:./
j = -classpath $(CLASS_PATH) #Xlint:deprecation


default : $(javaFiles)
	mkdir $(classDir); javac $j -d $(classDir) $(javaFiles); jar cvf PageRank.jar $(classDir);echo $(classFiles); $(hadoop) jar PageRank.jar PageRank

clean : 
	rm -r $(classDir); rm -r stage*; rm PageRank.jar;



