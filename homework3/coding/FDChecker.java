import java.util.*;

public class FDChecker {

	/**
	 * Checks whether a decomposition of a table is dependency
	 * preserving under the set of functional dependencies fds
	 * 
	 * @param t1 one of the two tables of the decomposition
	 * @param t2 the second table of the decomposition
	 * @param fds a complete set of functional dependencies that apply to the data
	 * 
	 * @return true if the decomposition is dependency preserving, false otherwise
	 **/
	public static boolean checkDepPres(AttributeSet t1, AttributeSet t2, Set<FunctionalDependency> fds) {
		//your code here
		//a decomposition is dependency preserving, if local functional dependencies are
		//sufficient to enforce the global properties
		//To check a particular functional dependency a -> b is preserved, 
		//you can run the following algorithm
		//result = a
		//while result has not stabilized
		//	for each table in the decomposition
		//		t = result intersect table 
		//		t = closure(t) intersect table
		//		result = result union t
		//if b is contained in result, the dependency is preserved
		Iterator<FunctionalDependency> iter = fds.iterator();
		ArrayList<AttributeSet> tables = new ArrayList<AttributeSet>();
		tables.add(t1);
		tables.add(t2);
		
		while(iter.hasNext()){
			FunctionalDependency curr = iter.next();
			AttributeSet result= new AttributeSet(curr.left);//result				
			
			AttributeSet prev= new AttributeSet(); //previous result to check
			
			while(!prev.equals(result)){
				prev.addAll(result);
				for(AttributeSet z: tables){
					AttributeSet temp= new AttributeSet();// initializes a temporary set to use
					
					AttributeSet t= closure(intersect(z, result), fds); //taking attribute closure of Z intersect Result
					
					temp.addAll(t); //adding attribute closure of Z interesect Result
					result.addAll(intersect(temp, z)); //union of result and the intersection of the closure of temp with Z
				}

			}
			if(!result.contains(curr.right)){
				return false; //if b is contained in result its preserved and flag then is set to true
			}
		}
		return true; //checks to see if false is located in the array, if so then false else true
	}
	
	public static AttributeSet intersect(AttributeSet a, AttributeSet b){
		AttributeSet in = new AttributeSet(a);
		in.retainAll(b);
		return in;
	}

	/**
	 * Checks whether a decomposition of a table is lossless
	 * under the set of functional dependencies fds
	 * 
	 * @param t1 one of the two tables of the decomposition
	 * @param t2 the second table of the decomposition
	 * @param fds a complete set of functional dependencies that apply to the data
	 * 
	 * @return true if the decomposition is lossless, false otherwise
	 **/
	public static boolean checkLossless(AttributeSet t1, AttributeSet t2, Set<FunctionalDependency> fds) {
		//your code here
		//Lossless decompositions do not lose information, the natural join is equal to the 
		//original table.
		//a decomposition is lossless if the common attributes for a superkey for one of the
		//tables.
		boolean flag=false;

		AttributeSet inter= intersect(t1,t2);
		AttributeSet closure= closure(inter, fds);
		if(closure.containsAll(t1) || closure.containsAll(t2)){
			flag=true;
		}
		return flag;
	}

	//recommended helper method
	//finds the total set of attributes implied by attrs
	private static AttributeSet closure(AttributeSet attrs, Set<FunctionalDependency> fds) {
		//closure=X
		//repeat until there is no change:{
		//if there is an FD U-> V in F such that U subset closure,
		//	then set closure = closure union V }
		AttributeSet closure= new AttributeSet(attrs); 		// set closure equal to X (attrs)
		AttributeSet prev= new AttributeSet(); 		//prev to keep track of changes to closure
		while(!prev.equals(closure)){ 				//repeat until no change
			Iterator<FunctionalDependency> iter= fds.iterator(); 			// iterator to iterate through fds
			prev.addAll(closure);
			while(iter.hasNext()){					//goes through fds
				FunctionalDependency curr = iter.next(); //currently used fd U->V
				if(closure.containsAll(curr.left)){								// if U is in closure
					closure.add(curr.right);									// closure union V
				}
			}
		}
		return closure;
	}
}
