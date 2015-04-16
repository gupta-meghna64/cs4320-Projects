import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;

public class TrustReducer extends Reducer<IntWritable, NodeOrDouble, IntWritable, Node> {
    public void reduce(IntWritable key, Iterable<NodeOrDouble> values, Context context)
	throws IOException, InterruptedException {
	//Implement
		//key is nodeid
		//value is list of nodeOrdoubles
		Double partial_pr=0.0;
		Node node=null;
		Counter pr = context.getCounter(PageRank.had_counter.leftover_pr);
		Counter nodes = context.getCounter(PageRank.had_counter.num_nodes);
		for(NodeOrDouble i: values){
			if(!i.isNode()){
				partial_pr+=i.getDouble();
			}
			else{
				//NodeOrDouble is a node	
				node=i.getNode();
				if(node.outgoingSize()==0){
					Double part =node.getPageRank()*100000;
					pr.increment(part.longValue());
				}
			}
		}
		node.setPageRank( partial_pr.doubleValue());
		nodes.increment(1);
		context.write(key, node);
    }
}
