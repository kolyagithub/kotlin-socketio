package blog

/**
 * Created by data-mc on 11/29/2017.
 */
class ChatObject {

    private var userName: String = "";
    private var message: String = ""

    constructor() {
    }

    constructor(userName: String, message: String) {
        this.userName = userName
        this.message = message
    }

    fun getUserName(): String {
        return userName
    }
    fun setUserName(userName: String) {
        this.userName = userName
    }

    fun getMessage(): String {
        return message
    }
    fun setMessage(message: String) {
        this.message = message
    }

}