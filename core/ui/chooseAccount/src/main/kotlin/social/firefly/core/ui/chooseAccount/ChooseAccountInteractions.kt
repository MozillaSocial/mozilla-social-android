package social.firefly.core.ui.chooseAccount

interface ChooseAccountInteractions {
    fun onAccountClicked(accountId: String, domain: String)
    fun onDismissRequest()
}

object ChooseAccountInteractionsNoOp : ChooseAccountInteractions {
    override fun onAccountClicked(accountId: String, domain: String) = Unit
    override fun onDismissRequest() = Unit
}