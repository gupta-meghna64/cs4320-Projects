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
		T found=null;
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
				found = searchHelp(key, (Node) index.children.get(0));
			}
			else if(key.compareTo((K) index.keys.get( index.keys.size() -1 )) >=0){
				found =searchHelp(key, (Node) index.children.get(index.keys.size() ));
			}
			else{
				for(int i=1; i< (index.children.size() -1); i++ ){
					if(key.compareTo( (K) index.keys.get(i)) >= 0 && key.compareTo((K) index.keys.get(i+1))<0. ){
						found =searchHelp(key, (Node) index.children.get(i+1));
					}
				}
			}
		}
		return found;
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
	 * Finds the right place to insert the key
	 * @param key
	 * @param value
	 * @param node
	 */
	public Entry<K, Node<K,T>> insertHelp(K key, T value, Node<K,T> node, Entry<K, Node<K,T>> enter){
		Entry<K, Node<K,T>> nchild=null;
		if(!node.isLeafNode){
			int ind=0;
			IndexNode index= (IndexNode) node;
			Node<K,T> child=null;
			if(key.compareTo((K) index.keys.get(0))<0){
				if(index.children.size()>0){
					child = (Node) index.children.get(0);
				}
			}
			else if(key.compareTo((K) index.keys.get( index.keys.size() -1 )) >=0){
				if(index.children.size()<=index.keys.size() && index.children.size()>0){
					child = (Node) index.children.get(index.children.size()-1);
				}
				else if(index.children.size()>index.keys.size()) {
					child = (Node) index.children.get(index.keys.size());
				}
				ind= index.keys.size();
			}
			else{
				for(int i=0; i< (index.children.size() -1); i++ ){
					if(key.compareTo( (K) index.keys.get(i)) >= 0 && key.compareTo((K) index.keys.get(i+1))<0){						
						child = (Node) index.children.get(i);
						ind=i;
					}
				}			
			}
			if(child==null){
				return null;
			}
			nchild=insertHelp(key, value, child, enter);
			if(nchild==null){
				return null;
			}
			
			else{
				if(node.keys.size()<D*2){
					index.insertSorted(nchild, ind);
					nchild=null;
					return nchild;
				}
				else{
					index.insertSorted(nchild, ind);
					Entry<K,Node<K,T>> split = splitIndexNode(index);
					nchild=split;
					if(node==root){
						
						IndexNode<K,T> s = (IndexNode )split.getValue();
						s.keys.remove(0);
						
						IndexNode<K,T> nroot = new IndexNode<K,T>(split.getKey(), root, s);
						root=nroot;
						return null;
					}
					return nchild;
				}
			}
		}
		else{
			LeafNode<K,T> leaf = (LeafNode) node;
			if(leaf.keys.size()<D*2){
				leaf.insertSorted(key, value);
			}
			else{
				leaf.insertSorted(key, value);
				nchild = splitLeafNode(leaf);
				return nchild;
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
		else if(root.isLeafNode){
			LeafNode<K,T> leaf = (LeafNode) root;
			if(leaf.keys.size()<D*2){
				leaf.insertSorted(key, value);
			}
			else{
				leaf.insertSorted(key, value);
				Entry<K, Node<K,T>> nchild = splitLeafNode(leaf);
				IndexNode<K,T> s = new IndexNode<K,T>(nchild.getKey(), leaf, nchild.getValue());
				root=s;
			}
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
		IndexNode<K,T> right = new IndexNode<K, T>(rKeys, childe);
		return (new SimpleEntry<K, Node<K,T>>(nkey, right) );
		
	}
	/**
	 * A helper method to help delete based off the books pseudocode
	 * @param parent
	 * @param node
	 * @param key
	 * @param childKey
	 * @return k key for recursive purposes, should return null to delete function
	 */
	public K deleteHelp( IndexNode<K,T> parent, Node<K,T> node, K key, K childKey){
		//node is an indexNode
		if(!node.isLeafNode){
			K child=null;
			IndexNode<K,T> index= (IndexNode) node;
			if(key.compareTo((K) index.keys.get(0))<0){
				child=deleteHelp((IndexNode) node, (Node) index.children.get(0), key, null);
			}
			else if(key.compareTo((K) index.keys.get( index.keys.size() -1 )) >=0){
				child=deleteHelp((IndexNode) node, (Node) index.children.get(index.keys.size()), key, null);
			}
			else{
				for(int i=1; i< (index.children.size() -1); i++ ){
					if(key.compareTo( (K) index.keys.get(i)) >= 0 && key.compareTo((K) index.keys.get(i+1))<0. ){
						child=deleteHelp((IndexNode) node, (Node) index.children.get(i+1), key, null);
					}
				}
			}
			if(child==null){
				return null;
			}
			else{
				index.keys.remove(child);
				if(!index.isUnderflowed() || index==root){
					return null;
				}
				else{
					//handle underflow of indexNode
					int splitKey=-1;
					int small = parent.children.indexOf(index);
					//uses left node to merge/redib
					if(small>0){
						splitKey = handleIndexNodeUnderflow((IndexNode) parent.children.get(small), 
									(IndexNode)parent.children.get(small-1), parent);
					}
					//uses right node to merge/redib
					else{
						splitKey = handleIndexNodeUnderflow((IndexNode) parent.children.get(small), 
								(IndexNode)parent.children.get(small+1), parent);
					}
					if(splitKey==-1){
						return null;
					}
					//merged and need to remove Node from parent
					else{
						return parent.keys.get(splitKey);
					}
				}
			}			
			
		}
		//node is a leafNode
		else{
			LeafNode<K,T> leaf= (LeafNode) node;
			int index= leaf.keys.indexOf(key);
			leaf.keys.remove(index);
			leaf.values.remove(index);
			//leaf is underflowed
			if(leaf.isUnderflowed() && leaf!=root){
				int splitKey=-1;
				int small = parent.children.indexOf(leaf);
				//uses left node to merge/redib
				if(small>0){
					splitKey = handleLeafNodeUnderflow((LeafNode) parent.children.get(small), 
								(LeafNode)parent.children.get(small-1), parent);
				}
				//uses right node to merge/redib
				else{
					splitKey = handleLeafNodeUnderflow((LeafNode) parent.children.get(small), 
							(LeafNode)parent.children.get(small+1), parent);
				}
				if(splitKey==-1){
					return null;
				}
				//merged and need to remove Node from parent
				else{
					return parent.keys.get(splitKey);
				}
			}
			else{
				return null;
			}
		}

	}
	/**
	 * TODO Delete a key/value pair from this B+Tree
	 * 
	 * @param key
	 */
	public void delete(K key) {
		if(search(key)!=null){
			deleteHelp(null, root, key, null);
		}
		if(root.keys.size()==0 && !root.isLeafNode){
			IndexNode<K,T> ind=(IndexNode) root;
			if(ind.children.size()==1){
				root= ind.children.get(0);	
			}
		}
	}

	/**
	 * TODO Handle LeafNode Underflow (merge or redistribution)
	 * 
	 * @param small
	 *            : the smaller node
	 * @param large
	 *            : the bigger node
	 * @param parent
	 *            : their parent index node
	 * @return the splitkey position in parent if merged so that parent can
	 *         delete the splitkey later on. -1 otherwise
	 */
	public int handleLeafNodeUnderflow(LeafNode<K,T> small, LeafNode<K,T> large,
			IndexNode<K,T> parent) {
		int index=-1;
		//merge
		if(small.keys.size()+large.keys.size()<=2*D){			
			//small is on the left
			if(parent.children.indexOf(small)<parent.children.indexOf(large)){
				large.previousLeaf=small.previousLeaf;
			}
			//small is on the right 
			else{
				large.nextLeaf=small.nextLeaf;
			}
			index=parent.keys.indexOf(large.keys.get(0));
			for(int i=0; i<small.keys.size(); i++){
				large.insertSorted(small.keys.get(i), small.values.get(i));
			}
			small.keys.clear();
			small.values.clear();
			parent.children.remove(small);
			if(parent==root && parent.keys.size()==1 && parent.children.size()==1 
					&& parent.children.get(0).isLeafNode){
				root=parent.children.get(0);
				return -1;
			}
			//returns the key to remove from parent
			return index;
		}
		//redistribute
		else{
			//small is on the left
			if(parent.children.indexOf(small)<parent.children.indexOf(large)){
				index=parent.keys.indexOf(large.keys.get(0));
				int i;
				for(i=0; i<large.keys.size(); i++){
					small.insertSorted(large.keys.get(i), large.values.get(i));
					if(small.keys.size()==large.keys.size()-i+1){
						break;
					}
				}
				for(int j=0; j<i+1; j++){
					large.keys.remove(j);
					large.values.remove(j);
				}
				//remove old split key and replace it
				parent.keys.remove(index);
				parent.keys.add(index, large.keys.get(0));
				return -1;
			}
			//small is on the right
			else{
				index=parent.keys.indexOf(small.keys.get(0));
				for(int i=large.keys.size()-1; i>=0; i++){
					small.insertSorted(large.keys.get(i), large.values.get(i));
					large.keys.remove(i);
					large.values.remove(i);
				}
				parent.keys.remove(index);
				parent.keys.add(index, small.keys.get(0));
				return -1;
			}
		}
	}

	/**
	 * TODO Handle IndexNode Underflow (merge or redistribution)
	 * 
	 * @param small
	 *            : the smaller node
	 * @param large
	 *            : the bigger node
	 * @param parent
	 *            : their parent index node
	 * @return the splitkey position in parent if merged so that parent can
	 *         delete the splitkey later on. -1 otherwise
	 */
	public int handleIndexNodeUnderflow(IndexNode<K,T> small,
			IndexNode<K,T> large, IndexNode<K,T> parent) {
		int index=-1;
		//merge
		if(small.keys.size()+large.keys.size()<=2*D){	
			K split;
			//small is on the left
			if(parent.children.indexOf(small)<parent.children.indexOf(large)){
				split = parent.keys.get(parent.children.indexOf(small));
				small.keys.add(split);
				for(int i=0; i<large.keys.size(); i++){
					small.keys.add(large.keys.get(i));
				}
				for(int i=0; i<large.children.size(); i++){
					small.children.add(large.children.get(i));
				}
				large.keys.clear();
				large.children.clear();
				parent.children.remove(large);
				return parent.keys.indexOf(split);
				
			}
			//small is on the right
			else{
				split = parent.keys.get(parent.children.indexOf(large));
				large.keys.add(split);
				for(int i=0; i<small.keys.size(); i++){
					large.keys.add(small.keys.get(i));
				}
				for(int i=0; i<small.children.size(); i++){
					large.children.add(small.children.get(i));
				}
				small.keys.clear();
				small.children.clear();
				parent.children.remove(small);
				return parent.keys.indexOf(split);
			}
			
		}
		//redistribute
		else{
			int par;
			//small is on the left
			if(parent.children.indexOf(small)<parent.children.indexOf(large)){
				par=parent.children.indexOf(small);
				while(small.keys.size()!=large.keys.size()){
					small.keys.add(parent.keys.get(par));
					small.children.add(large.children.get(0));
					parent.keys.remove(par);
					parent.keys.add(par, large.keys.get(0));
					large.keys.remove(0);
					large.children.remove(0);
				}
				return -1;
				
			}
			//small is on the right
			else{
				par=parent.children.indexOf(large);
				while(small.keys.size()!=large.keys.size()){
					small.keys.add(parent.keys.get(par));
					small.children.add(large.children.get(large.children.size()-1));
					parent.keys.remove(par);
					parent.keys.add(par, large.keys.get(large.keys.size()-1));
					large.keys.remove(large.keys.size()-1);
					large.children.remove(large.children.size()-1);
				}
				return -1;
				
			}
		}
	}
}
