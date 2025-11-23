package com.example.chitchat.feature_chat.data.repository

import com.example.chitchat.core.domain.model.Result
import com.example.chitchat.feature_chat.domain.model.Message
import com.example.chitchat.feature_chat.domain.repository.ChatRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): ChatRepository{
    override fun getMessages(conversationId: String): Flow<List<Message>> = callbackFlow {
        val messagesCollection = firestore.collection("conversations")
            .document(conversationId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)

        val listener = messagesCollection.addSnapshotListener { snapshot,e->
            if(e!=null){
                close(e)
                return@addSnapshotListener
            }
            val messages = snapshot?.toObjects(Message::class.java)?:emptyList()
            trySend(messages)
        }
        awaitClose { listener.remove() }
    }

    override suspend fun sendMessage(conversationId: String, message: Message): Result<Unit> {
        return try{
            val conversationRef = firestore.collection("conversations").document(conversationId)

            val newMessageRef = conversationRef.collection("messages").document()

            val lastMessageData = mapOf(
                "lastMessageText" to message.text,
                "lastMessageTimestamp" to Timestamp.now(),
                "lastMessageSenderId" to message.senderId
            )

            firestore.runBatch { batch ->
                batch.set(newMessageRef, message.copy(messageId = newMessageRef.id))
                batch.update(conversationRef, lastMessageData)
            }.await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getOrCreateConversation(otherUserId: String): Result<String> {
        val currentUserId = auth.currentUser?.uid?:return Result.Error(Exception("User not logged in"))

        val conversationId = if (currentUserId > otherUserId){
            "${currentUserId}_${otherUserId}"
        }else{
            "${otherUserId}_${currentUserId}"
        }

        return try {
            val conversationDoc = firestore.collection("conversations").document(conversationId)
            val docSnapshot = conversationDoc.get().await()

            if (!docSnapshot.exists()) {
                val participants = mapOf("participants" to listOf(currentUserId, otherUserId))
                conversationDoc.set(participants).await()
            }

            Result.Success(conversationId)

        }catch (e: Exception){
            Result.Error(e)
        }


    }


}