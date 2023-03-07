class SimulationIncarnation(environment: Environment,
                            sources: Cell[Set[Int]] = new Cell(Set.empty),
                            obstacles: Cell[Set[Int]] = new Cell(Set.empty))
  extends Incarnation
    with IncarnationWithEnvironment(environment)
    with TestLocalSensors
    with TestNeighborSensors:
  ...
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
      neighborsSink.Cell
  ...