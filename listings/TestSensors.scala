trait TestLocalSensors:
  self: Incarnation =>

  override type LocalSensorId = SimulationLocalSensor
  enum SimulationLocalSensor:
    case Source
    case Obstacle

  import SimulationLocalSensor._
  def source: Flow[Boolean] = sensor[Boolean](Source)
  def obstacle: Flow[Boolean] = sensor[Boolean](Obstacle)

trait TestNeighborSensors:
  self: Incarnation =>

  override type NeighborSensorId = SimulationNeighborSensor
  enum SimulationNeighborSensor:
    case NbrRange

  import SimulationNeighborSensor._
  def nbrRange: Flow[NeighborField[Double]] = nbrSensor[Double](NbrRange)