package social.firefly.core.ui.chooseAccount

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import social.firefly.core.accounts.accountsModule

val chooseAccountModule = module {
    includes(
        accountsModule,
    )

    viewModelOf(::ChooseAccountDialogViewModel)
}