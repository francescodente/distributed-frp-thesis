class SimulationIncarnation(environment: Environment,
                            sources: Set[Int] = Set.empty,
                            obstacles: Set[Int] = Set.empty)
  extends Incarnation
    with IncarnationWithEnvironment(environment)
    with TestLocalSensors
    with TestNeighborSensors:


  override type Context = SimulationContext
  override type NeighborState = SimulationNeighborState

  override def context(selfId: DeviceId): Context =
    new SimulationContext(selfId)

  class SimulationContext(val selfId: DeviceId) extends BasicContext:
    private val neighborsSink =
      IncrementalCellSink[Map[DeviceId, NeighborState]](
        Map.empty,
        calm = true
      )

    def receiveExport(neighborId: DeviceId, exported: Export[Any]): Unit =
      val newState = SimulationNeighborState(selfId, neighborId, exported)
      neighborsSink.update(_ + (neighborId -> newState))

    override def neighbors: Cell[Map[DeviceId, NeighborState]] =
      neighborsSink.cell

    import SimulationLocalSensor._
    override def sensor[A](id: LocalSensorId): Cell[A] = id match
      case Source => new Cell(sources.contains(selfId).asInstanceOf[A])
      case Obstacle => new Cell(obstacles.contains(selfId).asInstanceOf[A])

  case class SimulationNeighborState(selfId: DeviceId,
                                     neighborId: DeviceId,
                                     exported: Export[Any])
    extends BasicNeighborState:
    import SimulationNeighborSensor._
    override def sensor[A](id: NeighborSensorId): A = id match
      case NbrRange =>
        val selfPos = environment.position(selfId)
        val neighborPos = environment.position(neighborId)
        hypot(selfPos._1 - neighborPos._1, selfPos._2 - neighborPos._2)
          .asInstanceOf[A]