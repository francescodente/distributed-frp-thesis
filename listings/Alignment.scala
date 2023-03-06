private def alignWithNeighbors[T](
  path: Path,
  extract: (Export[Any], NeighborState) => T
)(
  using ctx: Context
): Cell[Map[DeviceId, T]] =

  def alignWith(nbrId: DeviceId, nbrState: NeighborState): Option[(DeviceId, T)] =
    nbrState.exported.followPath(path)
      .map(exp => (nbrId, extract(exp, nbrState)))

  ctx.neighbors.map(_.flatMap { (nbrId, nbrState) =>
    alignWith(nbrId, nbrState)
  })