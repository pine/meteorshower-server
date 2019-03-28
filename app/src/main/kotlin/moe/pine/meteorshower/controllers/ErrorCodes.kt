package moe.pine.meteorshower.controllers

enum class ErrorCodes(val code : String) {
    AUTH_STATE_MISMATCH("auth_state_mismatch"),
    AUTH_SAVE_USER_FAILURE("auth_save_user_failure"),
    AUTH_ISSUE_ACCESS_TOKEN_FAILURE("auth_issue_access_token_failure")
}