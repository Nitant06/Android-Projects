package com.example.chitchat.feature_conversations.data.repository


import com.example.chitchat.core.domain.model.Result
import com.example.chitchat.core.domain.model.User
import com.example.chitchat.feature_conversations.domain.model.Conversation
import com.example.chitchat.feature_conversations.domain.repository.ConversationsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ConversationsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : ConversationsRepository {

    override fun getConversations(): Flow<List<Conversation>> = callbackFlow {
        val currentUserId = firebaseAuth.currentUser?.uid
        if (currentUserId == null) {
            close(Exception("User not logged in"))
            return@callbackFlow
        }

        val listener = firestore.collection("conversations")
            .whereArrayContains("participants", currentUserId)
            .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }
                if (snapshot == null) {
                    return@addSnapshotListener
                }

                val conversations = snapshot.documents.mapNotNull { doc ->
                    val participants = doc.get("participants") as? List<String> ?: emptyList()
                    val otherUserId = participants.find { it != currentUserId }

                    otherUserId?.let {
                        Conversation(
                            id = doc.id,
                            lastMessageText = doc.getString("lastMessageText"),
                            lastMessageTimestamp = doc.getTimestamp("lastMessageTimestamp"),
                            lastMessageSenderId = doc.getString("lastMessageSenderId"),
                            otherUser = User(uid = it)
                        )
                    }
                }
                trySend(conversations)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun deleteConversation(conversationId: String): Result<Unit> {
        return try {
            val conversationRef = firestore.collection("conversations").document(conversationId)

            val messagesSnapshot = conversationRef.collection("messages").get().await()
            firestore.runBatch { batch ->
                messagesSnapshot.documents.forEach { doc ->
                    batch.delete(doc.reference)
                }
                batch.delete(conversationRef)
            }.await()

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}