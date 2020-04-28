package Model

object Bug_ {

	//Model
	case class Bug(
		id : Int = -1,
		bugId : String = "B-1",
		name : String = "",
		bells : Int = -1,
		availability : List[String] = List(),
		rarity : Int = -1,
		img : String = ""
	)
	//Arguments
	case class bugMonthsArgs(months : List[String])
	case class bugIdArgs(bugId : String)
	case class bugNameArgs(name : String)

}
