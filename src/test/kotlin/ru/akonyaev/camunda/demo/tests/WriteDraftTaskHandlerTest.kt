package ru.akonyaev.camunda.demo.tests

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import ru.akonyaev.camunda.demo.component.PresentationStorage
import ru.akonyaev.camunda.demo.process.preparePresentation.WriteDraftTaskHandler

class WriteDraftTaskHandlerTest {

    private val presentationStorage: PresentationStorage = mock()
    private val taskHandler = WriteDraftTaskHandler(presentationStorage)

    @Test
    fun `when delegate executed then process variables are set`() {
        // given
        whenever(presentationStorage.create(any(), any())).thenReturn("12345")
        val input = WriteDraftTaskHandler.Input(
            title = "Camunda pitfalls",
            templateId = "tinkoff"
        )

        // when
        val output = taskHandler.handle(input)

        // then
        with(output) {
            reviewIteration shouldBe 0
            fileId shouldBe "12345"
        }
        verify(presentationStorage).create(
            check { it shouldBe "Camunda pitfalls" },
            check { it shouldBe "tinkoff" }
        )
    }
}
