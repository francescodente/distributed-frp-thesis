def runGradientSimulation(environment: Environment,
                          sources: Cell[Set[Int]],
                          obstacles: Cell[Set[Int]]): Unit =
  val incarnation = SimulationIncarnation(environment, sources, obstacles)
  val simulator = Simulator(incarnation)

  import simulator.incarnation._
  import simulator.incarnation.given

  def gradient(src: Flow[Boolean]): Flow[Double] =
    loop(Double.PositiveInfinity) { distance =>
      mux(src) {
        constant(0.0)
      } {
        liftTwice(nbrRange, nbr(distance))(_ + _).withoutSelf.min
      }
    }

  simulator.run {
    branch(obstacle) {
      constant(-1.0)
    } {
      gradient(source)
    }
  }

@main def gradientSample(): Unit =
  val sourcesSink = IncrementalCellSink(Set(0))
  val obstaclesSink = IncrementalCellSink(Set(2, 7, 12))
  val environment = Environment.manhattanGrid(5, 5)
  runGradientSimulation(environment, sourcesSink.cell, obstaclesSink.cell)