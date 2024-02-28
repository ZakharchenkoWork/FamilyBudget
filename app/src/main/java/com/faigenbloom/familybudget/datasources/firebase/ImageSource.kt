package com.faigenbloom.familybudget.datasources.firebase

import androidx.core.net.toUri
import com.faigenbloom.familybudget.common.executeSuspendable
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ImageSource(
    private val storage: FirebaseStorage,
    private val imageStoreDirectory: File,
) {
    suspend fun upload(imageUri: String): Boolean {
        return executeSuspendable { callback ->
            val storageRef = storage.reference
            val uri = imageUri.toUri()
            val imageRef = storageRef.child(uri.lastPathSegment ?: "")
            imageRef.putFile(uri).addOnSuccessListener { taskSnapshot ->
                callback(true)
            }.addOnFailureListener {
                callback(false)
            }
        }
    }

    suspend fun download(imageUri: String): Boolean {
        return executeSuspendable { callback ->
            val storageRef = storage.reference
            imageStoreDirectory
            val uri = imageUri.toUri()
            val imageRef = storageRef.child(uri.lastPathSegment ?: "")
            val localFile = File(
                imageStoreDirectory,
                imageRef.name,
            )
            imageRef.getFile(localFile)
                .addOnSuccessListener {
                    callback(true)
                }.addOnFailureListener {
                    callback(false)
                }
        }
    }
}
