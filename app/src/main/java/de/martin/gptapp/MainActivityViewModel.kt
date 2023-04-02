package de.martin.gptapp

import android.util.Log
import androidx.lifecycle.*
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val CHAT_GPT_API_KEY = ""

class MainActivityViewModel : ViewModel() {
    var userGptQuery = "Hey Gpt, what is up?"
    var gptResponse = ""
    val gptResponseLiveData = MutableLiveData("")
    private val scope = CoroutineScope(Dispatchers.Main)

    fun updateGptQueryQuestion(query: String) {
        userGptQuery = query
    }

    @OptIn(BetaOpenAI::class)
    fun makeGptCall() = scope.launch {
        val openAI = OpenAI(CHAT_GPT_API_KEY)
        try {
            Log.i("GPTAPP", "Starting request: $userGptQuery")
            val chatCompletionRequest = ChatCompletionRequest(
                model = ModelId("gpt-3.5-turbo"), messages = listOf(
                    ChatMessage(
                        role = ChatRole.User, content = userGptQuery
                    )
                )
            )
            val completion: ChatCompletion = openAI.chatCompletion(chatCompletionRequest)

            val response = completion.choices.first().message?.content

            gptResponse = response ?: "..."
            gptResponseLiveData.value = gptResponseLiveData.value + "\n \n" + gptResponse
            Log.i("GPTAPP", "response: $gptResponse")
        } catch (e: Exception) {
            gptResponse = "Sorry, no response"
            Log.i("GPTAPP", "fail with: $e")
        }
    }
}