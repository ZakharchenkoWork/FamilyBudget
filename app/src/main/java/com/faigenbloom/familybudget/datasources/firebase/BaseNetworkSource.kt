package com.faigenbloom.familybudget.datasources.firebase

import android.util.Log
import com.faigenbloom.familybudget.common.Identifiable
import com.faigenbloom.familybudget.common.TAG
import com.faigenbloom.familybudget.common.executeSuspendable
import com.google.firebase.firestore.FirebaseFirestore

open class BaseNetworkSource(
    private val firestore: FirebaseFirestore,
) {
    protected suspend fun get(collectionId: String, id: String): Map<String, Any>? {
        return executeSuspendable { callback ->
            firestore.collection(collectionId)
                .document(id)
                .get()
                .addOnSuccessListener { result ->
                    callback(result.data)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG(), "Error getting documents. $collectionId for $id", exception)
                }
        }
    }

    protected suspend fun get(
        collectionId: String,
        document: String,
        id: String,
    ): Map<String, Any>? {
        return executeSuspendable { callback ->
            firestore.collection(collectionId)
                .document(document)
                .collection(document)
                .document(id)
                .get()
                .addOnSuccessListener { result ->
                    callback(result.data)
                }
                .addOnFailureListener { exception ->
                    Log.w(
                        TAG(),
                        "Error getting documents. $collectionId with $document for $id",
                        exception,
                    )
                }
        }
    }

    protected suspend fun getAsList(
        collectionId: String,
        document: String,
    ): List<Any> {
        return executeSuspendable { callback ->
            firestore.collection(collectionId)
                .document(document)
                .collection(document)
                .get()
                .addOnSuccessListener { result ->
                    callback(
                        result.mapNotNull {
                            it.data
                        },
                    )
                }
        }
    }

    protected suspend fun set(collectionId: String, document: String, innerId: String, data: Any) {
        return executeSuspendable { callback ->
            firestore.collection(collectionId)
                .document(document)
                .collection(document)
                .document(innerId)
                .set(data)
                .addOnSuccessListener {
                    callback(Unit)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG(), "Error getting documents.", exception)
                }
        }
    }

    protected suspend fun set(
        collectionId: String,
        document: String,
        data: List<Identifiable>,
    ) {
        return executeSuspendable { callback ->
            firestore.collection(collectionId)
                .document(document)
                .collection(document)
                .also { collection ->
                    data.forEach {
                        collection
                            .document(it.id)
                            .set(it)
                    }
                }
            callback(Unit)
        }
    }


    protected suspend fun set(collectionId: String, document: String, data: Any) {
        return executeSuspendable { callback ->
            firestore.collection(collectionId)
                .document(document)
                .set(data)
                .addOnSuccessListener {
                    callback(Unit)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG(), "Error getting documents.", exception)
                }
        }
    }
}
