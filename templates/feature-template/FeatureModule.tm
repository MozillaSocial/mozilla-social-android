package ${PACKAGE_NAME}

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ${MODULE_NAME}Module = module {
    viewModel { parameters -> ${MODULE_NAME}ViewModel(get()) }
}