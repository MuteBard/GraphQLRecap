package Model

object TurnipTransaction_ {

	case class TurnipTransaction(
		business: String = "",
		quantity: Int = 0,
		marketPrice : Int = 0,
		totalBells: Int = 0,
		netGainLossAsBells : Int = 0,
		netGainLossAsPercentage: Int = 0
	)
}
