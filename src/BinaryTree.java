import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Arrays;

public class BinaryTree {

    TreeNode root;

    public BinaryTree() {
        this.root = null;
    }

    public BinaryTree(TreeNode root) {
        this.root = root;
    }

    /*
    IMPORTANTE: respete EXACTAMENTE los nombres y firmas de los metodos que se
    piden abajo; la revision automatica los busca por nombre. Puede agregar
    todos los metodos auxiliares (privados) que necesite.
    */

    /*
    1. Construccion del arbol: implemente aqui

    public void insertRoot(Object data)
        Si el arbol esta vacio, crea la raiz con 'data'.
    */
    public void insertRoot(Object data) {
        if (this.root == null && data != null) {
            this.root = new TreeNode(data);
        }
    }
    /*
    public TreeNode search(Object data)
        Devuelve el nodo cuyo dato es igual (equals) a 'data', o null si no
        existe.
    */
    public TreeNode search(Object data) {
        if (data == null)
            return null;
        return searchRecursive(this.root, data);
    }
    private TreeNode searchRecursive(TreeNode current, Object data) {
        if (current == null)
            return null;
        if (current.data != null && current.data.equals(data)) {
            return current;
        }
        TreeNode foundLeft = searchRecursive(current.left, data);
        if (foundLeft != null)
            return foundLeft;
        return searchRecursive(current.right, data);
    }
    /*
    public boolean insertLeft(Object parent, Object data)
        Agrega 'data' como hijo izquierdo del nodo cuyo dato es 'parent'.
        Devuelve false (y no modifica nada) si 'parent' no existe o si ya
        tiene hijo izquierdo; true en otro caso.
    */
    public boolean insertLeft(Object parent, Object data) {
        if (parent == null || data == null)
            return false;
        TreeNode parentNode = search(parent);
        if (parentNode == null || parentNode.left != null) {
            return false;
        }
        parentNode.left = new TreeNode(data);
        return true;
    }
    /*
    public boolean insertRight(Object parent, Object data)
        Igual que insertLeft, pero con el hijo derecho.
    */
    public boolean insertRight(Object parent, Object data) {
        if (parent == null || data == null)
            return false;
        TreeNode parentNode = search(parent);
        if (parentNode == null || parentNode.right != null) {
            return false;
        }
        parentNode.right = new TreeNode(data);
        return true;
    }
    /*
    public void buildTree(Scanner scanner)
        Construye el arbol leyendo los datos con 'scanner' (en el main se le
        pasa new Scanner(System.in)). Primero se lee la raiz y luego, por
        niveles, el hijo izquierdo y el hijo derecho de cada nodo
        ("Hijo izquierdo de A: ", "Hijo derecho de A: ", ...). Una linea
        vacia o "-" significa que ese hijo no existe.
    */
    public void buildTree(Scanner scanner) {
        this.root = null;
        System.out.print("Raiz: ");
        if (!scanner.hasNextLine())
            return;
        String rootInput = scanner.nextLine().trim();

        if (rootInput.isEmpty() || rootInput.equals("-")) {
            return;
        }

        insertRoot(rootInput);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(this.root);

        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();

            System.out.print("Hijo izquierdo de " + current.data + ": ");
            if (scanner.hasNextLine()) {
                String leftInput = scanner.nextLine().trim();
                if (!leftInput.isEmpty() && !leftInput.equals("-")) {
                    current.left = new TreeNode(leftInput);
                    queue.add(current.left);
                }
            }

            System.out.print("Hijo derecho de " + current.data + ": ");
            if (scanner.hasNextLine()) {
                String rightInput = scanner.nextLine().trim();
                if (!rightInput.isEmpty() && !rightInput.equals("-")) {
                    current.right = new TreeNode(rightInput);
                    queue.add(current.right);
                }
            }
        }
    }
    /*
    2. Visualizacion: implemente aqui

    public void printTree()
        Muestra el arbol en consola de forma grafica, indicando claramente
        la relacion entre padres e hijos.
    */
    public void printTree() {
        dibujarArbol(false);
    }
    private int countNodes(TreeNode node) {
        if (node == null) return 0;
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    private void dibujarArbol(boolean mostrarFB) {
        if (root == null) {
            System.out.println("(Árbol vacío)");
            return;
        }
        int alto = height(root);
        int filas = alto * 2 - 1;
        int ancho = countNodes(root) * 7;

        char[][] lienzo = new char[filas][ancho];
        for (char[] fila : lienzo) Arrays.fill(fila, ' ');

        int[] contador = {0};
        dibujarRecursivo(root, 0, lienzo, contador, mostrarFB);

        for (char[] fila : lienzo) {
            System.out.println(new String(fila).replaceAll("\\s+$", ""));
        }
    }

    private int dibujarRecursivo(TreeNode node, int nivel, char[][] lienzo, int[] contador, boolean mostrarFB) {
        int colIzq = -1, colDer = -1;

        if (node.left != null) colIzq = dibujarRecursivo(node.left, nivel + 1, lienzo, contador, mostrarFB);

        int col = contador[0]++ * 7;
        String texto = String.valueOf(node.data);
        if (mostrarFB) texto += "(" + balanceFactor(node) + ")";

        for (int i = 0; i < texto.length() && col + i < lienzo[0].length; i++) {
            lienzo[nivel * 2][col + i] = texto.charAt(i);
        }

        if (node.right != null) colDer = dibujarRecursivo(node.right, nivel + 1, lienzo, contador, mostrarFB);

        if (colIzq != -1) lienzo[nivel * 2 + 1][(col + colIzq) / 2] = '/';
        if (colDer != -1) lienzo[nivel * 2 + 1][(col + colDer) / 2 + 1] = '\\';

        return col;
    }
    /*
    3. Recorridos: implemente aqui. Cada recorrido debe ser una funcion
    RECURSIVA independiente (por ejemplo, un metodo privado que reciba un
    TreeNode). Los metodos publicos devuelven la secuencia separada por
    espacios, p.ej. "A B D H I E C F G".

    public String preorder()
    */
    public String preorder() {
        StringBuilder sb = new StringBuilder();
        preorderRecursive(root, sb);
        return sb.toString().trim();
    }
    private void preorderRecursive(TreeNode node, StringBuilder sb) {
        if (node == null)
            return;
        sb.append(node.data).append(" ");
        preorderRecursive(node.left, sb);
        preorderRecursive(node.right, sb);
    }
    /*
    public String inorder()
    */
    public String inorder() {
        StringBuilder sb = new StringBuilder();
        inorderRecursive(root, sb);
        return sb.toString().trim();
    }

    private void inorderRecursive(TreeNode node, StringBuilder sb) {
        if (node == null)
            return;
        inorderRecursive(node.left, sb);
        sb.append(node.data).append(" ");
        inorderRecursive(node.right, sb);
    }
    /*
    public String postorder()
    */
    public String postorder() {
        StringBuilder sb = new StringBuilder();
        postorderRecursive(root, sb);
        return sb.toString().trim();
    }

    private void postorderRecursive(TreeNode node, StringBuilder sb) {
        if (node == null)
            return;
        postorderRecursive(node.left, sb);
        postorderRecursive(node.right, sb);
        sb.append(node.data).append(" ");
    }
    /*
    4. Factores de equilibrio: implemente aqui

    public int height(TreeNode node)
        Altura del subarbol que empieza en 'node' (null tiene altura 0 y una
        hoja altura 1).
    */
    public int height(TreeNode node) {
        if (node == null)
            return 0;
        int leftHeight = height(node.left);
        int rightHeight = height(node.right);
        return 1 + (leftHeight > rightHeight ? leftHeight : rightHeight);
    }
    /*
    public int balanceFactor(TreeNode node)
        Factor de equilibrio de 'node': height(node.right) - height(node.left).
    */
    public int balanceFactor(TreeNode node) {
        if (node == null)
            return 0;
        return height(node.right) - height(node.left);
    }
    /*
    public boolean isBalanced()
        true si TODOS los nodos tienen factor de equilibrio -1, 0 o 1.
    */
    public boolean isBalanced() {
        return isBalancedRecursive(this.root);
    }

    private boolean isBalancedRecursive(TreeNode node) {
        if (node == null)
            return true;
        int bf = balanceFactor(node);
        if (bf < -1 || bf > 1)
            return false;
        return isBalancedRecursive(node.left) && isBalancedRecursive(node.right);
    }
    /*
    public List<Object> unbalancedNodes()
        Lista con los datos de los nodos cuyo factor de equilibrio es menor
        que -1 o mayor que 1 (los que requieren balanceo).
    */
    public List<Object> unbalancedNodes() {
        List<Object> list = new ArrayList<>();
        collectUnbalanced(this.root, list);
        return list;
    }

    private void collectUnbalanced(TreeNode node, List<Object> list) {
        if (node == null)
            return;
        collectUnbalanced(node.left, list);
        int bf = balanceFactor(node);
        if (bf < -1 || bf > 1) {
            list.add(node.data);
        }
        collectUnbalanced(node.right, list);
    }
    /*
    public void printBalanceFactors()
        Muestra el arbol en consola indicando en cada nodo su factor de
        equilibrio.
    */
    public void printBalanceFactors() {
        if (this.root == null) {
            System.out.println("(Árbol vacío)");
            return;
        }
        dibujarArbol(true);
    }
    /*
    5. Balanceo AVL: implemente aqui. Cada rotacion recibe la raiz del
    subarbol desbalanceado y devuelve la NUEVA raiz de ese subarbol.

    public TreeNode rotateRight(TreeNode node)       rotacion simple a la derecha
    */
    public TreeNode rotateRight(TreeNode node) {
        if (node == null || node.left == null) return node;
        TreeNode newRoot = node.left;
        TreeNode temp = newRoot.right;

        newRoot.right = node;
        node.left = temp;

        return newRoot;
    }
    /*
    public TreeNode rotateLeft(TreeNode node)        rotacion simple a la izquierda
    */
    public TreeNode rotateLeft(TreeNode node) {
        if (node == null || node.right == null) return node;
        TreeNode newRoot = node.right;
        TreeNode temp = newRoot.left;

        newRoot.left = node;
        node.right = temp;

        return newRoot;
    }
    /*
    public TreeNode rotateLeftRight(TreeNode node)   rotacion doble izquierda-derecha
    */
    public TreeNode rotateLeftRight(TreeNode node) {
        if (node == null || node.left == null) return node;
        node.left = rotateLeft(node.left);
        return rotateRight(node);
    }
    /*
    public TreeNode rotateRightLeft(TreeNode node)   rotacion doble derecha-izquierda
    */
    public TreeNode rotateRightLeft(TreeNode node) {
        if (node == null || node.right == null) return node;
        node.right = rotateRight(node.right);
        return rotateLeft(node);
    }
    /*
    public void balance()
        Identifica los nodos desbalanceados y aplica las rotaciones necesarias
        hasta que el arbol sea AVL (actualiza 'root' si cambia). Debe mostrar
        el arbol con sus factores de equilibrio antes y despues de balancear.
    */
    public void balance() {
        if (this.root == null) {
            System.out.println("(Árbol vacío)");
            return;
        }

        System.out.println("\n--- Árbol original con factores de equilibrio ---");
        printBalanceFactors();

        List<Object> unbalanced = unbalancedNodes();
        if (unbalanced.isEmpty()) {
            System.out.println("\nEl árbol ya se encuentra balanceado (es AVL).");
            return;
        }

        System.out.println("\nNodos que requieren balanceo: " + unbalanced);

        int intentos = 0;
        while (!isBalanced() && intentos < 50) {
            this.root = balanceRecursive(this.root);
            intentos++;
        }

        System.out.println("\n--- Árbol balanceado (AVL) con factores actualizados ---");
        printBalanceFactors();
    }

    private TreeNode balanceNode(TreeNode node) {
        int bf = balanceFactor(node);

        if (bf < -1) {
            if (balanceFactor(node.left) <= 0) {
                return rotateRight(node);
            } else {
                return rotateLeftRight(node);
            }
        }
        if (bf > 1) {
            if (balanceFactor(node.right) >= 0) {
                return rotateLeft(node);
            } else {
                return rotateRightLeft(node);
            }
        }
        return node;
    }

    private TreeNode balanceRecursive(TreeNode node) {
        if (node == null) return null;
        node.left = balanceRecursive(node.left);
        node.right = balanceRecursive(node.right);
        return balanceNode(node);
    }
}
