package Model

object Fish_ {

	//Model
	case class Fish(
		_id : Int,
		FishId : String,
		name : String,
		bells : Int,
		availability : List[String],
		rarity : Int,
		img : String
	)
	//Arguments
	case class monthArgs(month : String)
}
