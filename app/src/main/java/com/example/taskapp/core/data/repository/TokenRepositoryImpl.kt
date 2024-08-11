package com.example.taskapp.core.data.repository

import com.example.taskapp.auth.data.remote.dto.TokenDto
import com.example.taskapp.core.data.remote.api.TokenApiClient
import com.example.taskapp.core.domain.repository.TokenRepository
import com.example.taskapp.core.domain.validator.DataError
import com.example.taskapp.core.domain.validator.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(private val tokenApiClient: TokenApiClient) :
    TokenRepository {
    override suspend fun validateToken(token: String): Result<TokenDto, DataError.Network> {
        return withContext(Dispatchers.IO) {
            try {
                val result = tokenApiClient.validateToken(token)

                if (result.isSuccessful && result.body() != null) {
                    Result.Success(result.body()!!)
                } else {
                    when (result.code()) {
                        400 -> Result.Error(DataError.Network.BAD_REQUEST)
                        401 -> Result.Error(DataError.Network.UNAUTHORIZED)
                        403 -> Result.Error(DataError.Network.FORBIDDEN)
                        408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
                        else -> Result.Error(DataError.Network.UNKNOWN)
                    }
                }
            } catch (e: IOException) {
                Result.Error(DataError.Network.NO_INTERNET)
            } catch (e: Exception) {
                Result.Error(DataError.Network.UNKNOWN)
            }
        }
    }
}