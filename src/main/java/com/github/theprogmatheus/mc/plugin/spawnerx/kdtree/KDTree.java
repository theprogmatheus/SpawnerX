package com.github.theprogmatheus.mc.plugin.spawnerx.kdtree;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class KDTree<V> implements Iterable<KDNode<V>> {

    KDNode<V> root;

    /**
     * @param loc  - Localização para extrair a coordenada
     * @param axis - Eixo de extração (x=0, y=1, z=2)
     * @return o valor da coordenada de acordo com o eixo 'axis', x=0, y=1, z=2
     */
    double coordByAxis(Location loc, int axis) {
        return switch (axis % 3) {
            case 0 -> loc.getX();
            case 1 -> loc.getY();
            case 2 -> loc.getZ();
            default -> throw new IllegalArgumentException("Axis inválido.");
        };
    }

    /**
     * @param root - Raiz base para inserção
     * @param node - Node que deve ser inserido
     * @param axis - Eixo atual para inserção
     * @return O root da subárvore *modificada* após a inserção.
     */
    KDNode<V> insert(KDNode<V> root, KDNode<V> node, int axis) {
        if (root == null)
            return node;

        int currentAxis = axis % 3;

        if (coordByAxis(node.loc, currentAxis) < coordByAxis(root.loc, currentAxis)) {
            root.left = insert(root.left, node, axis + 1);
        } else {
            root.right = insert(root.right, node, axis + 1);
        }

        return root;
    }

    /**
     * @param node - Node que deve ser inserido
     * @return Node inserido na árvore.
     */
    public KDNode<V> insert(KDNode<V> node) {
        if (this.root == null)
            return this.root = node;
        this.root = insert(this.root, node, 0);
        return node;
    }

    /**
     * @param node - Node que deve ser removido
     */
    public void remove(KDNode<V> node) {
        if (node == null) return;
        this.remove(node.loc);
    }

    /**
     * @param loc - Localização do node que deve ser removido
     */
    public void remove(Location loc) {
        if (loc == null) return;
        this.root = removeRecursive(this.root, loc, 0);
    }

    /**
     * Encontra o nó com o valor mínimo para um dado eixo em uma subárvore.
     *
     * @param current     O nó atual da busca (raiz da subárvore).
     * @param searchAxis  O eixo pelo qual estamos buscando o valor mínimo (x=0, y=1, z=2).
     * @param currentAxis O eixo de corte do nó 'current' (varia com a profundidade da recursão).
     * @return O nó com o valor mínimo no searchAxis na subárvore.
     */
    private KDNode<V> findMin(KDNode<V> current, int searchAxis, int currentAxis) {
        if (current == null) {
            return null;
        }
        if (searchAxis == currentAxis % 3) {
            if (current.left == null) {
                return current;
            }
            return findMin(current.left, searchAxis, (currentAxis + 1));
        } else {
            KDNode<V> minNode = current;

            KDNode<V> leftMin = findMin(current.left, searchAxis, (currentAxis + 1));
            if (leftMin != null && coordByAxis(leftMin.loc, searchAxis) < coordByAxis(minNode.loc, searchAxis)) {
                minNode = leftMin;
            }

            KDNode<V> rightMin = findMin(current.right, searchAxis, (currentAxis + 1));
            if (rightMin != null && coordByAxis(rightMin.loc, searchAxis) < coordByAxis(minNode.loc, searchAxis)) {
                minNode = rightMin;
            }
            return minNode;
        }
    }

    /**
     * Encontra o nó com o valor máximo para um dado eixo em uma subárvore.
     * Necessário para o caso de um nó com apenas filho esquerdo ser removido.
     *
     * @param current     O nó atual da busca (raiz da subárvore).
     * @param searchAxis  O eixo pelo qual estamos buscando o valor máximo (x=0, y=1, z=2).
     * @param currentAxis O eixo de corte do nó 'current' (varia com a profundidade da recursão).
     * @return O nó com o valor máximo no searchAxis na subárvore.
     */
    private KDNode<V> findMax(KDNode<V> current, int searchAxis, int currentAxis) {
        if (current == null) {
            return null;
        }

        if (searchAxis == currentAxis % 3) {
            if (current.right == null) {
                return current;
            }
            return findMax(current.right, searchAxis, (currentAxis + 1));
        } else {
            KDNode<V> maxNode = current;

            KDNode<V> leftMax = findMax(current.left, searchAxis, (currentAxis + 1));
            if (leftMax != null && coordByAxis(leftMax.loc, searchAxis) > coordByAxis(maxNode.loc, searchAxis)) {
                maxNode = leftMax;
            }

            KDNode<V> rightMax = findMax(current.right, searchAxis, (currentAxis + 1));
            if (rightMax != null && coordByAxis(rightMax.loc, searchAxis) > coordByAxis(maxNode.loc, searchAxis)) {
                maxNode = rightMax;
            }
            return maxNode;
        }
    }


    /**
     * @param root      - Raiz base para remoção
     * @param targetLoc - Localização alvo da remoção
     * @param axis      - Eixo atual para remoção
     * @return Retorna o root corrigido sem o node removido
     */
    KDNode<V> removeRecursive(KDNode<V> root, Location targetLoc, int axis) {
        if (root == null) return null;

        int currentAxis = axis % 3;

        if (root.loc.equals(targetLoc)) {
            if (root.left == null && root.right == null) {
                return null;
            }

            KDNode<V> replacementNode;
            int nextAxis = (axis + 1);

            if (root.right != null) {
                replacementNode = findMin(root.right, currentAxis, nextAxis);
                root.loc = replacementNode.loc;
                root.value = replacementNode.value;
                root.right = removeRecursive(root.right, replacementNode.loc, nextAxis);
            } else {
                replacementNode = findMax(root.left, currentAxis, nextAxis);
                root.loc = replacementNode.loc;
                root.value = replacementNode.value;
                root.left = removeRecursive(root.left, replacementNode.loc, nextAxis);
            }
            return root;
        }

        if (coordByAxis(targetLoc, currentAxis) < coordByAxis(root.loc, currentAxis)) {
            root.left = removeRecursive(root.left, targetLoc, axis + 1);
        } else {
            root.right = removeRecursive(root.right, targetLoc, axis + 1);
        }

        return root;
    }

    /**
     * @param target - A localização alvo para procura do node mais próximo.
     * @return O node mais próximo da localização alvo.
     */
    public KDNode<V> searchNearest(Location target) {
        return searchNearest(root, target, 0, null);
    }

    /**
     * @param current - Node atual da procura
     * @param target  - Node alvo da procura
     * @param axis    - Eixo atual da procura
     * @param best    - O melhor node encontrado até agora
     * @return O node mais próximo da localização informada.
     */
    KDNode<V> searchNearest(KDNode<V> current, Location target, int axis, KDNode<V> best) {
        if (current == null) return best;

        double currentDist = current.loc.distanceSquared(target);
        double bestDist = (best == null) ? Double.MAX_VALUE : best.loc.distanceSquared(target);

        if (currentDist < bestDist) {
            best = current;
        }

        int currentAxis = axis % 3;

        boolean leftFirst = coordByAxis(target, currentAxis) < coordByAxis(current.loc, currentAxis);

        KDNode<V> firstBranch = leftFirst ? current.left : current.right;
        KDNode<V> secondBranch = leftFirst ? current.right : current.left;

        best = searchNearest(firstBranch, target, axis + 1, best);

        double axisDist = Math.pow(coordByAxis(target, currentAxis) - coordByAxis(current.loc, currentAxis), 2);
        if (axisDist < best.loc.distanceSquared(target)) {
            best = searchNearest(secondBranch, target, axis + 1, best);
        }

        return best;
    }

    /**
     * Busca todos os nós contidos dentro de uma distância (raio) a partir de uma localização central.
     * A busca utiliza distância Euclidiana em 3D e realiza a verificação usando distância ao quadrado
     * para evitar cálculos caros de raiz quadrada.
     *
     * @param center A localização central para realizar a busca.
     * @param radius O raio de alcance da busca (em blocos).
     * @return Uma lista contendo os nós da árvore localizados dentro do raio especificado.
     */
    public List<KDNode<V>> rangeSearch(Location center, double radius) {
        List<KDNode<V>> results = new ArrayList<>();
        rangeSearchRecursive(this.root, center, radius * radius, 0, results);
        return results;
    }

    /**
     * Método auxiliar recursivo que percorre a árvore para encontrar todos os nós
     * dentro de uma distância ao quadrado a partir da localização central.
     *
     * @param node          O nó atual da árvore.
     * @param center        A localização central da busca.
     * @param radiusSquared O valor da distância ao quadrado para comparação.
     * @param axis          O eixo atual da comparação (0 = X, 1 = Y, 2 = Z).
     * @param results       A lista onde os nós válidos serão acumulados.
     */
    private void rangeSearchRecursive(KDNode<V> node, Location center, double radiusSquared, int axis, List<KDNode<V>> results) {
        if (node == null) return;

        double distSquared = node.loc.distanceSquared(center);
        if (distSquared <= radiusSquared) {
            results.add(node);
        }

        int currentAxis = axis % 3;
        double axisCenter = coordByAxis(center, currentAxis);
        double axisNode = coordByAxis(node.loc, currentAxis);
        double delta = axisCenter - axisNode;

        if (delta <= 0) {
            rangeSearchRecursive(node.left, center, radiusSquared, axis + 1, results);
            if (delta * delta <= radiusSquared) {
                rangeSearchRecursive(node.right, center, radiusSquared, axis + 1, results);
            }
        } else {
            rangeSearchRecursive(node.right, center, radiusSquared, axis + 1, results);
            if (delta * delta <= radiusSquared) {
                rangeSearchRecursive(node.left, center, radiusSquared, axis + 1, results);
            }
        }
    }


    @Override
    public Iterator<KDNode<V>> iterator() {
        return new KDTreeIterator<>(this.root);
    }
}