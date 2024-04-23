package social.firefly.common.tree

@Suppress("ComplexCondition")
fun <T> TreeNode<T>.toDepthList(
    depth: Int = 0,
    depthLines: List<Int> = listOf(),
    shouldIgnoreChildren: (T) -> Boolean,
    shouldAddAllChildrenBeyondLimit: (T) -> Boolean,
    childLimit: Int,
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
            val isLastBranch = index == branches.size - 1
            if (isLastBranch) {
                newDepthLines = newDepthLines.toMutableList().apply {
                    remove(depth)
                }
            }
            if (shouldAddAllChildrenBeyondLimit(value) || index < childLimit || depth == 0) {
                addAll(
                    treeNode.toDepthList(
                        depth + 1,
                        newDepthLines,
                        shouldIgnoreChildren,
                        shouldAddAllChildrenBeyondLimit,
                        childLimit,
                    )
                )
            }
            // add depth item that represents a view more button
            if (isLastBranch && index >= childLimit && !shouldAddAllChildrenBeyondLimit(value) && depth != 0) {
                add(
                    DepthItem(
                        value,
                        depth + 1,
                        newDepthLines,
                        hasReplies = false,
                        hiddenRepliesCount = index - childLimit + 1
                    )
                )
            }
        }
    }
}

data class DepthItem<T>(
    val value: T,
    val depth: Int,
    val depthLines: List<Int>,
    val hasReplies: Boolean,
    val hiddenRepliesCount: Int = -1,
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
