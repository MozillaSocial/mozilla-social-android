package social.firefly.core.ui.chooseAccount

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val chooseAccountModule = module {
    viewModelOf(::ChooseAccountDialogViewModel)
}