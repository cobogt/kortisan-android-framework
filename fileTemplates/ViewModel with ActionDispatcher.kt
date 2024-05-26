#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}.domain.viewmodels#end

import android.app.Application
import com.kortisan.content.presentation.tagging.${NAME}SceneBuilder
import com.kortisan.framework.bloc.ReduxViewModelBase
import com.kortisan.framework.bloc.ViewModelActionDispatcher
import com.kortisan.framework.redux.stores.ApplicationStateStore
import kotlinx.coroutines.flow.flow

#parse("File Header.java")
class ${NAME}ViewModel(
    application: Application,
    vmActionDispatcher: ViewModelActionDispatcher
): ReduxViewModelBase( application, vmActionDispatcher )  {
    private val taggingScene${NAME} = ${NAME}SceneBuilder.Builder()
        .setRootName("${NAME}")
        .build()
}