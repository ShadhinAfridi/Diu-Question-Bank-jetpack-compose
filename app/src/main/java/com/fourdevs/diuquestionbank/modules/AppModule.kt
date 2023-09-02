package com.fourdevs.diuquestionbank.modules

import android.content.Context
import com.fourdevs.diuquestionbank.core.ApiClient
import com.fourdevs.diuquestionbank.core.ApiClientImpl
import com.fourdevs.diuquestionbank.firebase.FirebaseCloudStorage
import com.fourdevs.diuquestionbank.firebase.FirebaseCloudStorageImpl
import com.fourdevs.diuquestionbank.repository.QuestionRepository
import com.fourdevs.diuquestionbank.repository.QuestionRepositoryOnline
import com.fourdevs.diuquestionbank.room.AppDatabase
import com.fourdevs.diuquestionbank.room.dao.QuestionDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun providerFirebaseStorage(): FirebaseStorage  = Firebase.storage

    @Provides
    fun providesStorageReference(storage: FirebaseStorage): StorageReference = storage.reference

    @Provides
    fun providesQuestionRepositoryOnline(online: QuestionRepositoryOnline): QuestionRepository =
        online

    @Provides
    fun providesApiHttpClient(): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                json()
            }
        }
    }

    @Provides
    fun providesAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return AppDatabase.getDatabase(context)!!
    }

    @Provides
    fun providesQuestionDao(db: AppDatabase): QuestionDao = db.questionDao()


    @Provides
    fun providesApiClient(impl: ApiClientImpl): ApiClient = impl

    @Provides
    fun providesFirebaseCloudStorage(impl: FirebaseCloudStorageImpl): FirebaseCloudStorage = impl




}