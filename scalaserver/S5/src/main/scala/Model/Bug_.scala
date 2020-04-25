package Model

object Bug_ {

	//Model
	case class Bug(
		_id : Int,
		bugId : String,
		name : String,
		bells : Int,
		availability : List[String],
		rarity : Int,
		img : String
	)
	//Arguments
	case class monthArgs(month : String)
}
