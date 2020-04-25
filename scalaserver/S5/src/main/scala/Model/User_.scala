package Model
import Pocket_.Pocket
import TurnipTransaction_.TurnipTransaction

object User_ {
	case class User(
		_id : Int,
		username : String,
		fishingPoleLvl : Int,
		bugNetLvl : Int,
		bells : Int,
		pocket : Pocket,
		liveTurnips : TurnipTransaction = TurnipTransaction(),
		turnipTransactionHistory : List[TurnipTransaction] = List(),
		img : String = "",
	)

}
