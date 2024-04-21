package social.firefly.common.tree

fun <T> TreeNode<T>.toDepthList(
    depth: Int = 0,
    depthLines: List<Int> = listOf(),
    shouldIgnoreChildren: (T) -> Boolean,
): List<DepthItem<T>> = buildList {
    var newDepthLines = buildList {
        addAll(depthLines)
        if (!isLeaf) {
            add(depth)
        }
    }
    add(
        DepthItem(
            value,
            depth,
            newDepthLines,
            hasReplies = !isLeaf,
        )
    )
    if (!shouldIgnoreChildren(value)) {
        branches.forEachIndexed { index, treeNode ->
            if (index == branches.size - 1) {
                newDepthLines = newDepthLines.toMutableList().apply {
                    remove(depth)
                }
            }
            addAll(
                treeNode.toDepthList(
                    depth + 1,
                    newDepthLines,
                    shouldIgnoreChildren,
                )
            )
        }
    }
}

data class DepthItem<T>(
    val value: T,
    val depth: Int,
    val depthLines: List<Int>,
    val hasReplies: Boolean,
)

fun <T> List<T>.toTree(
    identifier: (T) -> String,
    parentIdentifier: (T) -> String?,
): TreeNode<T>? {
    val nodes = mutableMapOf<String, MutableTreeNode<T>>()

    forEach {
        val newNode = MutableTreeNode(it)
        nodes[parentIdentifier(it)]?.add(newNode)
        nodes[identifier(it)] = newNode
    }

    return nodes[identifier(first())]
}
