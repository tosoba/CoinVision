package com.trm.coinvision.core.domain.exception

import io.ktor.http.HttpStatusCode

class HttpException(val status: HttpStatusCode) : Exception()
