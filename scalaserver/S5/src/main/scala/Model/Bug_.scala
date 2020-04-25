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
	case class bugMonthsArgs(months : List[String])
	case class bugIdArgs(bugId : String)
	case class bugNameArgs(name : String)

}
