package com.example.editingserver.exception

enum class ExceptionCode(val code: String, val message: String) {

    /**
     * document error code
     */
    NOT_FOUND_DOC_ELEMENTS("404", "서버 내 문서 오류"),
    NOT_FOUND_DOC("404", "문서를 찾을 수 없습니다.")

}