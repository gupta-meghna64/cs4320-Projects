import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.*;

/**
 * BPlusTree Class Assumptions: 1. No duplicate keys inserted 2. Order D:
 * D<=number of keys in a node <=2*D 3. All keys are non-negative
 */
public class BPlusTree<K extends Comparable<K>, T> {

	public Node<K,T> root;
	public static final int D = 2;

	/**
	 * A helper function for search that recursively searches down the tree till
	 * it finds the correct LeafNode or does not find it and returns null.
	 * @param key
	 * @param node
	 * @return value
	 */
	public T searchHelp(K key, Node<K,T> node){
		if(node.isLeafNode){
			LeafNode leaf = (LeafNode) node;
			if(leaf.keys.indexOf(key)!=-1){
				return (T) leaf.values.get(leaf.keys.indexOf(key));
			}
			else{
				return null;
			}
		}
		else if(node.isLeafNode==false){
			IndexNode index= (IndexNode) node;
			if(key.compareTo((K) index.keys.get(0))<0){
				searchHelp(key, (Node) index.children.get(0));
			}
			else if(key.compareTo((K) index.keys.get( index.keys.size() -1 )) >=0){
				searchHelp(key, (Node) index.children.get(index.keys.size() ));
			}
			else{
				for(int i=1; i< (index.children.size() -1); i++ ){
					if(key.compareTo( (K) index.keys.get(i)) >= 0 && key.compareTo((K) index.keys.get(i+1))<0. ){
						searchHelp(key, (Node) index.children.get(i));
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * TODO Search the value for a specific key
	 * 
	 * @param key
	 * @return value
	 */
	public T search(K key) {
		return searchHelp(key, root);
	}

	/**
	 * Handles inserting leaves into the tree
	 * @param key
	 * @param value
	 * @param leaf
	 * @return
	 */
	public Entry<K, Node<K, T>> insertLeaf(K key, T value, LeafNode<K,T> leaf){
		leaf.insertSorted(key, value);
		if(leaf.isOverflowed()){
			return splitLeafNode(leaf);
		}
		return null;
	}
	/**
	 * Finds the right place to insert the key
	 * @param key
	 * @param value
	 * @param node
	 */
	public Entry<K, Node<K,T>> insertHelp(K key, T value, Node<K,T> node, Entry<K, Node<K,T>> enter){
		/* //node is not a leaf
		if(node.isLeafNode==false){
			IndexNode index= (IndexNode) node;
			//checks to see if branch to take is first child
			if(key.compareTo((K) index.keys.get(0))<0){
				Node<K, T> child = (Node) index.children.get(0);
				if(child.isLeafNode){
					LeafNode leaf = (LeafNode) child;
					Entry<K, Node<K, T>> split= insertLeaf(key, value, leaf);
					if(split!=null){
						index.insertSorted(split, 0);
						if(index.isOverflowed()){
							return splitIndexNode(index);
						}
						else{
							return null;
						}
					}
				}
				else{
					searchHelp(key, (Node) index.children.get(0));
				}
				
			}
			//checks to see if branch to take is last child
			else if(key.compareTo((K) index.keys.get( index.keys.size() -1 )) >=0){
				Node<K, T> child = (Node) index.children.get(index.keys.size() -1);
				if(child.isLeafNode){
					LeafNode leaf = (LeafNode) child;
					Entry<K, Node<K, T>> split= insertLeaf(key, value, leaf);
					if(split!=null){
						index.insertSorted(split, index.keys.size() );
						if(index.isOverflowed()){
							return splitIndexNode(index);
						}
						else{
							return null;
						}
					}
				}
				else{
					searchHelp(key, (Node) index.children.get(index.keys.size() -1));
				}
			}
			//checks all other children to see where to input between
			else{
				for(int i=1; i< (index.children.size() -1); i++ ){
					if(key.compareTo( (K) index.keys.get(i)) >= 0 && key.compareTo((K) index.keys.get(i+1))<0){						
						Node<K, T> child = (Node) index.children.get(i);
						if(child.isLeafNode){
							LeafNode leaf = (LeafNode) child;
							Entry<K, Node<K, T>> split= insertLeaf(key, value, leaf);
							if(split!=null){
								index.insertSorted(split, i);
								if(index.isOverflowed()){
									return splitIndexNode(index);
								}
								else{
									return null;
								}
							}
						}
						else{
							searchHelp(key, (Node) index.children.get(index.keys.size() -1));
						}
					}
				}
			}
		}
		//root is a leaf
		else{
			if(node.isLeafNode){
				Entry<K, Node<K, T>> split=insertLeaf(key, value, (LeafNode) node);
				if(split!=null){
					root = new IndexNode<K,T>(split.getKey(), node, split.getValue());
					return null;
				}
			}
		}
		return null;*/
		
		if(!node.isLeafNode){
			int ind=0;
			IndexNode index= (IndexNode) node;
			Node<K,T> child=null;
			if(key.compareTo((K) index.keys.get(0))<0){
				child = (Node) index.children.get(0);
			}
			else if(key.compareTo((K) index.keys.get( index.keys.size() -1 )) >=0){
				child = (Node) index.children.get(index.keys.size());
				ind= index.keys.size()-1;
			}
			else{
				for(int i=1; i< (index.children.size() -1); i++ ){
					if(key.compareTo( (K) index.keys.get(i)) >= 0 && key.compareTo((K) index.keys.get(i+1))<0){						
						child = (Node) index.children.get(i);
						ind=i;
					}
				}			
			}
			Entry<K, Node<K,T>> nchild=insertHelp(key, value, child, null);
			if(nchild==null){
				return null;
			}
			else{
				Entry<K,Node<K,T>> split=null;
				index.insertSorted(nchild, ind);
				if(index.isOverflowed()){
					split = splitIndexNode(index);
					if(node==root){
						IndexNode<K,T> nroot = new IndexNode<K,T>(split.getKey(), root, split.getValue());
						root=nroot;
						return null;
					}
					return split;
				}
				else{
					return null;
				}

			}
		}
		else{
			LeafNode<K,T> leaf = (LeafNode) node;
			leaf.insertSorted(key, value);
			if(leaf.isOverflowed()){
				return splitLeafNode(leaf);
			}
		}
		return null;
	}
	/**
	 * TODO Insert a key/value pair into the BPlusTree
	 * 
	 * @param key
	 * @param value
	 */
	public void insert(K key, T value) {
		if(root==null){
			root = new LeafNode<K,T>(key, value);
		}
		else{
			insertHelp(key, value, root, null);
		}

	}

	/**
	 * TODO Split a leaf node and return the new right node and the splitting
	 * key as an Entry<slitingKey, RightNode>
	 * 
	 * @param leaf
	 * @return the key/node pair as an Entry
	 */
	public Entry<K, Node<K,T>> splitLeafNode(LeafNode<K,T> leaf) {
		ArrayList<K> rKeys = new ArrayList<K>();
		ArrayList<T> rValues = new ArrayList<T>();
		for(int i= D; i<leaf.keys.size(); i++ ){
			rKeys.add(leaf.keys.get(i));
			rValues.add(leaf.values.get(i));
		}
		for(int i=0; i<rKeys.size(); i++){
			leaf.keys.remove(rKeys.get(i));
			leaf.values.remove(rValues.get(i));
		}
		LeafNode<K,T> right = new LeafNode<K, T>(rKeys, rValues);
		right.nextLeaf=leaf.nextLeaf;
		leaf.nextLeaf=right;
		right.previousLeaf=leaf;
		return (new SimpleEntry<K, Node<K,T>>(rKeys.get(0), right) );
		
	}

	/**
	 * TODO split an indexNode and return the new right node and the splitting
	 * key as an Entry<slitingKey, RightNode>
	 * 
	 * @param index
	 * @return new key/node pair as an Entry
	 */
	public Entry<K, Node<K,T>> splitIndexNode(IndexNode<K,T> index) {
		ArrayList<K> rKeys = new ArrayList<K>();
		ArrayList<Node<K,T>> childe = new ArrayList<Node<K,T>>();
		for(int i= D; i<index.keys.size(); i++ ){
			rKeys.add(index.keys.get(i));
		}
		for(int i=D+1 ; i<index.children.size(); i++){
			childe.add(index.children.get(i));
		}
		for(int i=0; i<rKeys.size(); i++){
			index.keys.remove(rKeys.get(i));
		}
		for(int i=0; i<childe.size(); i++){
			index.children.remove(childe.get(i));
		}
		K nkey = rKeys.get(0);
		rKeys.remove(nkey);
		IndexNode<K,T> right = new IndexNode<K, T>(rKeys, childe);
		return (new SimpleEntry<K, Node<K,T>>(nkey, right) );
		
	}

	/**
	 * TODO Delete a key/value pair from this B+Tree
	 * 
	 * @param key
	 */
	public void delete(K key) {

	}

	/**
	 * TODO Handle LeafNode Underflow (merge or redistribution)
	 * 
	 * @param left
	 *            : the smaller node
	 * @param right
	 *            : the bigger node
	 * @param parent
	 *            : their parent index node
	 * @return the splitkey position in parent if merged so that parent can
	 *         delete the splitkey later on. -1 otherwise
	 */
	public int handleLeafNodeUnderflow(LeafNode<K,T> left, LeafNode<K,T> right,
			IndexNode<K,T> parent) {
		return -1;

	}

	/**
	 * TODO Handle IndexNode Underflow (merge or redistribution)
	 * 
	 * @param left
	 *            : the smaller node
	 * @param right
	 *            : the bigger node
	 * @param parent
	 *            : their parent index node
	 * @return the splitkey position in parent if merged so that parent can
	 *         delete the splitkey later on. -1 otherwise
	 */
	public int handleIndexNodeUnderflow(IndexNode<K,T> leftIndex,
			IndexNode<K,T> rightIndex, IndexNode<K,T> parent) {
		return -1;
	}

}
