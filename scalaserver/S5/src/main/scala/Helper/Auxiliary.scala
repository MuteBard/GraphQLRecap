package Helper

import java.util.Calendar
object Auxiliary {

	def date(): String = {
		val dt = Calendar.getInstance()
		val month = if (dt.get(Calendar.MONTH) < 10) "0"+(dt.get(Calendar.MONTH)+1) else dt.get(Calendar.MONTH)+1
		val day = dt.get(Calendar.DAY_OF_MONTH)
		val year = dt.get(Calendar.YEAR)
		val hour = if (dt.get(Calendar.HOUR_OF_DAY) < 10) "0"+dt.get(Calendar.HOUR_OF_DAY) else dt.get(Calendar.HOUR_OF_DAY)
		val minute = if (dt.get(Calendar.MINUTE) < 10) "0"+dt.get(Calendar.MINUTE) else dt.get(Calendar.MINUTE)
		val second = if (dt.get(Calendar.SECOND) < 10) "0"+dt.get(Calendar.SECOND) else dt.get(Calendar.SECOND)
		val millisecond = if (dt.get(Calendar.MILLISECOND) < 10) "00"+dt.get(Calendar.MILLISECOND) else if (dt.get(Calendar.MILLISECOND) < 100) "0"+dt.get(Calendar.MILLISECOND) else dt.get(Calendar.MILLISECOND)
		s"$month/$day/$year $hour:$minute:$second:$millisecond"
	}

	def todayDateId(): String = {
		val dt = Calendar.getInstance()
		val month = if (dt.get(Calendar.MONTH) < 10) "0"+(dt.get(Calendar.MONTH)+1) else dt.get(Calendar.MONTH)+1
		val day = if (dt.get(Calendar.DAY_OF_MONTH) < 10) "0"+dt.get(Calendar.DAY_OF_MONTH) else dt.get(Calendar.DAY_OF_MONTH)
		val year = dt.get(Calendar.YEAR)
		s"$year$month$day"
	}

	def month(): Int = {
		val dt = Calendar.getInstance()
		val month = dt.get(Calendar.MONTH)+1
		month
	}

	def day(): Int= {
		val dt = Calendar.getInstance()
		val day = dt.get(Calendar.DAY_OF_MONTH)
		day
	}

	object log {
		def info(className : String, message : String): Unit = {
			println(s"[INFO] [${date()}] [#####################################] [$className] $message")
		}
		def info(className : String, method: String, message : String): Unit = {
			println(s"[INFO] [${date()}] [#####################################] [$className] [$method] $message")
		}
		def info(className : String, method: String, status: String, message : String): Unit = {
			println(s"[INFO] [${date()}] [#####################################] [$className] [$method] [$status] $message")
		}
		def warn(className : String, message : String) : Unit = {
			println(s"[WARN] [${date()}] [#####################################] [$className] $message")
		}
		def warn(className : String, method: String, message : String): Unit = {
			println(s"[WARN] [${date()}] [#####################################] [$className] [$method] $message")
		}
		def warn(className : String, method: String, status: String, message : String): Unit = {
			println(s"[WARN] [${date()}] [#####################################] [$className] [$method] [$status] $message")
		}
	}






	def log(loglevel : String, className : String, path : String, message : String) : String = {
		val dt = Calendar.getInstance()
		val month = dt.get(Calendar.MONTH)
		val day = dt.get(Calendar.DAY_OF_MONTH)
		val year = dt.get(Calendar.YEAR)
		val hour = dt.get(Calendar.HOUR)
		val minute = dt.get(Calendar.MINUTE)
		val second = dt.get(Calendar.SECOND)
		val millisecond = dt.get(Calendar.MILLISECOND)
		s"[$loglevel][$className][$month/$day/$year $hour:$minute:$second:$millisecond]"
	}


}

