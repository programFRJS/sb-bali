import com.google.gson.annotations.SerializedName

data class SetPlayerRequest(
    @SerializedName("player_a") val playerA: List<String>,
    @SerializedName("player_b") val playerB: List<String>
)

data class SetPlayerResponse(
    val success: Boolean,
    val data: SetPlayerData?,
    val message: String
)

data class SetPlayerData(
    @SerializedName("player_a") val playerA: List<String>,
    @SerializedName("player_b") val playerB: List<String>
)