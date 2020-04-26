package Model
import Pocket_.Pocket
import TurnipTransaction_.TurnipTransaction

object User_ {
	case class User(
		_id : Int = -1,
		username : String = "NULL/USER",
		fishingPoleLvl : Int = -1,
		bugNetLvl : Int = -1,
		bells : Int = -1,
		pocket : Pocket = Pocket(),
		liveTurnips : TurnipTransaction = TurnipTransaction(),
		turnipTransactionHistory : List[TurnipTransaction] = List(),
		img : String = "",
	)

	//Arguments
	case class usernameArgs(username : String)
	case class sellCreatureArgs(username : String, creaturename : String, species : String)

}
