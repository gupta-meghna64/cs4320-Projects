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
		boolean flag[]=new boolean[fds.size()]; //array to keep track if each functional dependency passes
		int cnt=0;
		Iterator iter = fds.iterator();
		while(iter.hasNext()){
			FunctionalDependency curr = (FunctionalDependency) iter.next();
			AttributeSet result= new AttributeSet();//result
			result.addAll(curr.left);
			Iterator resit = result.iterator();
			
			AttributeSet prev= new AttributeSet(); //previous result to check
			prev.addAll(result);
			
		
			AttributeSet union= new AttributeSet(); //union of t1 and t2 to go through entire thing
			union.addAll(t1);
			union.addAll(t2);
			
			while(resit.hasNext()){
				Iterator unit = union.iterator();
				while(unit.hasNext()){
					AttributeSet temp= new AttributeSet();// initializes a temporary set to use
					Attribute att = (Attribute) unit.next(); //current attribute of the union set
					AttributeSet z= new AttributeSet(); //adds it to z by making it an attributeSet
					z.add(att);
					
					temp.addAll(result);
					temp.retainAll(z); //taking intersection of result and table
					
					AttributeSet t= closure(temp, fds); //taking attribute closure of table with the fds set
					temp.clear(); //resets temp
					
					temp.addAll(t);
					temp.retainAll(z); //taking intersection of the closure of temp with the table
					result.addAll(temp); //union of result and the intersection of the closure of temp with table
				}
				
				if(prev.equals(result)){
					break; //will break if prev does not equal result
				}
				else{
					prev.clear();
					prev.addAll(result); //resets prev to equal the new result
				}
			}
			if(result.contains(curr.right)){
				flag[cnt]=true; //if b is contained in result its preserved and flag then is set to true
			}
			cnt++; 
		}
		return (!Arrays.asList(flag).contains(false)); //checks to see if false is located in the array, if so then false else true
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

		AttributeSet inter= new AttributeSet();
		inter.addAll(t1);
		inter.retainAll(t2);
		AttributeSet closure= closure(inter, fds);
		if(closure.containsAll(t1) || closure.containsAll(t2)){
			flag=true;
		}
		return flag;
	}

	//recommended helper method
	//finds the total set of attributes implied by attrs
	private static AttributeSet closure(AttributeSet attrs, Set<FunctionalDependency> fds) {
		AttributeSet closure= attrs;
		Iterator iter= fds.iterator();
		AttributeSet prev= new AttributeSet();
		prev.addAll(closure);
		while(iter.hasNext()){
			FunctionalDependency curr = (FunctionalDependency) iter.next();
			if(closure.containsAll(curr.left)){
				closure.add(curr.right);
			}
			if(prev.equals(closure)){
				break;
			}
			else{
				prev.add(curr.right);
			}
		}
		return closure;
	}
}
