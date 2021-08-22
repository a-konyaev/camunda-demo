package ru.akonyaev.camunda.demo.tests

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.kotlintest.shouldBe
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.extension.mockito.delegate.DelegateExecutionFake
import org.junit.jupiter.api.Test
import ru.akonyaev.camunda.demo.component.PresentationStorage
import ru.akonyaev.camunda.demo.process.fileId
import ru.akonyaev.camunda.demo.process.preparePresentation.WriteDraftDelegate
import ru.akonyaev.camunda.demo.process.reviewIteration
import ru.akonyaev.camunda.demo.process.templateId
import ru.akonyaev.camunda.demo.process.title

class WriteDraftDelegateTest {

    private val execution: DelegateExecution = DelegateExecutionFake()
    private val presentationStorage: PresentationStorage = mock()
    private val delegate = WriteDraftDelegate(presentationStorage)

    @Test
    fun `when delegate executed then process variables are set`() {
        // given
        whenever(presentationStorage.create(any(), any())).thenReturn("12345")
        execution.title = "Camunda pitfalls"
        execution.templateId = "tinkoff"

        // when
        delegate.execute(execution)

        // then
        execution.reviewIteration shouldBe 0
        execution.fileId shouldBe "12345"
        verify(presentationStorage).create(
            check { it shouldBe "Camunda pitfalls" },
            check { it shouldBe "tinkoff" }
        )
    }
}
