package social.firefly.common.tree

class MutableTreeNode<T>(
    override val value: T
): TreeNode<T> {
    private val _branches: MutableList<TreeNode<T>> = mutableListOf()
    override val branches: List<TreeNode<T>> = _branches

    override val isLeaf: Boolean
        get() = branches.isEmpty()

    fun add(tree: TreeNode<T>) {
        _branches.add(tree)
    }

    fun remove(tree: TreeNode<T>) {
        _branches.remove(tree)
    }
}

interface TreeNode<T> {
    val branches: List<TreeNode<T>>
    val isLeaf: Boolean
    val value: T
}