package Model

object Fish_ {

	//Model
	case class Fish(
		id : Int = -1,
		fishId : String = "F-1",
		name : String = "",
		bells : Int = -1,
		availability : List[String] = List(),
		rarity : Int = -1,
		img : String = ""
	)
	//Arguments
	case class fishMonthsArgs(months : List[String])
	case class fishIdArgs(fishId : String)
	case class fishNameArgs(name : String)
}
