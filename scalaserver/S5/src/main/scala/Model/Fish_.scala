package Model

object Fish_ {

	//Model
	case class Fish(
		_id : Int,
		fishId : String,
		name : String,
		bells : Int,
		availability : List[String],
		rarity : Int,
		img : String
	)
	//Arguments
	case class fishMonthsArgs(months : List[String])
	case class fishIdArgs(fishId : String)
	case class fishNameArgs(name : String)
}
