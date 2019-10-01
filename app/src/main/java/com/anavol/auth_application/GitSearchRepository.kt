package com.anavol.auth_application

import io.reactivex.Observable

class GitSearchRepository(val apiService: GithubApiService) {
    fun searchUsers(login: String): Observable<Result> {
        return apiService.search("login:$login", "desc",1,30  )
    }
}