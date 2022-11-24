package com.example.editingserver.exception

open class CustomException(code: ExceptionCode) : RuntimeException(code.message) {

}