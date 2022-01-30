package ru.akonyaev.camunda.demo.configuration.camunda

import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.support.GenericApplicationContext
import ru.akonyaev.camunda.demo.task.TaskDelegate
import ru.akonyaev.camunda.demo.task.TaskHandler
import ru.akonyaev.camunda.demo.utils.getDelegateName

class TaskDelegateRegistrar(
    private val appContext: GenericApplicationContext
) : BeanPostProcessor {

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        if (bean is TaskHandler<*, *>) {
            appContext.registerBean(bean.getDelegateName(), TaskDelegate::class.java, bean)
        }
        return bean
    }
}
