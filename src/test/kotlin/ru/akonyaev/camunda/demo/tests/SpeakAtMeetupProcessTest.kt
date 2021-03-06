package ru.akonyaev.camunda.demo.tests

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.kotlintest.shouldBe
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.camunda.bpm.engine.runtime.ProcessInstance
import org.camunda.bpm.engine.test.Deployment
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat
import org.camunda.bpm.engine.test.mock.Mocks
import org.camunda.bpm.extension.process_test_coverage.junit.rules.ProcessCoverageInMemProcessEngineConfiguration
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRule
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder
import org.junit.After
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import ru.akonyaev.camunda.demo.camunda.FILE_ID
import ru.akonyaev.camunda.demo.camunda.PRESENTATION_DATE
import ru.akonyaev.camunda.demo.camunda.ProcessDefinitionKey
import ru.akonyaev.camunda.demo.camunda.REVIEW_ITERATION
import ru.akonyaev.camunda.demo.camunda.TEMPLATE_ID
import ru.akonyaev.camunda.demo.camunda.TITLE
import ru.akonyaev.camunda.demo.component.PresentationStorage
import ru.akonyaev.camunda.demo.model.SpeakAtMeetupRequest
import ru.akonyaev.camunda.demo.process.ProcessDefinitionName
import ru.akonyaev.camunda.demo.process.preparePresentation.FixRemarksTaskHandler
import ru.akonyaev.camunda.demo.process.preparePresentation.WriteDraftTaskHandler
import ru.akonyaev.camunda.demo.process.speakAtMeetup.AgreeAnAppointmentTaskHandler
import ru.akonyaev.camunda.demo.process.speakAtMeetup.PrepareDemoProjectTaskHandler
import ru.akonyaev.camunda.demo.process.speakAtMeetup.ShareVideoTaskHandler
import ru.akonyaev.camunda.demo.utils.getDelegateName
import ru.akonyaev.camunda.demo.utils.toDelegate
import java.util.UUID

@Deployment(
    resources = [
        "bpmn/speakAtMeetup.bpmn",
        "bpmn/preparePresentation.bpmn"
    ]
)
class SpeakAtMeetupProcessTest {

    private val runtimeService = BpmnAwareTests.runtimeService()
    private lateinit var process: ProcessInstance

    private val request = SpeakAtMeetupRequest(
        requestId = UUID.randomUUID().toString(),
        title = "Camunda pitfalls",
        correlationId = UUID.randomUUID().toString(),
    )
    private val presentationFileId = "12345"
    private val presentationStorage: PresentationStorage = mock()

    private val processId: ProcessDefinitionKey = ProcessDefinitionName.SpeakAtMeetupProcess
    private val businessKey: String = request.requestId
    private val defaultVariables: Map<String, Any?> = request.toProcessVariablesMap()

    @Before
    fun setUp() {
        listOf(
            AgreeAnAppointmentTaskHandler(),
            PrepareDemoProjectTaskHandler(),
            ShareVideoTaskHandler(presentationStorage),
            WriteDraftTaskHandler(presentationStorage),
            FixRemarksTaskHandler()
        ).forEach {
            Mocks.register(it.getDelegateName(), it.toDelegate())
        }

        whenever(presentationStorage.create(any(), any())).thenReturn(presentationFileId)
    }

    @After
    fun tearDown() {
        val runtime = BpmnAwareTests.runtimeService()
        val processIds = runtime.createProcessInstanceQuery().active().list().map { it.id }
        runtime.deleteProcessInstances(
            processIds,
            "stop all processes after test",
            true,
            true,
            true
        )
        Mocks.reset()
    }

    @Test
    fun `test process with sub process`() {
        runProcess()

        assertThat(process)
            .isStarted
            .hasPassed("StartEvent")
            .isWaitingAt("AgreeAnAppointmentTask")

        executeJob("AgreeAnAppointmentTask")
        hasVariables(PRESENTATION_DATE)

        assertThat(process).isWaitingAt("PrepareDemoProjectTask", "PreparePresentationTask")

        executeJob("PrepareDemoProjectTask")

        // ?????????????? ?????????????????? ??????????, ?????????? ?????????????????? ???? ????????????????????
        executeJob("PreparePresentationTask")
        runSubProcess(ProcessDefinitionName.PreparePresentationProcess) {
            hasVariables(
                TITLE to request.title,
                TEMPLATE_ID to "tinkoff"
            )

            executeJob("WriteDraftTask")
            hasVariables(
                REVIEW_ITERATION to 0,
                FILE_ID to presentationFileId
            )
            setVariable(REVIEW_ITERATION to 2)
            jump(from = "DoReviewTask", to = "FixRemarksTask")
            executeJob("FixRemarksTask")

            assertThat(process).isEnded
        }
        hasVariables(FILE_ID to presentationFileId)

        jump(from = "SpeakAtMeetupTask", to = "ShareVideoTask")
        executeJob("ShareVideoTask")
        verify(presentationStorage).share(
            check {
                it shouldBe presentationFileId
            }
        )

        assertThat(process)
            .isEnded
            .hasPassed("EndEvent")
    }

    private fun hasVariables(vararg names: String) {
        assertThat(process).variables().containsKeys(*names)
    }

    private fun hasVariables(vararg namesAndValues: Pair<String, Any?>) {
        assertThat(process).variables().containsAllEntriesOf(namesAndValues.toMap())
    }

    private fun setVariable(vararg namesAndValues: Pair<String, Any?>) {
        namesAndValues.forEach {
            runtimeService.setVariable(process.id, it.first, it.second)
        }
    }

    private fun executeJob(jobName: String) =
        BpmnAwareTests.execute(BpmnAwareTests.jobQuery().activityId(jobName).singleResult())

    private fun jump(from: String, to: String) {
        runtimeService.createProcessInstanceModification(process.id)
            .apply { startBeforeActivity(to) }
            .also { it.cancelAllForActivity(from) }
            .execute()
    }

    private fun runProcess(variables: Map<String, Any?> = emptyMap()) {
        process = runtimeService.startProcessInstanceByKey(
            processId.name,
            businessKey,
            defaultVariables.toMutableMap().apply { putAll(variables) }
        )
    }

    private fun runSubProcess(processDefinitionKey: ProcessDefinitionKey, assertions: ProcessInstance.() -> Unit) {
        val previousProcess = process
        try {
            process = runtimeService
                .createProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey.name)
                .singleResult()
            process.apply(assertions)
        } finally {
            process = previousProcess
        }
    }

    companion object {
        private val processEngine: ProcessEngine by lazy {
            val processEngineConfigurationInit: (ProcessEngineConfigurationImpl) -> Unit = {}
            ProcessCoverageInMemProcessEngineConfiguration()
                .also(processEngineConfigurationInit)
                .buildProcessEngine()
        }

        @Rule
        @ClassRule
        @JvmField
        val processEngineRule: TestCoverageProcessEngineRule = TestCoverageProcessEngineRuleBuilder
            .create(processEngine)
            .build()
    }
}

