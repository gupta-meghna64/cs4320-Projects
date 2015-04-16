import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;


public class TrustMapper extends Mapper<IntWritable, Node, IntWritable, NodeOrDouble> {
    public void map(IntWritable key, Node value, Context context) throws IOException, InterruptedException {

	//Implement 
		//key is nodeid
    	//value is node
    	//emit the node
    	context.write(key, new NodeOrDouble(value));
    	for(int i: value.outgoing){
    		context.write(new IntWritable(i), new NodeOrDouble(value.getPageRank()/value.outgoingSize()));
    	}
	}
}
