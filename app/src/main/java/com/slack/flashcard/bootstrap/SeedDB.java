package com.slack.flashcard.bootstrap;

import android.content.Context;

import com.slack.flashcard.domain.model.Card;
import com.slack.flashcard.domain.model.CardSet;
import com.slack.flashcard.domain.repositories.CardRepository;
import com.slack.flashcard.domain.repositories.CardSetRepository;
import com.slack.flashcard.domain.services.CardService;
import com.slack.flashcard.domain.services.CardSetService;

import java.util.ArrayList;
import java.util.List;

public class SeedDB {

    private CardSetRepository cardSetRepository;
    private CardRepository cardRepository;

    private class CardAQPair {

        private String question, answer;

        private CardAQPair(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }
    }

    public SeedDB(Context context) {
        cardSetRepository = new CardSetService(context);
        cardRepository = new CardService(context);

        addCTCICardSet();
        addSortAlgsCardSet();
    }

    private void createCardSet(String cardSetTitle, List<CardAQPair> cards) {
        // Create CardSet
        CardSet cardSet = new CardSet(cardSetTitle);
        // Get id from db when adding
        String cardSetId = String.valueOf(cardSetRepository.addCardSet(cardSet));
        // Add cards
        for (CardAQPair qA : cards ) {
            cardRepository.addCard(new Card(cardSetId, qA.question, qA.answer));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // CardSets
    private void addCTCICardSet() {
        List<CardAQPair> cards = new ArrayList<>();
        cards.add(new CardAQPair(
                "String has unique characters?",
                "Use a Set<char>, if !contains(charValue) -> insert(charValue), else " +
                        "return false."));
        cards.add(new CardAQPair(
                "String is permutation of another?",
                "Use characterCount Maps, if map1.equals(map2) -> true.\n\nalt: " +
                        "Strings.toCharArray(), charArrays.sort(), sorted1.equals(sorted2)."));
        cards.add(new CardAQPair(
                "URLify String (replace space with '%20')?",
                "String.replaceAll(\"\\\\s+\",\"\") to calculate whitespace. Use char[] " +
                        "with new calculated size, start filling from end with either the char or " +
                        "the needed sequence."));
        cards.add(new CardAQPair(
                "String is permutation of palindrome?",
                "Can have at most 1 odd character. Use int[128] and tally char counts. " +
                        "Use boolean oddEncountered flag and iterate through array, if isOdd && " +
                        "oddEncountered -> return false."));
        cards.add(new CardAQPair(
                "1 Away (one insert, remove, or replace allowed)?",
                "Use boolean oneAway flag.\nIf lengths == -> iterate both, if a!=b " +
                        "set oneAway true, if already true -> return false.\nIf lengths != -> " +
                        "assign long and short, iterate, if a!=b set oneAway true and increment " +
                        "long, if already true -> return false."));
        cards.add(new CardAQPair(
                "String compression (ssssabbb -> s4ab3)?",
                "Use StringBuilder and int counter. while char == nextChar -> increment " +
                        "counter, append char or char+counter. Return shorter of input string and " +
                        "sb."));
        cards.add(new CardAQPair(
                "Rotate a matrix 90 degrees?",
                "Create new type[][]. Reverse columns to get new rows. Use helper method " +
                        "to get a reversed column and assign type[] to new rows"));
        cards.add(new CardAQPair(
                "Zero matrix (if '0' flip row and column)?",
                "Use custom class Point(x,y) and find all zeroes. For all points flip row " +
                        "and column.\n(Could be beneficial to track indices to avoid already " +
                        "flipped)"));
        cards.add(new CardAQPair(
                "String is rotation of another (rotation <-> tationro)?",
                "String b = input + input -> if input.contains(b) -> true. " +
                        "(tationrotationro contains rotation)"));
        cards.add(new CardAQPair(
                "Remove duplicates from linked list?",
                "Use Set, lead and follow pointers. If set.contains(lead.value) -> " +
                        "follow.next = lead.next."));
        cards.add(new CardAQPair(
                "Get kth to last element in a linked list?",
                "Use 2 pointers. Set runner out k nodes, then iterate both. Once runner " +
                        "hits end -> return follow pointer value."));
        cards.add(new CardAQPair(
                "Remove referenced node from linked list?",
                "Simply copy next.value and next.next to this referenced node."));
        cards.add(new CardAQPair(
                "Partition linked list around P?",
                "Create new Node pointers for head and tail sections and iterate over input " +
                        "list. If >= P, attach to tail, else to head. When done iterating attach " +
                        "tail to head."));
        cards.add(new CardAQPair(
                "Sum lists (nodes are decimal places - reversed)?",
                "Use new LL for result. Use sum and prevCarry. Iterate, add, and carry."));
        cards.add(new CardAQPair(
                "Linked list is palindrome?",
                "If doubly linked walk from front and back until they meet.\n\nElse copy " +
                        "into StringBuilder, sb.reverse() and check if 1st half of substrings are equal."));
        cards.add(new CardAQPair(
                "Find linked list intersection if exists?",
                "Use custom class LengthAndTail -> Calculate lAT for both. If aTail != " +
                        "bTail return false.\nSet pointer on longer list diffLength ahead. Then " +
                        "iterate both until aNode == bNode. Since the tails are the same node we " +
                        "know they intersect and once the pointers point to the same node we know " +
                        "we have the intersection."));
        cards.add(new CardAQPair(
                "Linked list loop?",
                "Use 2 pointers, n and runner, an int runCount, and a new LL to populate " +
                        "while iterating. Advance pointers (n by 1 and runner by 2) while n != " +
                        "runner. (If there is a loop we can never find the end...duh). Once n == " +
                        "runner -> stop iterating/populating new LL, the start of the loop is " +
                        "runCount indices from the end of the loop. Return Node LL.get(LL.size() - " +
                        "runCount)"));
        cards.add(new CardAQPair(
                "3 stacks in 1 Array?",
                "Use int[] of stack sizes to keep track of top of each stack. Use helper " +
                        "method with offset to get indexOfTop(int stackNumber) into main array indices."));
        cards.add(new CardAQPair(
                "MinStack (Always be able to return min value - not pop())?",
                "Use additional stack of min values. On push, if new < current min -> push " +
                        "on both.\nOn pop, if popping min.peek() -> pop both."));
        cards.add(new CardAQPair(
                "Stack of plates (If plates get too high start new stack)?",
                "Use List<Stack> and int size to track total plates.\nOn push, if ++size % " +
                        "capacity == 1, add new stack to list.\nOn pop, if isEmpty() -> remove " +
                        "stack.\n*stackNum is (size-1) / capacity"));
        cards.add(new CardAQPair(
                "Queue via stacks?",
                "Use 2 stacks to shift, jobStack and shiftStack. Use lazy shift.\nOn add, if " +
                        "!shiftStack.isEmpty() -> shift to jobStack and push().\nOn remove and " +
                        "peek, if not jobStack.isEmpty() -> shift to shiftStack and peek() or pop()."));
        cards.add(new CardAQPair(
                "Sort a stack using 1 additional stack?",
                "Use shiftStack ss and int temp.\nOuter while: src stack not empty, temp = " +
                        "src.pop(), Nested while: !ss.isEmpty() && ss.peek() > temp, push ss back " +
                        "on src. Back in outer while: then push temp onto ss (it's ordered " +
                        "position).\nSs is now in reverse order so pushing all elements back to src " +
                        "gives the sorted stack."));
        cards.add(new CardAQPair(
                "Animal queue (Dequeue the oldest animal, dog or cat)?",
                "Class AnimalQueue has a CatQueue and a DogQueue and a static int numIn " +
                        "(passed to Dog and Cat constructors) indicating 'age' of animal.\n\nOn " +
                        "enqueue(Animal) simply check instanceOf to enqueue in proper list(queue)." +
                        "\nOn dequeue must check for age and empty lists."));
        cards.add(new CardAQPair(
                "Route exists between graph nodes (reachable, not the actual route)?",
                "Use 2 linked lists: reachable and visited. Add start node to visited and " +
                        "start.neighbors to reachable. Iterate through reachable using same " +
                        "process.\nIf finish is ever in reachable return true."));
        cards.add(new CardAQPair(
                "Minimal BST from sorted array?",
                "The root node of any sub-array is always in the middle.\nUse recursion, " +
                        "signature: BTNode createMinTree(int[] src, int start, int finish){}.\n" +
                        "Base cases: if start > finish ret null, and if start == finish ret new " +
                        "BTNode(src[start]).\nBTNode newNode from mid (start-finish / 2).\nnewNode." +
                        "left = cMT(src, start, mid-1) and .right = cMT(src, mid+1, finish).\nReturn" +
                        " newNode." ));
        cards.add(new CardAQPair(
                "List of depths (From tree, list of linked lists of nodes at each depth)?",
                "Use recursion, signature: void lOD(Node root, int depth, LL<LL<Node>>>> " +
                        "lists){}\nBase case: if root == null ret.\nLL list = null. If lists.size() == " +
                        "depth we are at this depth for the first time and list = new LL, else " +
                        "lists.get(depth).\nNow we have the needed list, add root and recurse with " +
                        "root.left and right and depth+1."));
        cards.add(new CardAQPair(
                "Tree isBalanced (sub-tree diff <= 1)?",
                "Use recursive helper to getHeight from a Node. Signature: int getHeight" +
                        "(Node root){}.\nBase case: if root == null ret -1.\nOtherwise return Math." +
                        "max(gH(root.left), gH(root.right)).\n\nMain problem also uses recursion. " +
                        "Signature: boolean isBalanced(Node root){}.\nBase case: if root == null " +
                        "ret true.\nOtherwise, int depthDiff = Math.abs(gh(left), gh(right). If " +
                        "depthDiff > 1 ret false. Else ret isB(left) && isB(right)."));
        cards.add(new CardAQPair(
                "Validate tree is BST?",
                "Use recursion. Signature: boolean isBST(Node root, int min, int max){}.\n" +
                        "Base case: if root == null ret true.\nOtherwise, if data > max or < min " +
                        "ret false.\nRet isBST(left, min, data) && isBST(right, data, max)."));
        cards.add(new CardAQPair(
                "Find inOrder successor to Node n?",
                "If n has a right child, successor s is the left-most child of said right " +
                        "child. So, if right != null, s = right, then while s.left != null, s = " +
                        "s.left, case done.\n\nElse, we have to climb the tree until n is no longer " +
                        "the right child. So, while: n.parent != null && n.parent.data < n.data, " +
                        "n = n.parent. If n.parent == null ret null (no inOrder successor), s = " +
                        "n.parent.\n\nRet s."));
        cards.add(new CardAQPair(
                "Print build order from given dependencies?",
                "Create a graph from dependencies. While: graph.size() != 0, pick a node " +
                        "with 0 neighbors, print and remove.\n\nRemember to remove node and " +
                        "dependencies from remaining nodes in graph."));
        cards.add(new CardAQPair(
                "First common ancestor (in BinTree, not BST)?",
                "Use LL<Node> pathNodes. Traverse up from a and b while .parent != null, if " +
                        "pathNodes ever contains current node n ret n.\n\nAlt: No additional ds. " +
                        "Get depths of a and b, move deeper up depthDiff parents, ret a==b?a:null;."));
        createCardSet("CTCI", cards);
    }

    private void addSortAlgsCardSet() {
        List<CardAQPair> cards = new ArrayList<>();
        cards.add(new CardAQPair(
                "Selection sort algorithm and complexity?",
                "Repeatedly find smallest element and swap to end of sorted sub-array." +
                        "\nUseful when memory writes are expensive. Never more than n swaps." +
                        "\n\nTime: O(n^2), Space: O(1)."));
        cards.add(new CardAQPair(
                "Insertion sort algorithm and complexity?",
                "Take first index in unsorted sub-array and move into correct postion in " +
                        "sorted. Requires shifting of all elements in sorted that are > the key" +
                        "\n\nTime: O(n^2), Space: O(1)."));
        cards.add(new CardAQPair(
                "Bubble sort algorithm and complexity?",
                "Iterate and repeatedly swap adjacent elements if they are in the wrong " +
                        "order." +
                        "\n\nTime: O(n^2), Space: O(1)."));
        cards.add(new CardAQPair(
                "Merge sort algorithm and complexity?",
                "Recursive divide and conquer algorithm. Divides input arrays in half and " +
                        "merges sorted sub-arrays.\nsort(arr[],l,r){ sort(arr,l,m); sort(arr,m+1,r); " +
                        "merge(arr,l,m,r); }" +
                        "\n\nTime: O(n log n), Space: O(n)."));
        cards.add(new CardAQPair(
                "Heap sort algorithm and complexity?",
                "Uses a binary heap. Repeatedly pull top element into sorted array and " +
                        "heapify until heap.size()==0." +
                        "\n\nTime: O(n log n) log n for heapify n times, Space: O(1)."));
        cards.add(new CardAQPair(
                "Quick sort algorithm and complexity?",
                "Recursive divide and conquer algorithm. Pick a pivot and partition elements " +
                        "on either side of the pivot. Recurse on partitions." +
                        "\nsort(arr[],l,h){ pi=partition(arr,l,h); sort(arr,l,pi-1); sort(arr,pi+1,h); }" +
                        "\n\nTime: O(n log n), Space: O(1)."));
        cards.add(new CardAQPair(
                "Bucket sort algorithm and complexity?",
                "Used for floating point numbers e.g. Create n buckets of lists. Insert each " +
                        "element into appropriate bucket (0.17 into bucket 1, 0.23 into bucket 2,...). " +
                        "Sort buckets using insertion sort." +
                        "\n\nTime: O(n + k), Space: O(n) for the bucket-lists."));
        cards.add(new CardAQPair(
                "Radix sort algorithm and complexity?",
                "Sorts numbers using counting sort by digits, starting with least " +
                        "significant digit." +
                        "\n\nTime: O(nk), Space: O(n + k)."));
        cards.add(new CardAQPair(
                "Bubble sort complexity. Best, Ave, Worst and Space?",
                "Best: O(n)\nAve: O(n^2)\nWorst: O(n^2)\nSpace: O(1)"));
        cards.add(new CardAQPair(
                "Selection sort complexity. Best, Ave, Worst and Space?",
                "Best: O(n^2)\nAve: O(n^2)\nWorst: O(n^2)\nSpace: O(1)"));
        cards.add(new CardAQPair(
                "Insertion sort complexity. Best, Ave, Worst and Space?",
                "Best: O(n)\nAve: O(n^2)\nWorst: O(n^2)\nSpace: O(1)"));
        cards.add(new CardAQPair(
                "Merge sort complexity. Best, Ave, Worst and Space?",
                "Best: O(n log n)\nAve: O(n log n)\nWorst: O(n log n)\nSpace: O(n)"));
        cards.add(new CardAQPair(
                "Quick sort complexity. Best, Ave, Worst and Space?",
                "Best: O(n log n)\nAve: O(n log n)\nWorst: O(n^2)\nSpace: O(n log n)"));
        cards.add(new CardAQPair(
                "Heap sort complexity. Best, Ave, Worst and Space?",
                "Best: O(n log n)\nAve: O(n log n)\nWorst: O(n log n)\nSpace: O(n)"));
        cards.add(new CardAQPair(
                "Bucket sort complexity. Best, Ave, Worst and Space?",
                "Best: O(n + k)\nAve: O(n + k)\nWorst: O(n^2)\nSpace: O(n)"));
        cards.add(new CardAQPair(
                "Radix sort complexity. Best, Ave, Worst and Space?",
                "Best: O(nk)\nAve: O(nk)\nWorst: O(nk)\nSpace: O(n + k)"));
        createCardSet("Sorting Algorithms", cards);
    }

}
