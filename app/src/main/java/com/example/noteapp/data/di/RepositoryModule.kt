package com.example.noteapp.data.di

import com.example.noteapp.data.repo.AuthRepository
import com.example.noteapp.data.repo.AuthRepositoryImp
import com.example.noteapp.data.repo.NoteRepository
import com.example.noteapp.data.repo.NoteRepositoryImp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNoteRepository(
        database: FirebaseFirestore
    ): NoteRepository {
        return  NoteRepositoryImp(database)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        database: FirebaseFirestore,
        auth: FirebaseAuth
    ): AuthRepository {
        return  AuthRepositoryImp(auth,database)
    }

}