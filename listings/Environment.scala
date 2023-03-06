trait Environment:
  def nDevices: Int
  def position(device: Int): (Double, Double)
  def neighbors(device: Int): Iterable[Int]