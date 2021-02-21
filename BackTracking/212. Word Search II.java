/*
https://leetcode.com/problems/word-search-ii/
Given an m x n board of characters and a list of strings words, return all words on the board.

Each word must be constructed from letters of sequentially adjacent cells, where adjacent cells are horizontally or vertically neighboring. The same letter cell may not be used more than once in a word.

 

Example 1:


Input: board = [["o","a","a","n"],["e","t","a","e"],["i","h","k","r"],["i","f","l","v"]], words = ["oath","pea","eat","rain"]
Output: ["eat","oath"]
Example 2:


Input: board = [["a","b"],["c","d"]], words = ["abcb"]
Output: []
 

Constraints:

m == board.length
n == board[i].length
1 <= m, n <= 12
board[i][j] is a lowercase English letter.
1 <= words.length <= 3 * 104
1 <= words[i].length <= 10
words[i] consists of lowercase English letters.
All the strings of words are unique.

*/
import java.util.*;
class TrieNode {
    Map<Character, TrieNode> children;
    String word;
    public TrieNode() {
        children = new HashMap<>();
        word = null;
    }
}
class Solution {
    private static int[][] DIRECTIONS = new int[][]{{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
    private boolean isValid(char[][] board, int row, int col) {
        int rows = board.length;
        int cols = board[0].length;
        return (row >= 0 && row < rows && col >= 0 && col < cols);
    }
    private void backtracking(char[][] board, int curRow, int curCol, TrieNode node, List<String> res, boolean[][] visited) {//node 代表当前已经找到的当前node，找到的位置在[i, j]
        if (node.word != null) {
            res.add(node.word);
            node.word = null;//这里一定要把word设成null，不然它dfs的时候，走完孩子走回这个节点的时候，还会再添加一次
        }
        //choices: up, down, left, right
        for (int[] direction : DIRECTIONS) {
            int nextRow = curRow + direction[0];
            int nextCol = curCol + direction[1];
            for (char c : node.children.keySet()) {
                if (isValid(board, nextRow, nextCol) && !visited[nextRow][nextCol] && board[nextRow][nextCol] == c) {
                    visited[nextRow][nextCol] = true;
                    backtracking(board, nextRow, nextCol, node.children.get(c), res, visited);
                    visited[nextRow][nextCol] = false;
                }
            }
        }
    }
    private void insert(TrieNode root, String word) {
        TrieNode cur = root;
        for (char c : word.toCharArray()) {
            if (!cur.children.containsKey(c)) {
                cur.children.put(c, new TrieNode());
            }
            cur = cur.children.get(c);
        }
        cur.word = word;
    }
    public List<String> findWords(char[][] board, String[] words) {
        int rows = board.length;
        int cols = board[0].length;
        List<String> res = new ArrayList<>();
        boolean[][] visited = new boolean[rows][cols];
        TrieNode root = new TrieNode();
        //initialize trie
        for (String word : words) {
            insert(root, word);
        }
        for (char c : root.children.keySet()) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (board[i][j] == c) {
                        visited[i][j] = true;
                        backtracking(board, i, j, root.children.get(c), res, visited);
                        visited[i][j] = false;
                    }
                }
            }
        }
        return res;
    }
}