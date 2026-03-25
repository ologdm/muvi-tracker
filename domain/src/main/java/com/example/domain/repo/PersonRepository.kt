package com.example.domain.repo

import com.example.domain.IoResponse
import com.example.domain.model.Ids
import com.example.domain.model.Person

interface PersonRepository {

    suspend fun getPersonDetail(personIds: Ids): IoResponse<Person>

}

