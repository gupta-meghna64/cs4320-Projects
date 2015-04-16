import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;

public class LeftoverMapper extends Mapper<IntWritable, Node, IntWritable, Node> {

    public void map(IntWritable nid, Node N, Context context) throws IOException, InterruptedException {	
	//Implement
    	Configuration con = context.getConfiguration();
    	Double leftover = Double.parseDouble(con.get("leftover"));
    	Double size= Double.parseDouble(con.get("size"));
    	Double pr = new Double(leftover/100000);
    	Double p_prime= LeftoverReducer.alpha*(1/size)
    					+(1-LeftoverReducer.alpha)*((pr/size)+N.getPageRank());
    	N.setPageRank(p_prime);
    	context.write(nid, N);
    }
}
