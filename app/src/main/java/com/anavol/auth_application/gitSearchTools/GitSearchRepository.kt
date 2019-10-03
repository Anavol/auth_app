package com.anavol.auth_application.gitSearchTools

import io.reactivex.Observable

class GitSearchRepository(val apiService: GithubApiService) {
    fun searchUsers(login: String, perPage: Int, page: Int): Observable<Result> {
        return apiService.search(query= login, perPage =  perPage, page= page)
    }
}