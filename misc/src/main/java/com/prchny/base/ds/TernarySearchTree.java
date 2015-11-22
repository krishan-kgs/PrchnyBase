/*
 *  Copyright 2011 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Jul 6, 2011
 *  @author singla
 */

package com.prchny.base.ds;

import java.util.ArrayList;
import java.util.List;

/**
 * @author singla Implementation of ternary search tree. A Ternary Search Tree
 *         is a data structure that behaves in a manner that is very similar to
 *         a HashMap.
 * @param <E>
 */
public class TernarySearchTree<E> {
  
  private TSTNode<E> rootNode;
  
  private int defaultNumReturnValues = -1;
  
  /**
   * Stores value in the TernarySearchTree. The value may be retrieved using
   * key.
   * 
   * @param key
   *          A string that indexes the object to be stored.
   * @param value
   *          The object to be stored in the tree.
   */
  public void put(final String key, final E value) {
  
    getOrCreateNode(key).addValue(value);
  }
  
  /**
   * Stores value in the TernarySearchTree. The value may be retrieved using
   * key.
   * 
   * @param key
   *          A string that indexes the object to be stored.
   * @param value
   *          The object to be stored in the tree.
   */
  public void putAllCombinations(final String key, final E value,
      final int minimumLength) {
  
    getOrCreateNode(key).addValue(value);
    for (int i = 1; i < ((key.length() - minimumLength) + 1); i++) {
      getOrCreateNode(key.substring(i)).addValue(value);
    }
  }
  
  public void freeze() {
  
    if (rootNode != null) {
      rootNode.freeze();
    }
  }
  
  /**
   * Retrieve the object indexed by key.
   * 
   * @param key
   *          A String index.
   * @return Object The object retrieved from the TernarySearchTree.
   */
  public List<E> get(final String key) {
  
    final TSTNode<E> node = getNode(key);
    if (node == null) {
      return null;
    }
    return node.data;
  }
  
  /**
   * Sets default maximum number of values returned from matchPrefix,
   * matchPrefixString, matchAlmost, and matchAlmostString methods.
   * 
   * @param num
   *          The number of values that will be returned when calling the
   *          methods above. Set this to -1 to get an unlimited number of return
   *          values. Note that the methods mentioned above provide overloaded
   *          versions that allow you to specify the maximum number of return
   *          values, in which case this value is temporarily overridden.
   */
  public void setNumReturnValues(final int num) {
  
    defaultNumReturnValues = (num < 0) ? -1 : num;
  }
  
  private int checkNumberOfReturnValues(final int numReturnValues) {
  
    return ((numReturnValues < 0) ? -1 : numReturnValues);
  }
  
  /**
   * Returns the Node indexed by key, or null if that node doesn't exist. Search
   * begins at root node.
   * 
   * @param key
   *          An index that points to the desired node.
   * @return TSTNode The node object indexed by key. This object is an instance
   *         of an inner class named TernarySearchTree.TSTNode.
   */
  public TSTNode<E> getNode(final String key) {
  
    return getNode(key, rootNode);
  }
  
  /**
   * Returns the Node indexed by key, or null if that node doesn't exist. Search
   * begins at root node.
   * 
   * @param key
   *          An index that points to the desired node.
   * @param startNode
   *          The top node defining the subtree to be searched.
   * @return TSTNode The node object indexed by key. This object is an instance
   *         of an inner class named TernarySearchTree.TSTNode.
   */
  protected TSTNode<E> getNode(final String key, final TSTNode<E> startNode) {
  
    if ((key == null) || (startNode == null) || (key.length() == 0)) {
      return null;
    }
    TSTNode<E> currentNode = startNode;
    int charIndex = 0;
    
    while (true) {
      if (currentNode == null) {
        return null;
      }
      final int charComp =
          compareCharsAlphabetically(key.charAt(charIndex),
              currentNode.splitchar);
      
      if (charComp == 0) {
        charIndex++;
        if (charIndex == key.length()) {
          return currentNode;
        }
        currentNode = currentNode.relatives[TSTNode.EQKID];
      } else if (charComp < 0) {
        currentNode = currentNode.relatives[TSTNode.LOKID];
      } else {
        // charComp must be greater than zero
        currentNode = currentNode.relatives[TSTNode.HIKID];
      }
    }
  }
  
  /**
   * @param prefix
   * @return List of all the nodes matching the prefix
   */
  public List<E> matchPrefix(final String prefix) {
  
    return matchPrefix(prefix, defaultNumReturnValues);
  }
  
  /**
   * @param prefix
   * @param numReturnValues
   * @return
   */
  public List<E> matchPrefix(final String prefix, final int numReturnValues) {
  
    int sortKeysNumReturnValues = checkNumberOfReturnValues(numReturnValues);
    final List<E> sortKeysResult = new ArrayList<E>();
    final TSTNode<E> startNode = getNode(prefix);
    
    if (startNode == null) {
      return sortKeysResult;
    }
    if (startNode.data != null) {
      sortKeysResult.addAll(startNode.data);
      sortKeysNumReturnValues--;
    }
    
    sortKeysRecursion(sortKeysResult, startNode.relatives[TSTNode.EQKID],
        sortKeysNumReturnValues);
    
    return sortKeysResult;
  }
  
  private void sortKeysRecursion(final List<E> sortKeysResult,
      final TSTNode<E> currentNode, int sortKeysNumReturnValues) {
  
    if (currentNode == null) {
      return;
    }
    sortKeysRecursion(sortKeysResult, currentNode.relatives[TSTNode.LOKID],
        sortKeysNumReturnValues);
    if (sortKeysNumReturnValues == 0) {
      return;
    }
    
    if (currentNode.data != null) {
      sortKeysResult.addAll(currentNode.data);
      sortKeysNumReturnValues--;
    }
    
    sortKeysRecursion(sortKeysResult, currentNode.relatives[TSTNode.EQKID],
        sortKeysNumReturnValues);
    sortKeysRecursion(sortKeysResult, currentNode.relatives[TSTNode.HIKID],
        sortKeysNumReturnValues);
  }
  
  protected List<E> sortKeys(final TSTNode<E> startNode,
      final int numReturnValues) {
  
    final int sortKeysNumReturnValues =
        checkNumberOfReturnValues(numReturnValues);
    final List<E> sortKeysResult = new ArrayList<E>();
    sortKeysRecursion(sortKeysResult, startNode, sortKeysNumReturnValues);
    return sortKeysResult;
  }
  
  /**
   * Returns the Node indexed by key, creating that node if it doesn't exist,
   * and creating any required. intermediate nodes if they don't exist.
   * 
   * @param key
   *          A string that indexes the node that is returned.
   * @return TSTNode The node object indexed by key. This object is an instance
   *         of an inner class named TernarySearchTree.TSTNode.
   */
  protected TSTNode<E> getOrCreateNode(final String key)
      throws NullPointerException, IllegalArgumentException {
  
    if (key == null) {
      throw new NullPointerException(
          "attempt to get or create node with null key");
    }
    if (key.length() == 0) {
      throw new IllegalArgumentException(
          "attempt to get or create node with key of zero length");
    }
    if (rootNode == null) {
      rootNode = new TSTNode<E>(key.charAt(0), null);
    }
    
    TSTNode<E> currentNode = rootNode;
    int charIndex = 0;
    while (true) {
      final int charComp =
          compareCharsAlphabetically(key.charAt(charIndex),
              currentNode.splitchar);
      
      if (charComp == 0) {
        charIndex++;
        if (charIndex == key.length()) {
          return currentNode;
        }
        if (currentNode.relatives[TSTNode.EQKID] == null) {
          currentNode.relatives[TSTNode.EQKID] =
              new TSTNode<E>(key.charAt(charIndex), currentNode);
        }
        currentNode = currentNode.relatives[TSTNode.EQKID];
      } else if (charComp < 0) {
        if (currentNode.relatives[TSTNode.LOKID] == null) {
          currentNode.relatives[TSTNode.LOKID] =
              new TSTNode<E>(key.charAt(charIndex), currentNode);
        }
        currentNode = currentNode.relatives[TSTNode.LOKID];
      } else {
        // charComp must be greater than zero
        if (currentNode.relatives[TSTNode.HIKID] == null) {
          currentNode.relatives[TSTNode.HIKID] =
              new TSTNode<E>(key.charAt(charIndex), currentNode);
        }
        currentNode = currentNode.relatives[TSTNode.HIKID];
      }
    }
  }
  
  /**
   * An inner class of TernarySearchTree that represents a node in the tree.
   */
  private static final class TSTNode<E> {
    
    protected static final int LOKID = 0, EQKID = 1, HIKID = 2; // index values
    
    // for accessing
    
    // relatives array
    protected char splitchar;
    
    @SuppressWarnings("unchecked")
    protected TSTNode<E>[] relatives = new TSTNode[3];
    
    protected ArrayList<E> data = null;
    
    protected TSTNode(final char splitchar, final TSTNode<E> parent) {
    
      this.splitchar = splitchar;
    }
    
    protected void addValue(final E value) {
    
      if (data == null) {
        data = new ArrayList<E>();
      }
      data.add(value);
    }
    
    protected void freeze() {
    
      if (data != null) {
        data.trimToSize();
      }
      if (relatives[LOKID] != null) {
        relatives[LOKID].freeze();
      }
      if (relatives[HIKID] != null) {
        relatives[HIKID].freeze();
      }
    }
  }
  
  private static int compareCharsAlphabetically(final char cCompare,
      final char cRef) {
  
    return alphabetizeChar(cCompare) - alphabetizeChar(cRef);
  }
  
  private static int alphabetizeChar(final char c) {
  
    if (c < 65) {
      return c;
    }
    if (c < 89) {
      return (2 * c) - 65;
    }
    if (c < 97) {
      return c + 24;
    }
    if (c < 121) {
      return (2 * c) - 128;
    }
    
    return c;
  }
  
  public static void main(final String[] args) {
  
    final TernarySearchTree<String> tree = new TernarySearchTree<String>();
    // tree.put("Rahul", "Rahul1");
    // tree.put("Rahul1", "Rahul2");
    // tree.put("1Rahul", "Rahul3");
    
    tree.putAllCombinations("Rahul", "Rahul1", 2);
    tree.putAllCombinations("New Delhi", "New Delhi", 2);
    tree.putAllCombinations("Bombayel", "Bombayel", 2);
    
    System.out.println(tree.matchPrefix("yel"));
  }
}
