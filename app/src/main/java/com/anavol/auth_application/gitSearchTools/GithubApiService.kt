package com.anavol.auth_application.gitSearchTools

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApiService {

    @GET("search/users")
    fun search(@Query("q") query: String,
             //  @Query("order") order: String,
            //   @Query("page") page: Int,
               @Query("per_page") perPage: Int
        ): Observable<Result>

    companion object{ // перенести в класс Вебапи и там создать это инстанс, функцию дженерик криэйтСервис, принимает интерфейс отдает инстанс
        fun create(): GithubApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .build()

            return retrofit.create(GithubApiService::class.java) // вызвать в функции криэйтСервис и передать иснтанс, объект? не статическая функция

        }
    }
}